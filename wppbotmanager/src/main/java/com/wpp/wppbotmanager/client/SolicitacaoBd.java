package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import com.wpp.wppbotmanager.dto.SolicitacaoDto;

@Component
public class SolicitacaoBd {

  private final WebClient solicitacaoTb;

  public SolicitacaoBd(WebClient.Builder builder) {
    this.solicitacaoTb = builder
      .baseUrl("http://localhost:3001/Solicitacao")
      .build();
  }

  public String getSolicitacao() {
    return solicitacaoTb.get()
      .uri("/listarsolic")
      .retrieve()
      .bodyToMono(String.class)
      .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
      .block();
  }

  public String postSolicitacao(SolicitacaoDto request) {
    return solicitacaoTb.post()
    .uri("/criarsolic")
    .header("Content-Type", "application/json")
    .bodyValue(request)
    .retrieve()
    .bodyToMono(String.class)
    .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
    .block();
  }
}