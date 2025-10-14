package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigErpDto {

  private Integer id_config;
  private String url_api;
  private String token_api;
  private Integer status;
  private Integer id_empresa;

  public ConfigErpDto() {}

  public ConfigErpDto(Integer id_config, String url_api, String token_api, Integer status, Integer id_empresa) {
    this.id_config = id_config;
    this.url_api = url_api;
    this.token_api = token_api;
    this.status = status;
    this.id_empresa = id_empresa;
  }
}
