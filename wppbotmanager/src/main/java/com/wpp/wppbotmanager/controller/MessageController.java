package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.ChatbotService;
import com.wpp.wppbotmanager.service.MessageService;
import com.wpp.wppbotmanager.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;
import com.wpp.wppbotmanager.dto.UserDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List; 

@RestController
@RequestMapping("/wpp/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final ChatbotService chatbotService;

    public MessageController(MessageService messageService, ChatbotService chatbotService) {
        this.messageService = messageService;
        this.chatbotService = chatbotService;
        this.userService = null;
    }

    @GetMapping
    public String getMessages() {
        return messageService.getMessageApi();
    }

    @PostMapping("/enviar")
    public String SendMessage(@RequestParam String numero, @RequestParam String texto){
        return messageService.sendMessage(numero, texto);
    }
    @PostMapping("/receber/msg")
    public ResponseEntity<?> receiveMessage(@RequestBody ReceiveMessageRequest request) {
        String numUser = request.getFrom();
        String atividade = request.getStatus();
        String texto = request.getTexto();

        
        try{
            String userJson = userService.getUser();
            ObjectMapper mapper = new ObjectMapper();
            List<UserDto> usuarios = mapper.readValue(userJson, new TypeReference<List<UserDto>>() {});
            
            boolean existe = usuarios.stream()
                .filter(u -> u.getTelefone() != null)
                .anyMatch(u -> u.getTelefone().equals(numUser));
            
            
            if (existe) {
                if("ATIVO".equalsIgnoreCase(atividade)) {
                    chatbotService.processMessage(numUser, texto);
                    return ResponseEntity.ok("Mensagem recebida e usuário ativo");
            
                } if("INATIVO".equalsIgnoreCase(atividade)){
                    return ResponseEntity.ok("Mensagem recebida e usuário inativo");
                }
                else{
                    return ResponseEntity.ok("Mensagem recebida e status de atividade desconhecido");
                }
            }
            else {
              return ResponseEntity.status(404).body("Usuário não encontrado no banco ");
            }
        }catch(Exception e){
            return ResponseEntity.status(500).body("Erro ao processar usuários: " + e.getMessage());
        }
    }
}