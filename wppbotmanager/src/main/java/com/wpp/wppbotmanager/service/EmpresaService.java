package com.wpp.wppbotmanager.service;

import org.springframework.stereotype.Service;

import com.wpp.wppbotmanager.client.EmpresaBd;
import com.wpp.wppbotmanager.dto.EmpresaDto;

@Service
public class EmpresaService {

  private final EmpresaBd empresaBd;

  public EmpresaService(EmpresaBd empresaBd) {
    this.empresaBd = empresaBd;
  }

  public String getEmpresa() {
    return empresaBd.getEmpresa();
  }

  public String postEmpresa(String nome_empresa, String cnpj, String token_api) {
    try {
      EmpresaDto request = new EmpresaDto(null , nome_empresa, cnpj, token_api);
        return empresaBd.postEmpresa(request);
    } catch (Exception e) {
      return "erro ao criar empresa: " + e.getMessage();
    }
  }
}
