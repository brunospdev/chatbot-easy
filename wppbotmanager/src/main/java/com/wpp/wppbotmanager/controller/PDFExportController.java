package com.wpp.wppbotmanager.controller;// No seu com.wpp.wppbotmanager.controller.PDFExportController.java
import com.wpp.wppbotmanager.dto.OmieDTO;
import com.wpp.wppbotmanager.service.PDFGenerationService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PDFExportController {

    @Autowired
    private PDFGenerationService pdfService;

    // Vamos usar um POST para receber as credenciais de forma segura
    @PostMapping("/export/relatorio-financeiro")
    public ResponseEntity<byte[]> exportPdf(@RequestBody OmieDTO.OmieApiRequest credenciais) {
        try {
            // Chama o serviço para gerar o PDF
            byte[] pdfBytes = pdfService.gerarRelatorioFinanceiroPdf(credenciais.getAppKey(), credenciais.getAppSecret());

            // Configura os headers da resposta para indicar que é um PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            // A linha abaixo faz o navegador tentar abrir o PDF inline.
            headers.setContentDispositionFormData("inline", "relatorio_financeiro.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (JRException e) {
            // Lidar com erros de geração do Jasper
            e.printStackTrace(); // Logar o erro
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            // Lidar com outros erros (ex: API Node.js fora do ar)
            e.printStackTrace(); // Logar o erro
            return ResponseEntity.status(502).body(null); // Bad Gateway
        }
    }
}