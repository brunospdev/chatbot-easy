package com.wpp.wppbotmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpp.wppbotmanager.service.ConfigErpService;

@RestController
@RequestMapping("/configErp")
public class ConfigErpController {

  private final ConfigErpService configErpService;

  public ConfigErpController(ConfigErpService configErpService) {
    this.configErpService = configErpService;
  }

  @GetMapping("/lconfigerp")
  public String getConfigErp() {
    return configErpService.getConfigErp();
  }

  @PostMapping("/cconfigerp")
    public String postAdmin(@RequestBody String url_api, @RequestBody String token_api, @RequestBody Integer status, @RequestBody Integer id_empresa){
        return configErpService.postConfigErp(url_api, token_api, status, id_empresa);
    }
}
