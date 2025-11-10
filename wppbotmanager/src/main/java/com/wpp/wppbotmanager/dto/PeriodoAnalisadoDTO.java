package com.wpp.wppbotmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PeriodoAnalisadoDTO {

    @JsonProperty("data_inicio")
    private String data_inicio;

    @JsonProperty("data_fim")
    private String data_fim;

    @JsonProperty("total_dias")
    private Integer total_dias;

}
