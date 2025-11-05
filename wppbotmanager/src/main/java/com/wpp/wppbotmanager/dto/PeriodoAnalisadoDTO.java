package com.wpp.wppbotmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodoAnalisadoDTO {

    @JsonProperty("dataInicio")
    private String data_inicio;

    @JsonProperty("dataFim")
    private String data_fim;

    @JsonProperty("dataDias")
    private Integer total_dias;

}
