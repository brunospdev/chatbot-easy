package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/manager/messages")
    public String getMessages() {
        return messageService.getMessageApi();
    }
}
