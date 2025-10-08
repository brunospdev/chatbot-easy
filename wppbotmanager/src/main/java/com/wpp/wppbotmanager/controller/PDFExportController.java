package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.PDFGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class PDFExportController {

    private final PDFGeneratorService pdfGeneratorService;

    public PDFExportController(PDFGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @GetMapping("/pdf/gerador")
    public void geradorPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        DateFormat formatoData = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dataAtual = formatoData.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_" + dataAtual + ".pdf";
        response.setHeader(headerKey, headerValue);

        this.pdfGeneratorService.export(response);
    }
}
