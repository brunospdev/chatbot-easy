package com.wpp.wppbotmanager.dto;

import lombok.Getter;

@Getter
public class SendMessageRequest {
    private String numero;
    private String texto;

    public SendMessageRequest() {}

    public SendMessageRequest(String numero, String texto) {
        this.numero = numero;
        this.texto = texto;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
