package com.wpp.wppbotmanager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;
import com.wpp.wppbotmanager.dto.ReceiveReportRequest;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    @Scheduled(fixedRate = 1000)
    public void processarMensagensAutomaticamente() {
        try {
            String json = messageService.getMessageApi();
            
            List<ReceiveMessageRequest> mensagens = mapper.readValue(json, new TypeReference<>() {});

            for (ReceiveMessageRequest msg : mensagens) {
                if (mensagensProcessadas.contains(msg.getId())) {
                    continue;
                }

                String numUser = msg.getFrom();
                System.out.println("[DEBUG] Nova mensagem: " + msg.getTexto() + " de " + numUser);

                try {
                    
                    String userUrl = "http://localhost:3001/users/telefone/" + numUser;
                    RestTemplate restTemplate = new RestTemplate();
                    String userJson = restTemplate.getForObject(userUrl, String.class);
                    
                    System.out.println("[DEBUG] User fetched from Node: " + userJson);
                    
                    if (userJson != null && !userJson.isBlank()) {
                        JsonNode userNode = mapper.readTree(userJson);
                        System.out.println("[DEBUG] Parsed user JSON: " + userNode.toPrettyString());
                        
                        
                        if (userNode.has("message")) {
                            userNode = userNode.get("message");
                        }
                        
                        
                        String idEmpresa = userNode.path("id_empresa").asText(null);
                        String atividade = userNode.path("atividade").asText("ativo").toLowerCase();
                        String papel = userNode.path("papel").asText("user");
                        String nome = userNode.path("nome").asText(null);
                        
                        System.out.println("[DEBUG] *** USER DATA EXTRACTED ***");
                        System.out.println("[DEBUG] id_empresa: " + idEmpresa);
                        System.out.println("[DEBUG] atividade: " + atividade);
                        System.out.println("[DEBUG] papel: " + papel);
                        System.out.println("[DEBUG] nome: " + nome);
                        
                        
                        msg.setId_empresa(idEmpresa);
                        msg.setStatus(atividade);
                        msg.setPapel(papel);
                        msg.setNome(nome);
                        
                        
                        if ("ativo".equalsIgnoreCase(atividade)) {
                            ReceiveReportRequest report = new ReceiveReportRequest();
                            report.setIdEmpresa(idEmpresa);
                            chatbotService.processMessage(msg, report);
                        } else if ("inativo".equalsIgnoreCase(atividade)) {
                            chatbotService.inactiveUser(numUser);
                        } else {
                            chatbotService.unknownUser(numUser);
                        }
                    } else {
                        System.out.println("[DEBUG] User not found in database for number: " + numUser);
                        chatbotService.unknownUser(numUser);
                    }
                    
                } catch (Exception e) {
                    System.out.println("[ERROR] Exception fetching user from Node: " + e.getMessage());
                    e.printStackTrace();
                    chatbotService.unknownUser(numUser);
                }

                mensagensProcessadas.add(msg.getId());
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Erro ao processar mensagens automaticamente: " + e.getMessage());
        }
    }
}
