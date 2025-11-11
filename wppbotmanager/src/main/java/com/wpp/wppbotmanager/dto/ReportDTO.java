package com.wpp.wppbotmanager.dto;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class ReportDTO {


    public byte[] generatePdfReport(List<?> dataList, Map<String, Object> parameters)
            throws JRException, IOException {

        InputStream templateStream = getClass().getResourceAsStream("/reports/seu_relatorio.jasper");
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(templateStream);

        JRDataSource dataSource = new JRBeanCollectionDataSource(dataList);

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                dataSource
        );

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }
}

