import axios from 'axios';

interface OmieCategoria {
  codigo: string;
  descricao: string;
  dadosDRE?: { descricaoDRE: string };
}
interface OmieMovimento {
  detalhes: {
    dDtPagamento: string;
    cCodCateg: string;
  };
  resumo?: { nValPago?: number };
}
interface ProcessedCategories {
  mapaClassificacao: Record<string, string>;
}

const MAPA_CODIGO_GERENCIAL: Record<string, string> = {
  "1.0": "receitas_operacionais",
  "2.1": "custos_variaveis",
  "3.0": "despesas_com_pessoal",
  "3.1": "despesas_administrativas",
  "3.2": "pro_labore",
  "4.0": "investimentos",
  "5.0": "parcelamentos",
  "6.0": "entradas_nao_operacionais",
  "7.0": "saidas_nao_operacionais"
};
const CODIGO_POR_DESCRICAO: Record<string, string> = Object.entries(
  MAPA_CODIGO_GERENCIAL
).reduce((acc, [codigo, descricao]) => {
  acc[descricao] = codigo;
  return acc;
}, {} as Record<string, string>);

const apiClient = axios.create({
  baseURL: "https://app.omie.com.br/api/v1",
  timeout: 60000,
});

function formatDateToOmieApi(date: Date): string {
  const dia = String(date.getUTCDate()).padStart(2, '0');
  const mes = String(date.getUTCMonth() + 1).padStart(2, '0');
  const ano = date.getUTCFullYear();
  return `${dia}/${mes}/${ano}`;
}

async function buscarCategoriasPaginado(appKey: string, appSecret: string): Promise<OmieCategoria[]> {
  let todasAsCategorias: OmieCategoria[] = [];
  let paginaAtual = 1;
  let totalDePaginas = 1;
  do {
    const payload = {
      call: "ListarCategorias",
      app_key: appKey, app_secret: appSecret,
      param: [{ pagina: paginaAtual, registros_por_pagina: 500 }],
    };
    try {
        const response = await apiClient.post("/geral/categorias/", payload);
        const data = response.data;
        if (data?.categoria_cadastro) {
            todasAsCategorias = todasAsCategorias.concat(data.categoria_cadastro);
            totalDePaginas = data.total_de_paginas || 1;
        } else {
            break;
        }
    } catch (error) {
        console.error(`Erro ao buscar página ${paginaAtual} de categorias:`, error);
        break;
    }
    paginaAtual++;
  } while (paginaAtual <= totalDePaginas);
  return todasAsCategorias;
}

async function buscarMovimentosPaginado(
    appKey: string, 
    appSecret: string,
    dataInicio: Date, 
    dataFim: Date
): Promise<OmieMovimento[]> {
    let todosMovimentos: OmieMovimento[] = [];
    let paginaAtual = 1;
    let totalDePaginas = 1;

    const dataInicioApi = formatDateToOmieApi(dataInicio);
    const dataFimApi = formatDateToOmieApi(dataFim);

    do {
        const payload = {
            call: "ListarMovimentos",
            app_key: appKey, app_secret: appSecret,
            param: [{ 
                nPagina: paginaAtual, 
                nRegPorPagina: 500,
                dDtPagtoDe: dataInicioApi,
                dDtPagtoAte: dataFimApi
            }],
        };
        try {
            const response = await apiClient.post("/financas/mf/", payload);
            const data = response.data;
            if (data?.movimentos?.length > 0) {
                todosMovimentos = todosMovimentos.concat(data.movimentos);
                totalDePaginas = data.nTotPaginas || 1;
            } else {
                break;
            }
        } catch (error) {
            console.error(`Erro ao buscar página ${paginaAtual} de movimentos:`, error);
            break;
        }
        paginaAtual++;
    } while (paginaAtual <= totalDePaginas);
    return todosMovimentos;
}

function processarCategorias(todasAsCategorias: OmieCategoria[]): ProcessedCategories {
    const mapaClassificacao: Record<string, string> = {};
    for (const categoria of todasAsCategorias) {
        if (!categoria.codigo) continue;
        let codigoGerencialPrincipal: string | null = null;
        const descricao = categoria.descricao || "";
        const descricaoDRE = categoria.dadosDRE?.descricaoDRE;
        if (descricaoDRE) {
             for (const codigo in MAPA_CODIGO_GERENCIAL) {
                if (descricaoDRE.startsWith(codigo) || descricaoDRE === MAPA_CODIGO_GERENCIAL[codigo]) {
                    codigoGerencialPrincipal = codigo;
                    break;
                }
             }
        }
        if (!codigoGerencialPrincipal && CODIGO_POR_DESCRICAO[descricao]) {
            codigoGerencialPrincipal = CODIGO_POR_DESCRICAO[descricao];
        }
        if (!codigoGerencialPrincipal) {
            const match = descricao.match(/^(\d+(\.\d+)?)/);
            if (match) {
                const codigoEncontrado = match[1];
                if (MAPA_CODIGO_GERENCIAL[codigoEncontrado]) {
                    codigoGerencialPrincipal = codigoEncontrado;
                } else if (codigoEncontrado.includes('.')) {
                    const codigoPai = codigoEncontrado.substring(0, codigoEncontrado.lastIndexOf('.'));
                    if (MAPA_CODIGO_GERENCIAL[codigoPai]) {
                        codigoGerencialPrincipal = codigoPai;
                    }
                }
            }
        }
        if (codigoGerencialPrincipal) {
            mapaClassificacao[categoria.codigo] = codigoGerencialPrincipal;
        }
    }
    return { mapaClassificacao };
}

export async function gerarRelatorioFinanceiroGeral(
    appKey: string, 
    appSecret: string, 
    dataInicio: Date, 
    dataFim: Date
): Promise<Record<string, number>> {
  
  const [categorias, todosOsMovimentos] = await Promise.all([
    buscarCategoriasPaginado(appKey, appSecret),
    buscarMovimentosPaginado(appKey, appSecret, dataInicio, dataFim) 
  ]);

  const { mapaClassificacao } = processarCategorias(categorias);

  const resultados: Record<string, number> = Object.keys(MAPA_CODIGO_GERENCIAL).reduce((acc, codigo) => {
    acc[codigo] = 0;
    return acc;
  }, {} as Record<string, number>);

  for (const mov of todosOsMovimentos) {
    const detalhes = mov.detalhes;
    const codigoCategoriaOmie = detalhes?.cCodCateg;
    const dataPagamentoStr = detalhes?.dDtPagamento; 

    if (!codigoCategoriaOmie || !dataPagamentoStr) {
        continue;
    }

    const codigoGrupoPrincipal = mapaClassificacao[codigoCategoriaOmie];
    if (codigoGrupoPrincipal && resultados.hasOwnProperty(codigoGrupoPrincipal)) {
      resultados[codigoGrupoPrincipal] += mov.resumo?.nValPago || 0;
    }
  }

  return resultados;
}