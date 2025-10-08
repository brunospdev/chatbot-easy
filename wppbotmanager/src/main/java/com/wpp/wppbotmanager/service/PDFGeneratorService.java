package com.wpp.wppbotmanager.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PDFGeneratorService {

    public void export(HttpServletResponse response) throws IOException {
        Document documento = new Document();

        PdfWriter.getInstance(documento, response.getOutputStream());

        documento.open();
        Font tituloFonte = FontFactory.getFont(FontFactory.TIMES_ROMAN);
        tituloFonte.setSize(24);

        Font conteudo = FontFactory.getFont(FontFactory.HELVETICA);

        Paragraph titulo = new Paragraph("RELATÓRIO", tituloFonte);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);

        PdfPTable tabela = new PdfPTable(5);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Lançamentos:", conteudo));
        cell.setRowspan(5);
        tabela.addCell(cell);
        tabela.addCell("R$ 0,00");
        tabela.addCell("R$ 0,00");
        tabela.addCell("R$ 0,00");
        tabela.addCell("R$ 0,00");

        documento.add(titulo);
        documento.add(tabela);
        documento.close();
    }
}
