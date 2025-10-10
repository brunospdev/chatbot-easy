package com.wpp.wppbotmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpp.wppbotmanager.service.EmpresaService;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

  private final EmpresaService empresaService;

  public EmpresaController(EmpresaService empresaService) {
    this.empresaService = empresaService;
  }

  @GetMapping("/listarEmpresa")
  public String getEmpresas() {
    return empresaService.getEmpresa();
  }

  @PostMapping("/criarempresa")
    public String postEmpresa(@RequestBody String nome_empresa, @RequestBody String cnpj, @RequestBody String token_api){
        return empresaService.postEmpresa(nome_empresa, cnpj, token_api);
    }
}
