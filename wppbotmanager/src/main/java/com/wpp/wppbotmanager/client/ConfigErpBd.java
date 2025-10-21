package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


import com.wpp.wppbotmanager.dto.ConfigErpDto;

@Component
public class ConfigErpBd {

  private WebClient configErpTb;

  public ConfigErpBd(WebClient.Builder builder) {
    this.configErpTb = builder
      .baseUrl("http://localhost:3001/ConfiguracaoERP")
      .build();
  }

  public String getConfigErp() {
    return configErpTb.get()
      .uri("/lconfigerp")
      .retrieve()
      .bodyToMono(String.class)
      .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
      .block();
  }

  public String postConfigErp(ConfigErpDto request) {
    return configErpTb.post()
    .uri("/cconfigerp")
    .header("Content-Type", "application/json")
    .bodyValue(request)
    .retrieve()
    .bodyToMono(String.class)
    .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
    .block();
  }
}
