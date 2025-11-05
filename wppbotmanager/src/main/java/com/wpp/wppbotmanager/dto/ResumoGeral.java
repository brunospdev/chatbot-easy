package com.wpp.wppbotmanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumoGeral {

    @JsonProperty("periodo_analisado")
    private PeriodoAnalisadoDTO periodo_analisado;

    private String total_receitas;
    private String total_despesas_custos;
    private String resultado_liquido;

    private Double parseCurrency(String value) {
        if (value == null || value.isEmpty()) return 0.0;

        String cleanValue = value
                .replaceAll("[^\\d,-]", "");

        cleanValue = cleanValue.replace(",", ".");

        if (cleanValue.equals("-")) return 0.0;

        return Double.parseDouble(cleanValue);
    }

    public Double getTotal_receitas_numerico() {
        return parseCurrency(this.total_receitas);
    }

    public Double getTotal_despesas_custos_numerico() {
        return parseCurrency(this.total_despesas_custos);
    }

    public Double getResultado_liquido_numerico() {
        return parseCurrency(this.resultado_liquido);
    }
}
