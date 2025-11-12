package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.ChatbotService;
import com.wpp.wppbotmanager.service.MessageService;
import com.wpp.wppbotmanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;
import com.wpp.wppbotmanager.dto.SendMessageRequest;
import com.wpp.wppbotmanager.dto.UserDto;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/wpp/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final ChatbotService chatbotService;

    public MessageController(MessageService messageService, ChatbotService chatbotService, UserService userService) {
        this.messageService = messageService;
        this.chatbotService = chatbotService;
        this.userService = userService;
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
        String atividade = request.getStatus();
        String texto = request.getTexto();
        String papel = request.getPapel();

        try {
            List<String> userList = new ArrayList<>();
            userList.add(numUser+","+atividade+","+papel);
            String[] dados = userList.get(0).split(",");

            String telefone = dados[0];
            String atividadeValor = dados[1];
            String papelValor = dados[2];
  
            boolean existe = telefone != null && telefone.endsWith(numUser);

            if (existe) { 
                if ("ATIVO".equalsIgnoreCase(atividadeValor)) { 
                    chatbotService.processMessage(request);
                    return ResponseEntity.ok("Mensagem recebida e usuário ativo"); 
                } else if ("INATIVO".equalsIgnoreCase(atividadeValor)) { 
                    chatbotService.inactiveUser(numUser); 
                    return ResponseEntity.ok("Mensagem recebida e usuário inativo"); 
                } else { 
                    chatbotService.unknownUser(numUser); 
                    return ResponseEntity.ok("Mensagem recebida e status de atividade desconhecido"); 
                } 
            } else { 
                return ResponseEntity.status(404).body("Usuário não encontrado no banco"); 
            }


        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar mensagem: " + e.getMessage());
        }
    }
    
}