package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.ChatbotService;
import com.wpp.wppbotmanager.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;
import com.wpp.wppbotmanager.dto.SendMessageRequest;
import com.wpp.wppbotmanager.dto.ReceiveReportRequest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/wpp/messages")
public class MessageController {

    private final MessageService messageService;
    private final ChatbotService chatbotService;

    public MessageController(MessageService messageService, ChatbotService chatbotService) {
        this.messageService = messageService;
        this.chatbotService = chatbotService;
    }

    @GetMapping
    public String getMessages() {
        return messageService.getMessageApi();
    }

    @PostMapping("/enviar")
    public String SendMessage(@RequestBody SendMessageRequest request) {
        return messageService.sendMessage(request.getNumero(), request.getTexto());
    }

    @PostMapping("/receber/msg")
    public ResponseEntity<?> receiveMessage(@RequestBody ReceiveMessageRequest request) {
        String numUser = request.getFrom();
        
        try {
            System.out.println("[DEBUG] Incoming message from: " + numUser);
            String userUrl = "http://localhost:3001/users/telefone/" + numUser;
            RestTemplate restTemplate = new RestTemplate();
            String userJson = null;
            
            try {
                userJson = restTemplate.getForObject(userUrl, String.class);
                System.out.println("[DEBUG] User fetched from Node: " + userJson);
            } catch (Exception e) {
                System.out.println("[DEBUG] Error fetching user from Node: " + e.getMessage());
                userJson = null;
            }
            if (userJson != null && !userJson.isBlank()) {
                System.out.println("[DEBUG] Raw user JSON from Node: " + userJson);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode userNode = mapper.readTree(userJson);
                
                System.out.println("[DEBUG] Parsed JSON node: " + userNode.toPrettyString());

                if (userNode.has("message")) {
                    System.out.println("[DEBUG] Found 'message' envelope, unwrapping...");
                    userNode = userNode.get("message");
                }
                
                String idEmpresa = userNode.path("id_empresa").asText(null);
                JsonNode atividadeNode = userNode.path("atividade");
                String atividade = "ativo";  // default
                if (!atividadeNode.isMissingNode()) {
                    if (atividadeNode.isNumber()) {
                        int atividadeNum = atividadeNode.asInt(-1);
                        atividade = (atividadeNum == 1 || atividadeNum == 0) ? 
                            (atividadeNum == 1 ? "ativo" : "inativo") : "ativo";
                    } else {
                        atividade = atividadeNode.asText("ativo").toLowerCase();
                    }
                }
                String papel = userNode.path("papel").asText("user");
                String nome = userNode.path("nome").asText(null);
                
                System.out.println("[DEBUG] *** USER DATA EXTRACTED ***");
                System.out.println("[DEBUG] id_empresa: " + idEmpresa);
                System.out.println("[DEBUG] atividade (raw): " + atividadeNode);
                System.out.println("[DEBUG] atividade (parsed): " + atividade);
                System.out.println("[DEBUG] papel: " + papel);
                System.out.println("[DEBUG] nome: " + nome);

                request.setId_empresa(idEmpresa);
                request.setStatus(atividade);
                request.setPapel(papel);
                request.setNome(nome);

                if ("ativo".equalsIgnoreCase(atividade)) {
                    ReceiveReportRequest reportRequest = new ReceiveReportRequest();
                    reportRequest.setIdEmpresa(idEmpresa);
                    chatbotService.processMessage(request, reportRequest);
                    return ResponseEntity.ok("Mensagem recebida e usuário ativo");
                } else if ("inativo".equalsIgnoreCase(atividade)) {
                    chatbotService.inactiveUser(numUser);
                    return ResponseEntity.ok("Mensagem recebida e usuário inativo");
                } else {
                    return ResponseEntity.ok("Mensagem recebida e status de atividade desconhecido");
                }
            } else {
                System.out.println("[DEBUG] User not found in database for number: " + numUser);
                chatbotService.unknownUser(numUser);
                return ResponseEntity.ok("Usuário não encontrado no banco");
            }

        } catch (Exception e) {
            System.err.println("[ERROR] Exception in receiveMessage: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao processar mensagem: " + e.getMessage());
        }
    }
    
}