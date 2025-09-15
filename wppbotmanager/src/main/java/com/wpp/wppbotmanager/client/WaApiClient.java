package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WaApiClient {

    private final WebClient webClient;

    public WaApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:3000/wpp")
                .build();
    }

    public String getMessages() {
        return webClient.get()
                .uri("/listar")
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
                .block();
    }
}
