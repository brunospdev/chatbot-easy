package com.wpp.wppbotmanager.service;


import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.wpp.wppbotmanager.model.RelatorioFinanceiroModel;

@Service
public class ReportService {

    /**
     * Gera o relatório PDF a partir do modelo compilado e dos dados.
     * * @param reportData Objeto RelatorioFinanceiroModel com os dados do JSON.
     * @param parameters Parâmetros adicionais para o relatório.
     * @return O conteúdo do PDF em bytes.
     */
    public byte[] generatePdfReport(RelatorioFinanceiroModel reportData, Map<String, Object> parameters)
            throws JRException {

        // Simplesmente para o JasperReports funcionar com JRBeanCollectionDataSource,
        // passamos o objeto único dentro de uma lista.
        List<RelatorioFinanceiroModel> dataList = Collections.singletonList(reportData);

        // O Spring Boot carrega o arquivo .jasper do diretório 'resources'
        // Assumindo que você colocou o arquivo em src/main/resources/reports/
        InputStream templateStream = getClass().getResourceAsStream("/reports/relatorio_financeiro.jasper");

        if (templateStream == null) {
            throw new RuntimeException("Modelo .jasper não encontrado em /reports/");
        }

        // 1. Carregar e Compilar o Modelo
        JasperReport jasperReport;
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(templateStream);
        } catch (Exception e) {
            throw new JRException("Erro ao carregar o modelo JasperReport: " + e.getMessage(), e);
        }

        // 2. Preencher com os Dados
        // JRBeanCollectionDataSource usa os getters dos objetos na lista.
        JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);

        // 3. Gerar o Objeto de Impressão (o relatório preenchido)
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
        );

        // 4. Exportar para PDF
        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}