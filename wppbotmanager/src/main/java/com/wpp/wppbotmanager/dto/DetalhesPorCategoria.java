package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetalhesPorCategoria {

    private String receitas_operacionais;
    private String custos_variaveis;
    private String despesas_com_pessoal;
    private String despesas_administrativas;
    private String pro_labore;
    private String investimentos;
    private String parcelamentos;
    private String entradas_nao_operacionais;
    private String saidas_nao_operacionais;

    // Método de parsing de moeda (copiado do ResumoGeral para consistência)
    private Double parseCurrency(String value) {
        if (value == null || value.isEmpty()) return 0.0;

        String cleanValue = value
                .replaceAll("[^\\d,-]", ""); // Remove tudo que não for dígito, vírgula ou sinal de menos

        cleanValue = cleanValue.replace(",", ".");

        if (cleanValue.equals("-")) return 0.0;

        return Double.parseDouble(cleanValue);
    }

    // Métodos numéricos para os campos
    public Double getReceitas_operacionais_numerico() {
        return parseCurrency(this.receitas_operacionais);
    }

    public Double getCustos_variaveis_numerico() {
        return parseCurrency(this.custos_variaveis);
    }

    public Double getDespesas_com_pessoal_numerico() {
        return parseCurrency(this.despesas_com_pessoal);
    }

    public Double getDespesas_administrativas_numerico() {
        return parseCurrency(this.despesas_administrativas);
    }

    public Double getPro_labore_numerico() {
        return parseCurrency(this.pro_labore);
    }

    public Double getInvestimentos_numerico() {
        return parseCurrency(this.investimentos);
    }

    public Double getParcelamentos_numerico() {
        return parseCurrency(this.parcelamentos);
    }

    public Double getEntradas_nao_operacionais_numerico() {
        return parseCurrency(this.entradas_nao_operacionais);
    }

    public Double getSaidas_nao_operacionais_numerico() {
        return parseCurrency(this.saidas_nao_operacionais);
    }
}
