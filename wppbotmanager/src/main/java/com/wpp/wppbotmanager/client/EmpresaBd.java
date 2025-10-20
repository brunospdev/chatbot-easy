package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.wpp.wppbotmanager.dto.EmpresaDto;

@Component
public class EmpresaBd {

  private final WebClient empresaTb;

  public EmpresaBd(WebClient.Builder builder) {
    this.empresaTb = builder
        .baseUrl("http://localhost:3001/empresa")
        .build();
  }

  public String getEmpresa() {
    return empresaTb.get()
      .uri("/listarEmpresa")
      .retrieve()
      .bodyToMono(String.class)
      .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
      .block();
  }

  public String postEmpresa(EmpresaDto request) {
    return empresaTb.post()
    .uri("/criarEmpresa")
    .header("Content-Type", "application/json")
    .bodyValue(request)
    .retrieve()
    .bodyToMono(String.class)
    .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
    .block();
  }
}
