package com.wpp.wppbotmanager.service;

import org.springframework.stereotype.Service;

import com.wpp.wppbotmanager.client.SolicitacaoBd;
import com.wpp.wppbotmanager.dto.SolicitacaoDto;

@Service
public class SolicitacaoService {

  private final SolicitacaoBd solicitacaoBd;

  public SolicitacaoService(SolicitacaoBd solicitacaoBd) {
    this.solicitacaoBd = solicitacaoBd;
  }

  public String getSolicitacao() {
    return solicitacaoBd.getSolicitacao();
  }

  public String postSolicitacao(Integer tipo_solicitacao, Integer status, Integer id_usuario) {
    try {
      SolicitacaoDto request = new SolicitacaoDto(null , tipo_solicitacao, null, status, id_usuario);
        return solicitacaoBd.postSolicitacao(request);
    } catch (Exception e) {
      return "erro ao criar empresa: " + e.getMessage();
    }
  }
}
