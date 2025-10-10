package com.wpp.wppbotmanager.service;

import com.wpp.wppbotmanager.dto.PersonagemDTO;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class RelatorioService {
    private final RickAndMortyService apiService;
    private final PDFGeneratorService pdfService;

    // ... Construtor ...

    public void gerarRelatorioPersonagens(HttpServletResponse response) throws IOException {

        // 1. CHAMA O SERVIÇO EXTERNO (busca os dados)
        List<PersonagemDTO> dadosPersonagens = apiService.buscarPersonagensIniciais();

        // 2. CHAMA O SERVIÇO DE GERAÇÃO DE PDF (passa os dados)
        pdfService.export(response, dadosPersonagens);
    }
}
