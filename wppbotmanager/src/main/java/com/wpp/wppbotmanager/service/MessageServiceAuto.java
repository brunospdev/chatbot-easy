package com.wpp.wppbotmanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MessageServiceAuto {

    private final MessageService messageService;
    private final ChatbotService chatbotService;
    private final ObjectMapper mapper = new ObjectMapper();

    private final Set<String> mensagensProcessadas = new HashSet<>();

    public MessageServiceAuto(MessageService messageService, ChatbotService chatbotService) {
        this.messageService = messageService;
        this.chatbotService = chatbotService;
    }

    @Scheduled(fixedRate = 1000) // a cada 1 segundo
    public void processarMensagensAutomaticamente() {
        try {
            String json = messageService.getMessageApi();

            List<ReceiveMessageRequest> mensagens = mapper.readValue(json, new TypeReference<>() {});

            for (ReceiveMessageRequest msg : mensagens) {
                if (mensagensProcessadas.contains(msg.getId())) {
                    continue; // ignora mensagens repetidas
                }

                System.out.println("Nova mensagem: " + msg.getTexto() + " de " + msg.getFrom());

                if ("ativo".equalsIgnoreCase(msg.getStatus())) {
                    chatbotService.processMessage(msg);
                } else if ("inativo".equalsIgnoreCase(msg.getStatus())) {
                    chatbotService.inactiveUser(msg.getFrom());
                } else {
                    chatbotService.unknownUser(msg.getFrom());
                }

                mensagensProcessadas.add(msg.getId());
            }

        } catch (Exception e) {
            System.err.println("Erro ao processar mensagens automaticamente: " + e.getMessage());
        }
    }
}
