package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumoGeral {

    private String data_inicio;
    private String data_fim;
    private Integer total_dias;
    private String total_receitas;
    private String total_despesas_custos;
    private String resultado_liquido;
}
