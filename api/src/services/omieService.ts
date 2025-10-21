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
  "1.0": "1.0 - Receitas Operacionais",
  "2.1": "2.1 - Custos Variáveis",
  "3.0": "3.0 - Despesas com Pessoal",
  "3.1": "3.1 - Despesas Administrativas",
  "3.2": "3.2 - Pro-Labore",
  "4.0": "4.0 - Investimentos",
  "5.0": "5.0 - Parcelamentos",
  "6.0": "6.0 - Entradas Não Operacionais",
  "7.0": "7.0 - Saídas Não Operacionais"
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

async function buscarMovimentosPaginado(appKey: string, appSecret: string): Promise<OmieMovimento[]> {
    let todosMovimentos: OmieMovimento[] = [];
    let paginaAtual = 1;
    let totalDePaginas = 1;
    do {
        const payload = {
            call: "ListarMovimentos",
            app_key: appKey, app_secret: appSecret,
            param: [{ nPagina: paginaAtual, nRegPorPagina: 500 }],
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

export async function gerarRelatorioFinanceiroGeral(appKey: string, appSecret: string, dias: number): Promise<Record<string, number>> {
  const dataFim = new Date();
  const dataInicio = new Date();
  dataInicio.setDate(dataFim.getDate() - dias);
  const dataInicioStr = dataInicio.toISOString();
  const dataFimStr = dataFim.toISOString();

  const [categorias, todosOsMovimentos] = await Promise.all([
    buscarCategoriasPaginado(appKey, appSecret),
    buscarMovimentosPaginado(appKey, appSecret)
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

    if (!codigoCategoriaOmie || !dataPagamentoStr || dataPagamentoStr.length !== 10) {
        continue;
    }

    try {
        const [dia, mes, ano] = dataPagamentoStr.split('/');
        const dataPagamentoISO = `${ano}-${mes}-${dia}T12:00:00.000Z`;
        if (dataPagamentoISO < dataInicioStr || dataPagamentoISO > dataFimStr) {
            continue;
        }
    } catch(e) {
        continue;
    }

    const codigoGrupoPrincipal = mapaClassificacao[codigoCategoriaOmie];
    if (codigoGrupoPrincipal && resultados.hasOwnProperty(codigoGrupoPrincipal)) {
      resultados[codigoGrupoPrincipal] += mov.resumo?.nValPago || 0;
    }
  }

  return resultados;
}