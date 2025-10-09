package com.wpp.wppbotmanager.client;

import com.wpp.wppbotmanager.dto.SendMessageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WaApiClient {

    private final WebClient webClient;

    public WaApiClient(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:3001/wpp")
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

    public String sendMessage(SendMessageRequest request) {
        return webClient.post()
                .uri("/enviar")
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(e -> System.err.println("Erro ao enviar a mensagem: " + e.getMessage()))
                .block();
    }
}
