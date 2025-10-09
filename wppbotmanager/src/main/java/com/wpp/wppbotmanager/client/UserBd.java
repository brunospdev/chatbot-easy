package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserBd {

  private final WebClient userTb;

  public UserBd(WebClient.Builder builder) {
    this.userTb = builder
        .baseUrl("http://localhost:3001/usuario")
        .build();
  }
}
