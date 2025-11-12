package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceiveReportRequest {
    public String app_key;
    public String app_secret;
    public String id_empresa; 
}
