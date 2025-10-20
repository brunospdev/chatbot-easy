package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmpresaDto {

  private Integer id_empresa;
  private String nome_empresa;
  private String cnpj;
  private String token_api;

  public EmpresaDto() {}

  public EmpresaDto(Integer id_empresa, String nome_empresa, String cnpj, String token_api) {
    this.id_empresa = id_empresa;
    this.nome_empresa = nome_empresa;
    this.cnpj = cnpj;
    this.token_api = token_api;
  }
}