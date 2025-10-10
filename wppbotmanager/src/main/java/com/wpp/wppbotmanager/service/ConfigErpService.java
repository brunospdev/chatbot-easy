package com.wpp.wppbotmanager.service;

import org.springframework.stereotype.Service;

import com.wpp.wppbotmanager.client.ConfigErpBd;
import com.wpp.wppbotmanager.dto.ConfigErpDto;

@Service
public class ConfigErpService {

  private final ConfigErpBd configErpBd;

  public ConfigErpService(ConfigErpBd configErpBd) {
    this.configErpBd = configErpBd;
  }

  public String getConfigErp() {
    return configErpBd.getConfigErp();
  }

  public String postConfigErp(String url_api, String token_api, Integer status, Integer id_empresa) {
    try {
      ConfigErpDto request = new ConfigErpDto(null, url_api, token_api, status, id_empresa);
        return configErpBd.postConfigErp(request);
    } catch(Exception e) {
      return "erro ao criar Configração: " + e.getMessage();
    }
  }
}
