package com.wpp.wppbotmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpp.wppbotmanager.model.RelatorioFinanceiroModel;
import com.wpp.wppbotmanager.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PDFExportController {

    @Autowired
    private ReportService reportService;

    // Objeto Jackson para converter o JSON para as classes Java
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/api/reports/financeiro")
    public ResponseEntity<byte[]> getFinancialReportPdf() {
        try {
            // 1. Simulação da Leitura/Obtenção dos Dados JSON
            String jsonContent = getJsonSimulado();

            // 2. Desserialização do JSON para o Modelo Java
            RelatorioFinanceiroModel reportData = objectMapper.readValue(jsonContent, RelatorioFinanceiroModel.class);

            // 3. Preparação dos Parâmetros (Ex: Logo, Subtítulo, etc.)
            Map<String, Object> params = new HashMap<>();
            params.put("REPORT_TITLE", "Relatório de Análise Financeira");

            // 4. Chamada ao Serviço para Geração do PDF
            byte[] pdfBytes = reportService.generatePdfReport(reportData, params);

            // 5. Retorno da Resposta HTTP (PDF)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "relatorio_financeiro.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (JRException | IOException e) {
            e.printStackTrace();
            // Retorna um erro 500 em caso de falha na geração ou I/O.
            return ResponseEntity.internalServerError().body(("Erro ao gerar o relatório: " + e.getMessage()).getBytes());
        }
    }

    /**
     * Simula a obtenção do conteúdo JSON fornecido.
     */
    private String getJsonSimulado() {
        return "{\"resumo_geral\":{\"periodo_analisado\":{\"data_inicio\":\"2020-10-29\",\"data_fim\":\"2025-10-28\",\"total_dias\":1825},\"total_receitas\":\"R$ 194.291,85\",\"total_despesas_custos\":\"R$ 203.480,59\",\"resultado_liquido\":\"-R$ 9.188,74\"},\"detalhes_por_categoria\":{\"receitas_operacionais\":\"R$ 189.976,11\",\"custos_variaveis\":\"R$ 76.038,61\",\"despesas_com_pessoal\":\"R$ 0,00\",\"despesas_administrativas\":\"R$ 51.114,66\",\"pro_labore\":\"R$ 43.070,64\",\"investimentos\":\"R$ 14.877,34\",\"parcelamentos\":\"R$ 0,00\",\"entradas_nao_operacionais\":\"R$ 4.315,74\",\"saidas_nao_operacionais\":\"R$ 33.256,68\"}}";
    }
}