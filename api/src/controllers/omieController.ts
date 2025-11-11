import { Request, Response } from "express";
import * as omieService from "../services/omieService";

const formatadorReais = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

const MAPA_CHAVES_SIMPLES: Record<string, string> = {
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

const MAPA_DESCRICOES_RESPOSTA: Record<string, string> = {
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

const ehReceita = (codigo: string): boolean => codigo.startsWith("1.") || codigo.startsWith("6.");
const ehDespesaOuCusto = (codigo: string): boolean =>
    codigo.startsWith("2.") || codigo.startsWith("3.") || codigo.startsWith("5.") || codigo.startsWith("7.");

export async function gerarRelatorioFinanceiroGeralController(req: Request, res: Response) {
    try {
        const dias = parseInt(req.query.dias as string) || 1825;
        const { appKey, appSecret } = req.body;

        if (!appKey || !appSecret) {
            return res.status(400).json({ error: 'Os campos appKey e appSecret são obrigatórios no corpo da requisição.' });
        }

        const resultadosNumericos = await omieService.gerarRelatorioFinanceiroGeral(appKey, appSecret, dias);

        let totalReceitas = 0;
        let totalDespesasCustos = 0;

        const detalhesFormatados: Record<string, string> = {};
        for (const codigo in resultadosNumericos) {
            if (resultadosNumericos.hasOwnProperty(codigo)) {
                const valor = resultadosNumericos[codigo];
                const chaveSimples = MAPA_CHAVES_SIMPLES[codigo] || codigo;
                const valorFormatado = formatadorReais.format(valor);

                detalhesFormatados[chaveSimples] = valorFormatado.replace(/\u00A0/g, ' ');

                if (ehReceita(codigo)) {
                    totalReceitas += valor;
                } else if (ehDespesaOuCusto(codigo)) {
                    totalDespesasCustos += valor;
                }
            }
        }

        const resultadoLiquido = totalReceitas - totalDespesasCustos;

        const dataFim = new Date();
        const dataInicio = new Date();
        dataInicio.setDate(dataFim.getDate() - dias);

        const respostaFinal = {
            resumo_geral: {
                periodo_analisado: {
                    data_inicio: dataInicio.toISOString().split('T')[0],
                    data_fim: dataFim.toISOString().split('T')[0],
                    total_dias: dias
                },
                total_receitas: formatadorReais.format(totalReceitas).replace(/\u00A0/g, ' '),
                total_despesas_custos: formatadorReais.format(totalDespesasCustos).replace(/\u00A0/g, ' '),
                resultado_liquido: formatadorReais.format(resultadoLiquido).replace(/\u00A0/g, ' ')
            },
            detalhes_por_categoria: detalhesFormatados
        };

        return res.json(respostaFinal);

    } catch (err: any) {
        console.error("ERRO AO GERAR RELATÓRIO GERAL:", err.response?.data || err.message || err);
        return res.status(500).json({
            error: 'Falha ao processar a solicitação.',
            details: err.response?.data?.faultstring || err.response?.data?.error || err.message || 'Erro desconhecido'
        });
    }
}