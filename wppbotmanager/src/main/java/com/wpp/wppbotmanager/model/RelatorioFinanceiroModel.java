package com.wpp.wppbotmanager.model;

import com.wpp.wppbotmanager.dto.DetalhesPorCategoria;
import com.wpp.wppbotmanager.dto.ResumoGeral;

public class RelatorioFinanceiroModel {

    private ResumoGeral resumo_geral;

    private DetalhesPorCategoria detalhes_por_categoria;

    public ResumoGeral getResumo_geral() {
        return resumo_geral;
    }

    public void setResumo_geral(ResumoGeral resumo_geral) {
        this.resumo_geral = resumo_geral;
    }

    public DetalhesPorCategoria getDetalhes_por_categoria() {
        return detalhes_por_categoria;
    }

    public void setDetalhes_por_categoria(DetalhesPorCategoria detalhes_por_categoria) {
        this.detalhes_por_categoria = detalhes_por_categoria;
    }
}
