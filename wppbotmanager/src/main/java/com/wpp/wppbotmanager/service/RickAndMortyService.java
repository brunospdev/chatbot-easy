package com.wpp.wppbotmanager.service;

import com.wpp.wppbotmanager.dto.PersonagemDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class RickAndMortyService {

    private final WebClient webClient;
    private static final String BASE_URL = "https://rickandmortyapi.com/api";

    public RickAndMortyService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }


    public PersonagemDTO buscarPersonagemPorId(Long id) {
        return this.webClient.get()
                .uri("/character/{id}", id)
                .retrieve()
                .bodyToMono(PersonagemDTO.class)
                .block();
    }
}
