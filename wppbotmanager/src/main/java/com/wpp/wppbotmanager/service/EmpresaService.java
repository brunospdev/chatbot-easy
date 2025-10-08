package com.wpp.wppbotmanager.service;

import org.springframework.stereotype.Service;

import com.wpp.wppbotmanager.client.EmpresaBd;

@Service
public class EmpresaService {

  private final EmpresaBd empresaBd;

  public EmpresaService(EmpresaBd empresaBd) {
    this.empresaBd = empresaBd;
  }

  public String getEmpresa() {
    return empresaBd.getEmpresa();
  }
}
