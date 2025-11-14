package com.wpp.wppbotmanager.controller;

import org.springframework.web.bind.annotation.*;
import com.wpp.wppbotmanager.dto.ReceiveReportRequest;
import com.wpp.wppbotmanager.service.OmieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/receber/relatorios")
public class ReportController {

    @Autowired
    private OmieService omieService;

    @PostMapping
    public ResponseEntity<String> gerarRelatorio(@RequestParam int dias, @RequestBody ReceiveReportRequest request) {
        try {
            omieService.gerarRelatorioFinanceiroGeral(dias, request);
            return ResponseEntity.ok("Relatório solicitado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
