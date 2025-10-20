package com.wpp.wppbotmanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.wpp.wppbotmanager.dto.Lancamento;
import com.wpp.wppbotmanager.dto.RelatorioDetalhadoDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PDFGeneratorService {

    public void export(HttpServletResponse response) throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/detalhado.json");
        if (inputStream == null) {
            throw new FileNotFoundException("Arquivo detalhado.json não encontrado no classpath!");
        }
        ObjectMapper mapper = new ObjectMapper();
        RelatorioDetalhadoDTO relatorio = mapper.readValue(inputStream, RelatorioDetalhadoDTO.class);

        Document documento = new Document();
        PdfWriter.getInstance(documento, response.getOutputStream());
        documento.open();
        try {
            Image img = Image.getInstance(getClass().getResource("/logo.png"));
            img.setAlignment(Image.ALIGN_CENTER);
            img.scaleToFit(100, 100);
            documento.add(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        NumberFormat formatadorMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        PdfPTable tabela = new PdfPTable(3);
        tabela.addCell("Data");
        tabela.addCell("Descrição");
        tabela.addCell("Valor");
        for (Lancamento l : relatorio.getLancamentos()) {
            tabela.addCell(l.getData());
            tabela.addCell(l.getDescricao());
            String valorFormatado = formatadorMoeda.format(l.getValor());
            tabela.addCell(valorFormatado);
        }
        documento.add(tabela);


        documento.close();
    }
}