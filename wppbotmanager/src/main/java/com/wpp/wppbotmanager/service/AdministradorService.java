package com.wpp.wppbotmanager.service;

import org.springframework.stereotype.Service;

import com.wpp.wppbotmanager.client.AdministradorBd;
import com.wpp.wppbotmanager.dto.AdministradorDto;

@Service
public class AdministradorService {

  private final AdministradorBd adminBd;

  public AdministradorService(AdministradorBd adminBd) {
    this.adminBd = adminBd;
  }

  public String getAdmin() {
    return adminBd.getAdmin();
  }

  public String postAdmin(String nome, String email, String senha) {
    try {
      AdministradorDto request = new AdministradorDto(null, nome, email, senha);
        return adminBd.postAdmin(request);
    } catch(Exception e) {
      return "erro ao criar Administrador: " + e.getMessage();
    }
  }
}
