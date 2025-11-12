package com.wpp.wppbotmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.wpp.wppbotmanager.dto.ReceiveMessageRequest;

import java.util.Map;

@Service
public class ChatbotService {

    private final UserStateManagerService userStateManager;
    private final MessageService messageService;

    public ChatbotService(UserStateManagerService userStateManager, MessageService messageService) {
        this.userStateManager = userStateManager;
        this.messageService = messageService;
    }
    // analisa papel do usuario para liberar menu de gestao de usuarios
    public String analisarPapel(ReceiveMessageRequest request, String textInput) {
        String papel = request.getPapel();

        if ("administrador".equalsIgnoreCase(papel)) {
            return MAPA_MENU_GESTAO_USUARIOS.getOrDefault(textInput, "ESTADO_INVALIDO");
        } else {
            messageService.sendMessage(request.getFrom(), "Acesso negado. Função disponível apenas para administradores.");
            return "ACESSO_NEGADO";
        }
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

    public void processMessage(ReceiveMessageRequest request) {
        String numUser = request.getFrom();
        String textInput = request.getTexto();
        String estadoAtual = userStateManager.getState(numUser);

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
                if("2".equals(textInput)){
                    proximoEstado = "SUBMENU_RELATORIO";
                    
                }
                break;
            case "SUBMENU_RESUMO":
                proximoEstado = MAPA_MENU_RESUMO.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;
            case "SUBMENU_RELATORIO":
                proximoEstado = MAPA_MENU_RELATORIO.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;
            case "SUBMENU_GESTAO_USUARIOS":
                proximoEstado = analisarPapel(request, textInput);
                break;
            default:
                proximoEstado = UserStateManagerService.MENU_PRINCIPAL;
        }
        switch (proximoEstado) {
            case UserStateManagerService.MENU_PRINCIPAL:
                resposta = TEXTO_MENU_PRINCIPAL;
                break;
            case "SUBMENU_RESUMO":
                resposta = TEXTO_MENU_RESUMO;
                break;
            case "SUBMENU_RELATORIO":
                resposta = TEXTO_MENU_RELATORIO;
                break;
            case "SUBMENU_GESTAO_USUARIOS":
                resposta = TEXTO_MENU_GESTAO_USUARIOS;
                break;
            case "ACESSO_NEGADO":
                return;
            case "ESTADO_INVALIDO":
                resposta = "Opção inválida!\n" + TEXTO_MENU_PRINCIPAL;
                proximoEstado = estadoAtual;
                break;
        }
        messageService.sendMessage(numUser, resposta);
        userStateManager.setState(numUser, proximoEstado);
    }
    public void inactiveUser(String numUser) {
        messageService.sendMessage(numUser, "Contate um administrador para reativar seu acesso ao chatbot.");
    }
    public void unknownUser(String numUser) {
        messageService.sendMessage(numUser, "Usuário não encontrado. Por favor, contate um administrador para se cadastrar.");
    }
}
