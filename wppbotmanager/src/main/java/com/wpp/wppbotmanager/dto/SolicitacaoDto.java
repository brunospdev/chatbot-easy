package com.wpp.wppbotmanager.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitacaoDto {

  private Integer id_solicitacao;
  private Integer tipo_solicitacao;
  private LocalDateTime data_solicitacao;
  private Integer status;
  private Integer id_usuario;

  public SolicitacaoDto() {}

  public SolicitacaoDto(Integer id_solicitacao, Integer tipo_solicitacao, LocalDateTime data_solicitacao, Integer status, Integer id_usuario ) {
    this.id_solicitacao = id_solicitacao;
    this.tipo_solicitacao = tipo_solicitacao;
    this.data_solicitacao = data_solicitacao;
    this.status = status;
    this.id_usuario = id_usuario;
  }
}
