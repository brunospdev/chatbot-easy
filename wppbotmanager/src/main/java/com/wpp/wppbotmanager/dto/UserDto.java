package com.wpp.wppbotmanager.dto;

import com.wpp.wppbotmanager.dto.enums.atividade.Atividade;
import com.wpp.wppbotmanager.dto.enums.papel.Papel;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class UserDto {

  private Integer id_user;
  private String nome;
  private String telefone;
  private Papel papel;
  public  Atividade atividade;
  private Integer id_empresa;

  public UserDto() {}

  public UserDto(Integer id_user, String nome, String telefone, Papel papel, Atividade atividade, Integer id_empresa) {
    this.id_user = id_user;
    this.nome = nome;
    this.telefone = telefone;
    this.atividade = atividade;
    this.papel = papel;
    this.id_empresa = id_empresa;
  }
}