package com.wpp.wppbotmanager.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveMessageRequest {    
    private String from;
    private String nome;
    private String texto;
    private String data;
    private String hora;
}


