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
    public String SendMessage(@RequestBody SendMessageRequest request){
        return messageService.sendMessage(request.getNumero(), request.getTexto());
    }
    @PostMapping("/receber/msg")
    public ResponseEntity<?> receiveMessage(@RequestBody ReceiveMessageRequest request) {
        String numUser = request.getFrom().replaceAll("\\D", "");
        String atividade = request.getStatus();
        String texto = request.getTexto();
        String papel = request.getPapel();

        
        try{
            String userJson = userService.getUser();
            ObjectMapper mapper = new ObjectMapper();
            List<UserDto> usuarios = mapper.readValue(userJson, new TypeReference<List<UserDto>>() {});
            
            boolean existe = usuarios.stream().map(u -> u.getTelefone() == null ? "" : u.getTelefone().replaceAll("\\D", "")).anyMatch(tel -> tel.endsWith(numUser));

            if (existe) {
                if ("ATIVO".equalsIgnoreCase(atividade)) {
                    return ResponseEntity.ok("Mensagem recebida e usuário ativo");
                } else if ("INATIVO".equalsIgnoreCase(atividade)) {
                    chatbotService.inactiveUser(numUser);
                    return ResponseEntity.ok("Mensagem recebida e usuário inativo");
                } else {
                    chatbotService.unknownUser(numUser);
                    return ResponseEntity.ok("Mensagem recebida e status de atividade desconhecido");
                }
            } else {
                return ResponseEntity.status(404).body("Usuário não encontrado no banco");
            }
        }

        return ResponseEntity.ok("Mensagens processadas com sucesso");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Erro ao processar usuários: " + e.getMessage());
    }
}

}