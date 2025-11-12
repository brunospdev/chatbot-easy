package com.wpp.wppbotmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.wpp.wppbotmanager.dto.ReceiveReportRequest;

import com.wpp.wppbotmanager.dto.ReceiveReportRequest;


@RestController
@RequestMapping("/omie")
public class ReportController {
    @PostMapping("/receber/relatorios")
    public ResponseEntity<?> receiveReport(@RequestBody ReceiveReportRequest request) {
        String appKey = request.getApp_key();
        String appSecret = request.getApp_secret();
        String idEmpresa = request.getId_empresa();

        

        return ResponseEntity.ok("Relatório recebido com sucesso");
    }
}
