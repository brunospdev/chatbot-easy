package com.wpp.wppbotmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpp.wppbotmanager.service.AdministradorService;

@RestController
@RequestMapping("/admin")
public class AdministradorController {
  
  private final AdministradorService adminService;

  public AdministradorController(AdministradorService adminService) {
    this.adminService = adminService;
  }

  @GetMapping("/ladmin")
  public String getAdmin() {
    return adminService.getAdmin();
  }

  @PostMapping("/cadmin")
    public String postAdmin(@RequestBody String nome, @RequestBody String email, @RequestBody String senha){
        return adminService.postAdmin(nome, email, senha);
    }
}
