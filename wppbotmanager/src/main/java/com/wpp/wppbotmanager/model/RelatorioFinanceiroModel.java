package com.wpp.wppbotmanager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wpp.wppbotmanager.dto.DetalhesPorCategoria;
import com.wpp.wppbotmanager.dto.ResumoGeral;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelatorioFinanceiroModel {

    @JsonProperty("resumo_geral")
    private ResumoGeral resumoGeral;

    @JsonProperty("detalhes_por_categoria")
    private DetalhesPorCategoria detalhesPorCategoria;

}
