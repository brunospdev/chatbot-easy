package com.wpp.wppbotmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        // A URL base da sua API Node.js
        return builder.baseUrl("http://localhost:3001").build();
    }
}