package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.wpp.wppbotmanager.dto.AdministradorDto;

@Component
public class AdministradorBd {

  private final WebClient adminTb;

  public AdministradorBd(WebClient.Builder builder) {
    this.adminTb = builder
      .baseUrl("http://localhost:3001/Administrador")
      .build();
  }

  public String getAdmin() {
    return adminTb.get()
      .uri("/ladm")
      .retrieve()
      .bodyToMono(String.class)
      .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
      .block();
  }

  public String postAdmin(AdministradorDto request) {
    return adminTb.post()
    .uri("/cadm")
    .header("Content-Type", "application/json")
    .bodyValue(request)
    .retrieve()
    .bodyToMono(String.class)
    .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
    .block();
  }

}
