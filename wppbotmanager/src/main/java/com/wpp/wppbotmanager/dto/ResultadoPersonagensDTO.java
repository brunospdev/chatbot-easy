package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResultadoPersonagensDTO {
    private InfoDTO info;
    private List<PersonagemDTO> results;
}
