package com.wpp.wppbotmanager.dto;

import lombok.Getter;
import lombok.Setter;
import com.wpp.wppbotmanager.dto.papel.Papel;
@Getter
@Setter
public class UserDto {

  private Integer id_user;
  private String nome;
  private String telefone;
  private Papel papel;
  private Integer id_empresa;

  public UserDto() {}

  public UserDto(Integer id_user, String nome, String telefone, Papel papel, Integer id_empresa) {
    this.id_user = id_user;
    this.nome = nome;
    this.telefone = telefone;
    this.papel = papel;
    this.id_empresa = id_empresa;
  }
}