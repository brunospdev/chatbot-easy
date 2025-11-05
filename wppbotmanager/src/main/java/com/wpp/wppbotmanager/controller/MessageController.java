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
    public ResponseEntity<?> receiveMessage(@RequestBody List<ReceiveMessageRequest> requests) {
    try {
        ObjectMapper mapper = new ObjectMapper();

        // Obtém os usuários do userService (ajuste para lista OU único objeto)
        String userJson = userService.getUser();
        List<UserDto> usuarios;

        try {
            // tenta ler como lista
            usuarios = mapper.readValue(userJson, new TypeReference<List<UserDto>>() {});
        } catch (Exception e) {
            // se não for lista, lê como único objeto
            UserDto usuarioUnico = mapper.readValue(userJson, UserDto.class);
            usuarios = List.of(usuarioUnico);
        }

        // percorre todas as mensagens recebidas
        for (ReceiveMessageRequest request : requests) {
            String numUser = request.getFrom();
            String atividade = request.getStatus();
            String texto = request.getTexto();
            String papel = request.getPapel();

            boolean existe = usuarios.stream()
                .filter(u -> u.getTelefone() != null)
                .anyMatch(u -> u.getTelefone().equals(numUser));

            chatbotService.processMessage(numUser, texto);

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