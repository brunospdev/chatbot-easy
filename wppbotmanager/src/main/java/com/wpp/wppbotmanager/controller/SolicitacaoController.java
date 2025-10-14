package com.wpp.wppbotmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpp.wppbotmanager.service.SolicitacaoService;

@RestController
@RequestMapping("/Solicitacao")
public class SolicitacaoController {

  private final SolicitacaoService solicitacaoService;

  public SolicitacaoController(SolicitacaoService solicitacaoService) {
    this.solicitacaoService = solicitacaoService;
  }

    @GetMapping("/listarsolic")
  public String getSolicitacao() {
    return solicitacaoService.getSolicitacao();
  }

    @PostMapping("/criarsolic")
    public String postSolicitacao(@RequestBody Integer tipo_solicitacao, @RequestBody Integer status, @RequestBody Integer id_usuario){
        return solicitacaoService.postSolicitacao(tipo_solicitacao, status, id_usuario);
    }
}
