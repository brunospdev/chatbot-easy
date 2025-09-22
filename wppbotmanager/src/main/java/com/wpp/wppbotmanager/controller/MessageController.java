package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.MessageService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manager/messages")
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
}
