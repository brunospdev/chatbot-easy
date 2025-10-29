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

    // Injeção de dependência via construtor (melhor prática)
    public PDFGenerationService(WebClient webClient) {
        this.webClient = webClient;
    }

    public byte[] gerarRelatorioFinanceiroPdf(String appKey, String appSecret) throws JRException {
        // 1. Chamar a API Node.js para obter os dados
        RelatorioFinanceiroModel dadosFinanceiros = buscarDadosFinanceiros(appKey, appSecret);

        // 2. Carregar o arquivo .jasper compilado
        InputStream jasperStream = this.getClass().getResourceAsStream("/reports/relatorioestruturado.jasper");

        // 3. Criar os parâmetros e a fonte de dados para o Jasper
        Map<String, Object> parameters = new HashMap<>();
        // O Jasper funciona melhor passando objetos complexos como parâmetros
        parameters.put("resumoGeral", dadosFinanceiros.getResumo_geral());
        parameters.put("detalhesPorCategoria", dadosFinanceiros.getDetalhes_por_categoria());

        // Para o Jasper, mesmo que seja um único objeto, ele espera uma coleção.
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(Collections.singletonList(dadosFinanceiros));

        // 4. Preencher o relatório
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperStream, parameters, dataSource);

        // 5. Exportar para PDF (retorna um array de bytes)
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    private RelatorioFinanceiroModel buscarDadosFinanceiros(String appKey, String appSecret) {
        OmieDTO.OmieApiRequest requestBody = new OmieDTO.OmieApiRequest(appKey, appSecret);

        // Faz a chamada POST e converte a resposta JSON diretamente para o seu modelo Java
        return webClient.post()
                .uri("/omie/relatorio-financeiro") // A rota que descobrimos
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(RelatorioFinanceiroModel.class)
                .block(); // .block() torna a chamada síncrona para este exemplo
    }
}
