package com.wpp.wppbotmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wpp.wppbotmanager.dto.enums.atividade.Atividade;
import com.wpp.wppbotmanager.dto.enums.papel.Papel;
import com.wpp.wppbotmanager.dto.enums.primeiro_contato.Primeiro_contato;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

  private Integer id_user;
  private String nome;
  private String telefone;
  private Papel papel;
  public  Atividade atividade;
  private Integer id_empresa;
  public Primeiro_contato primeiro_contato;

  public UserDto() {}

  public UserDto(Integer id_user, String nome, String telefone, Papel papel, Atividade atividade, Integer id_empresa) {
    this.id_user = id_user;
    this.nome = nome;
    this.telefone = telefone;
    this.atividade = atividade;
    this.papel = papel;
    this.id_empresa = id_empresa;
    this.primeiro_contato = primeiro_contato;
  }
}