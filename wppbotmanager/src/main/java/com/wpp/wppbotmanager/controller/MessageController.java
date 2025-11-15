package com.wpp.wppbotmanager.controller;

import com.wpp.wppbotmanager.service.ChatbotService;
import com.wpp.wppbotmanager.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;
import com.wpp.wppbotmanager.dto.SendMessageRequest;
import com.wpp.wppbotmanager.dto.enums.primeiro_contato;
import com.wpp.wppbotmanager.dto.ReceiveReportRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/wpp/messages")
public class MessageController {

  private final MessageService messageService;
  private final ChatbotService chatbotService;

  public MessageController(MessageService messageService, ChatbotService chatbotService) {
    this.messageService = messageService;
    this.chatbotService = chatbotService;
  }

  @GetMapping
  public String getMessages() {
    return messageService.getMessageApi();
  }

  @PostMapping("/enviar")
  public String SendMessage(@RequestBody SendMessageRequest request) {
    return messageService.sendMessage(request.getNumero(), request.getTexto());
  }

  @PostMapping("/receber/msg")
  public ResponseEntity<?> receiveMessage(@RequestBody ReceiveMessageRequest request) {
    String numUser = request.getFrom();

    try {
      System.out.println("[DEBUG] Incoming message from: " + numUser);

      String userUrl = "http://localhost:3001/users/telefone/" + numUser;
      RestTemplate restTemplate = new RestTemplate();
      String userJson = null;

      try {
        userJson = restTemplate.getForObject(userUrl, String.class);
        System.out.println("[DEBUG] User fetched from Node: " + userJson);
      } catch (Exception e) {
        System.out.println("[DEBUG] Error fetching user from Node: " + e.getMessage());
        userJson = null;
      }

      // Se usuário não existe
      if (userJson == null || userJson.isBlank()) {
        System.out.println("[DEBUG] User not found for: " + numUser);
        chatbotService.unknownUser(numUser);
        return ResponseEntity.ok("Usuário não encontrado no banco");
      }

      // Parse JSON
      ObjectMapper mapper = new ObjectMapper();
      JsonNode userNode = mapper.readTree(userJson);
      System.out.println("[DEBUG] Parsed JSON node: " + userNode.toPrettyString());

      if (userNode.has("message")) {
        userNode = userNode.get("message");
      }

      // Dados extraídos
      String idEmpresa = userNode.path("id_empresa").asText(null);
      String atividade = userNode.path("atividade").asText("ativo").trim().toLowerCase();
      String papel = userNode.path("papel").asText("user");
      String nome = userNode.path("nome").asText(null);

      String primeiroContatoRaw = userNode.path("primeiro_contato").asText("nao");
      System.out.println("[DEBUG] primeiro_contato RAW: [" + primeiroContatoRaw + "]");

      // normalizar removendo espaços, quebras, tabs, caracteres invisíveis
      String primeiroContato = primeiroContatoRaw
          .replaceAll("[^A-Za-z0-9]", "")
          .trim()
          .toLowerCase();

      System.out.println("[DEBUG] primeiro_contato NORMALIZADO: [" + primeiroContato + "]");
      System.out.println("[DEBUG] primeiro_contato NORMALIZADO LEN = " + primeiroContato.length());

      // set no DTO
      request.setId_empresa(idEmpresa);
      request.setStatus(atividade);
      request.setPapel(papel);
      request.setNome(nome);

      // ===== SE FOR PRIMEIRA MENSAGEM =====
      if ("nao".equalsIgnoreCase(primeiroContatoRaw))) {
        System.out.println("[DEBUG] >>> ENTROU NO BLOCO DE PRIMEIRO CONTATO <<<");

        try {
          String marcarUrl = "http://localhost:3001/users/pcontato/" + numUser;
          restTemplate.put(marcarUrl, null);
          System.out.println("[DEBUG] primeiro_contato atualizado para 'sim'");
        } catch (Exception e) {
          System.out.println("[ERROR] Falha ao atualizar primeiro_contato: " + e.getMessage());
        }
      }

      if ("ativo".equalsIgnoreCase(atividade)) {
        ReceiveReportRequest reportRequest = new ReceiveReportRequest();
        reportRequest.setIdEmpresa(idEmpresa);

        chatbotService.processMessage(request, reportRequest);

        return ResponseEntity.ok("Mensagem recebida e usuário ativo");
      }

      if ("inativo".equalsIgnoreCase(atividade)) {
        chatbotService.inactiveUser(numUser);
        return ResponseEntity.ok("Mensagem recebida e usuário inativo");
      }

      return ResponseEntity.ok("Mensagem recebida com status desconhecido");

    } catch (Exception e) {
      System.err.println("[ERROR] Exception in receiveMessage: " + e.getMessage());
      e.printStackTrace();
      return ResponseEntity.status(500).body("Erro ao processar mensagem: " + e.getMessage());
    }
  }
}