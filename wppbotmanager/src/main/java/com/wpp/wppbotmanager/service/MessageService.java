package com.wpp.wppbotmanager.service;

import com.wpp.wppbotmanager.client.WaApiClient;
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
}
