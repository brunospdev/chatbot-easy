package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.MessageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest; 

@RestController
@RequestMapping("/wpp/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
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
        return ResponseEntity.ok("Mensagem recebida com sucesso");
    }
}
