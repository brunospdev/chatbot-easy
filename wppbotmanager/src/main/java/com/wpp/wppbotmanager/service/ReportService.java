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

    public byte[] generatePdfReport(RelatorioFinanceiroModel reportData, Map<String, Object> parameters)
            throws JRException {

        List<RelatorioFinanceiroModel> dataList = Collections.singletonList(reportData);

        InputStream templateStream = getClass().getResourceAsStream("/reports/relatorio_financeiro.jasper");

        if (templateStream == null) {
            throw new RuntimeException("Modelo .jasper não encontrado em /reports/");
        }

        JasperReport jasperReport;
        try {
            jasperReport = (JasperReport) JRLoader.loadObject(templateStream);
        } catch (Exception e) {
            throw new JRException("Erro ao carregar o modelo JasperReport: " + e.getMessage(), e);
        }

        JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}