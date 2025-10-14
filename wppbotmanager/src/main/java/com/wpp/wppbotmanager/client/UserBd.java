package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.wpp.wppbotmanager.dto.UserDto;

@Component
public class UserBd {

  private final WebClient userTb;

  public UserBd(WebClient.Builder builder) {
    this.userTb = builder
        .baseUrl("http://localhost:3001/users")
        .build();
  }

  public String getUser() {
    return userTb.get()
      .uri("/luser")
      .retrieve()
      .bodyToMono(String.class)
      .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
      .block();
  }
  
  public String createUser(UserDto userDto) {
      return userTb.post()
          .uri("/cuser")
          .bodyValue(userDto)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao criar usuário: " + e.getMessage()))
          .block();
  }

  public String updateUser(Integer id, UserDto userDto) {
      return userTb.put()
          .uri("/uuser/{id}", id)
          .bodyValue(userDto)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao atualizar usuário: " + e.getMessage()))
          .block();
  }

  public String deleteUser(Integer id) {
      return userTb.delete()
          .uri("/duser/{id}", id)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao deletar usuário: " + e.getMessage()))
          .block();
  }

  public String getUserById(Integer id) {
      return userTb.get()
          .uri("/guser/{id}", id)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao buscar usuário por id: " + e.getMessage()))
          .block();
  }
}
