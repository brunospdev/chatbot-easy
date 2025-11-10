package com.wpp.wppbotmanager.service;

import com.wpp.wppbotmanager.dto.OmieDTO;
import com.wpp.wppbotmanager.model.RelatorioFinanceiroModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class PDFGenerationService {

    private final WebClient webClient;

    public PDFGenerationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public byte[] gerarRelatorioFinanceiroPdf(String appKey, String appSecret) throws JRException {
        RelatorioFinanceiroModel dadosFinanceiros = buscarDadosFinanceiros(appKey, appSecret);

        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/relatorioestruturado.jasper");
        InputStream imageStream = getClass().getResourceAsStream("/images/logo.png");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("resumoGeral", dadosFinanceiros.getResumoGeral());
        parameters.put("detalhesPorCategoria", dadosFinanceiros.getDetalhesPorCategoria());
        parameters.put("Logo_Imagem", imageStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(dadosFinanceiros));

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private RelatorioFinanceiroModel buscarDadosFinanceiros(String appKey, String appSecret) {
        OmieDTO.OmieApiRequest requestBody = new OmieDTO.OmieApiRequest(appKey, appSecret);

        return webClient.post()
                .uri("/omie/relatorio-financeiro")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(RelatorioFinanceiroModel.class)
                .block();
    }
}
