package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RelatorioDetalhadoDTO {
    private String codigo_filtrado;
    private Periodo periodo_analisado;
    private String valor_total;
    private int numero_de_lancamentos;
    private List<Lancamento> lancamentos;
}

