package com.wpp.wppbotmanager.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.wpp.wppbotmanager.dto.AgendamentoDto;

@Component
public class AgendBd {
  
  private WebClient agendBd;

    public AgendBd(WebClient.Builder builder) {
    this.agendBd = builder
      .baseUrl("http://localhost:3001/Agendamento")
      .build();
  }

  public String getUser() {
    return agendBd.get()
      .uri("/lagend")
      .retrieve()
      .bodyToMono(String.class)
      .doOnError(e -> System.err.println("Erro ao chamar API TS: " + e.getMessage()))
      .block();
  }
  
  public String createUser(AgendamentoDto agendamentoDto) {
      return agendBd.post()
          .uri("/cagend")
          .bodyValue(agendamentoDto)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao criar agendamento: " + e.getMessage()))
          .block();
  }

  public String updateUser(Integer id, AgendamentoDto agendamentoDto) {
      return agendBd.put()
          .uri("/uagend/{id}", id)
          .bodyValue(agendamentoDto)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao atualizar agendamento: " + e.getMessage()))
          .block();
  }

  public String deleteUser(Integer id) {
      return agendBd.delete()
          .uri("/dagend/{id}", id)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao deletar agendamento: " + e.getMessage()))
          .block();
  }

  public String getUserById(Integer id) {
      return agendBd.get()
          .uri("/gagend/{id}", id)
          .retrieve()
          .bodyToMono(String.class)
          .doOnError(e -> System.err.println("Erro ao buscar agendamento por id: " + e.getMessage()))
          .block();
  }
}
