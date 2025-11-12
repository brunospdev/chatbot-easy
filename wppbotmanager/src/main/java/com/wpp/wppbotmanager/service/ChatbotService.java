package com.wpp.wppbotmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;
import com.wpp.wppbotmanager.dto.ReceiveReportRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatbotService {

    private final UserStateManagerService userStateManager;
    private final MessageService messageService;

    public ChatbotService(UserStateManagerService userStateManager, MessageService messageService) {
        this.userStateManager = userStateManager;
        this.messageService = messageService;
    }
    private JsonNode unwrapMessageEnvelope(JsonNode node) {
        if (node == null || node.isNull()) return node;
        JsonNode cur = node;
        while (cur != null && cur.has("message")) {
            cur = cur.get("message");
        }
        if (cur != null && cur.isArray() && cur.size() > 0) {
            return cur.get(0);
        }
        return cur;
    }

    private static final String TEXTO_MENU_PRINCIPAL =
            "Olá, bem-vindo ao atendimento do Chatbot Easy!\nEscolha uma opção: \n" +
            "1. Resumo\n" +
            "2. Relatório\n" +
            "3. Gestão de Usuários";

    private static final String TEXTO_MENU_RESUMO =
            "Escolha um intervalo:\n" +
            "1. 7 dias\n" +
            "2. 15 dias\n" +
            "3. 30 dias\n" +
            "0. Voltar";

    private static final String TEXTO_MENU_RELATORIO =
            "Escolha um intervalo:\n" +
            "1. 7 dias\n" +
            "2. 15 dias\n" +
            "3. 30 dias\n" +
            "0. Voltar";

    private static final String TEXTO_MENU_GESTAO_USUARIOS =
            "1. Cadastrar usuários\n" +
            "2. Listar usuários\n" +
            "3. Deletar usuários\n" +
            "0. Voltar";

    private static final Map<String, String> MAPA_MENU_PRINCIPAL = Map.of(
            "1", "SUBMENU_RESUMO",
            "2", "SUBMENU_RELATORIO",
            "3", "SUBMENU_GESTAO_USUARIOS"
    );

    private static final Map<String, String> MAPA_MENU_RESUMO = Map.of(
            "1", "7_DIAS_RESUMO",
            "2", "15_DIAS_RESUMO",
            "3", "30_DIAS_RESUMO",
            "0", UserStateManagerService.MENU_PRINCIPAL
    );

    private static final Map<String, String> MAPA_MENU_RELATORIO = Map.of(
            "1", "7_DIAS_RELATORIO",
            "2", "15_DIAS_RELATORIO",
            "3", "30_DIAS_RELATORIO",
            "0", UserStateManagerService.MENU_PRINCIPAL
    );

    private static final Map<String, String> MAPA_MENU_GESTAO_USUARIOS = Map.of(
            "1", "CADASTRAR_USUARIOS",
            "2", "LISTAR_USUARIOS",
            "3", "DELETAR_USUARIOS",
            "0", UserStateManagerService.MENU_PRINCIPAL
    );

    private final ObjectMapper objectMapper = new ObjectMapper();

    public void enviarRelatorio(String numUser, int dias, ReceiveReportRequest reportRequest) {
        try {
            RestTemplate restTemplate = new RestTemplate();


            String idEmpresa = reportRequest.getIdEmpresa();
            if (idEmpresa == null || idEmpresa.isBlank()) {
                messageService.sendMessage(numUser, "Nenhuma empresa associada ao seu usuário. Por favor, verifique sua configuração.");
                return;
            }
            String empresaUrl = "http://localhost:3001/empresa/listarempresa/" + idEmpresa;
            String empresaJson = restTemplate.getForObject(empresaUrl, String.class);
            if (empresaJson == null || empresaJson.isBlank()) {
                messageService.sendMessage(numUser, "Empresa não encontrada ou retorno inválido.");
                return;
            }

            JsonNode empresaNode = objectMapper.readTree(empresaJson);
            
            empresaNode = unwrapMessageEnvelope(empresaNode);
            if (empresaNode == null || empresaNode.isNull()) {
                messageService.sendMessage(numUser, "Empresa não encontrada ou formato inesperado.");
                return;
            }
            if (empresaNode.isArray() && empresaNode.size() > 0) {
                empresaNode = empresaNode.get(0);
            }

            String appKey = null;
            String appSecret = null;
            
            if (empresaNode.has("app_key")) appKey = empresaNode.path("app_key").asText(null);
            if ((appKey == null || appKey.isEmpty()) && empresaNode.has("appKey")) appKey = empresaNode.path("appKey").asText(null);
            if (empresaNode.has("app_secret")) appSecret = empresaNode.path("app_secret").asText(null);
            if ((appSecret == null || appSecret.isEmpty()) && empresaNode.has("appSecret")) appSecret = empresaNode.path("appSecret").asText(null);

            if (appKey == null || appKey.isEmpty() || appSecret == null || appSecret.isEmpty()) {
                messageService.sendMessage(numUser, "Dados de integração da empresa não encontrados.");
                return;
            }


            String url = "http://localhost:3001/omie/relatorio-financeiro?dias=" + dias;
            Map<String, Object> body = new HashMap<>();
            body.put("appKey", appKey);
            body.put("appSecret", appSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            String response = restTemplate.postForObject(url, entity, String.class);

            if (response == null) {
                messageService.sendMessage(numUser, "Falha ao gerar relatório: resposta vazia do servidor.");
                return;
            }


            try {
                JsonNode jsonResponse = objectMapper.readTree(response);

                StringBuilder msg1 = new StringBuilder();
                JsonNode resumo = jsonResponse.path("resumo_geral");
                msg1.append("resumo_geral:\n");
                JsonNode periodo = resumo.path("periodo_analisado");
                if (!periodo.isMissingNode()) {
                    String dataInicio = periodo.path("data_inicio").asText("");
                    String dataFim = periodo.path("data_fim").asText("");
                    int totalDias = periodo.path("total_dias").asInt(0);
                    msg1.append("  periodo_analisado\n");
                    msg1.append("    " + dataInicio + " - " + dataFim + "\n");
                    msg1.append("      total de dias: " + totalDias + "\n\n");
                }

                StringBuilder full = new StringBuilder();
                full.append(msg1.toString()).append("\n");
                full.append("total receitas: " + resumo.path("total_receitas").asText("") + "\n");
                full.append("total de despesas ou custos: " + resumo.path("total_despesas_custos").asText("") + "\n");
                full.append("resultado liquido: " + resumo.path("resultado_liquido").asText("") + "\n\n");

                full.append("detalhes por categoria:\n");
                JsonNode detalhes = jsonResponse.path("detalhes_por_categoria");
                if (detalhes.isObject()) {
                    detalhes.fieldNames().forEachRemaining(field -> {
                        JsonNode val = detalhes.get(field);
                        String line = "  " + field + ": " + (val == null ? "" : val.asText());
                        full.append(line + "\n");
                    });
                }

                messageService.sendMessage(numUser, full.toString());

            } catch (Exception e) {
                messageService.sendMessage(numUser, response);
            }

        } catch (Exception e) {
            System.err.println("Erro ao solicitar relatório: " + e.getMessage());
            e.printStackTrace();
            messageService.sendMessage(numUser, "Erro ao gerar relatório: " + e.getMessage());
        }
    }
    
    public String analisarPapel(ReceiveMessageRequest request, String textInput) {
        if ("administrador".equalsIgnoreCase(request.getPapel())) {
            return MAPA_MENU_GESTAO_USUARIOS.getOrDefault(textInput, "ESTADO_INVALIDO");
        } else {
            messageService.sendMessage(request.getFrom(), "Acesso negado. Função disponível apenas para administradores.");
            return "ACESSO_NEGADO";
        }
    }
    public void processMessage(ReceiveMessageRequest request, ReceiveReportRequest reportRequest) {
        String numUser = request.getFrom();
        String textInput = request.getTexto();
        String estadoAtual = userStateManager.getState(numUser);
        reportRequest.setIdEmpresa(request.getId_empresa());

        String proximoEstado;
        String resposta = "";

        switch (estadoAtual) {
            case UserStateManagerService.MENU_PRINCIPAL:
                if ("3".equals(textInput)) {
                    if ("administrador".equalsIgnoreCase(request.getPapel())) {
                        proximoEstado = "SUBMENU_GESTAO_USUARIOS";
                    } else {
                        messageService.sendMessage(numUser, "Função disponível apenas para administradores.");
                        messageService.sendMessage(numUser, TEXTO_MENU_PRINCIPAL);
                        proximoEstado = UserStateManagerService.MENU_PRINCIPAL;
                        userStateManager.setState(numUser, proximoEstado);
                        return;
                    }
                } else {
                    proximoEstado = MAPA_MENU_PRINCIPAL.getOrDefault(textInput, UserStateManagerService.MENU_PRINCIPAL);
                }
                break;

            case "SUBMENU_RELATORIO":
                int dias = switch (textInput) {
                    case "1" -> 7;
                    case "2" -> 15;
                    case "3" -> 30;
                    default -> 0;
                };

                if (dias > 0) {
                    resposta = "Gerando relatório de " + dias + " dias...";
                   
                    messageService.sendMessage(numUser, resposta);
                    resposta = "";
                    enviarRelatorio(numUser, dias, reportRequest);
                } else if ("0".equals(textInput)) {
                    proximoEstado = UserStateManagerService.MENU_PRINCIPAL;
                    resposta = TEXTO_MENU_PRINCIPAL;
                    userStateManager.setState(numUser, proximoEstado);
                    messageService.sendMessage(numUser, resposta);
                    return;
                } else {
                    resposta = "Opção inválida!\n" + TEXTO_MENU_RELATORIO;
                }

                proximoEstado = MAPA_MENU_RELATORIO.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;
            case "SUBMENU_RESUMO":
                proximoEstado = MAPA_MENU_RESUMO.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;

            case "SUBMENU_GESTAO_USUARIOS":
                proximoEstado = analisarPapel(request, textInput);
                break;
            default:
                proximoEstado = UserStateManagerService.MENU_PRINCIPAL;
        }
        switch (proximoEstado) {
            case UserStateManagerService.MENU_PRINCIPAL -> resposta = TEXTO_MENU_PRINCIPAL;
            case "SUBMENU_RESUMO" -> resposta = TEXTO_MENU_RESUMO;
            case "SUBMENU_RELATORIO" -> resposta = TEXTO_MENU_RELATORIO;
            case "SUBMENU_GESTAO_USUARIOS" -> resposta = TEXTO_MENU_GESTAO_USUARIOS;
            case "ACESSO_NEGADO" -> { return; }
            case "ESTADO_INVALIDO" -> {
                resposta = "Opção inválida!\n" + TEXTO_MENU_PRINCIPAL;
                proximoEstado = estadoAtual;
            }
        }
        if (resposta != null && !resposta.isBlank()) {
            messageService.sendMessage(numUser, resposta);
        }
        userStateManager.setState(numUser, proximoEstado);
    }
    public void inactiveUser(String numUser) {
        messageService.sendMessage(numUser, "Contate um administrador para reativar seu acesso ao chatbot.");
    }
    public void unknownUser(String numUser) {
        messageService.sendMessage(numUser, "Usuário não encontrado. Por favor, contate um administrador para se cadastrar.");
    }
}
