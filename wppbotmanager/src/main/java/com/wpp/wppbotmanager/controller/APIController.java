package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.dto.PersonagemDTO;
import com.wpp.wppbotmanager.service.RickAndMortyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teste/rickandmorty")
public class APIController {
    private final RickAndMortyService service;

    public APIController(RickAndMortyService service) {
        this.service = service;
    }

    @GetMapping("/personagem/{id}")
    public PersonagemDTO getPersonagem(@PathVariable Long id) {
        // O Controller chama o Service, que chama a API externa
        return service.buscarPersonagemPorId(id);
    }
}
