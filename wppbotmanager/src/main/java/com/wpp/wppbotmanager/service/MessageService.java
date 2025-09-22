package com.wpp.wppbotmanager.service;

import com.wpp.wppbotmanager.client.WaApiClient;
import com.wpp.wppbotmanager.dto.SendMessageRequest;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final WaApiClient apiClient;

    public MessageService(WaApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public String getMessageApi() {
        return apiClient.getMessages();
    }

    public String sendMessage(String numero, String texto) {
        try {
            SendMessageRequest request = new SendMessageRequest(numero, texto);
            return apiClient.sendMessage(request);
        } catch (Exception e) {
            return "erro ao enviar mensagem: " + e.getMessage();
        }
    }
}
