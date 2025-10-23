package com.wpp.wppbotmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ChatbotService {

    private final UserStateManagerService userStateManager;
    private final MessageService messageService;

    public ChatbotService(UserStateManagerService userStateManager, MessageService messageService) {
        this.userStateManager = userStateManager;
        this.messageService = messageService;
    }

    private static final String TEXTO_MENU_PRINCIPAL =
            "Olá, bem vindo ao atendimento do Chatbot Easy!\nEscolha uma opção: \n" +
                    "1. Resumo\n" +
                    "2. Relatório\n" +
                    "3. Gestão de Usuários";

    private static final String TEXTO_MENU_RESUMO =
            "Escolha um intervalo:" +
                    "1. 7 dias\n" +
                    "2. 15 dias\n" +
                    "3. 30 dias\n" +
                    "0. Voltar";

    private static final String TEXTO_MENU_RELATORIO =
            "Escolha um intervalo:" +
                    "1. 7 dias\n" +
                    "2. 15 dias\n" +
                    "3. 30 dias\n" +
                    "0. Voltar";

    private static final String TEXTO_MENU_GESTAO_USUARIOS =
            "1. Cadastrar usuários\n" +
            "2. Listar usuários\n" +
            "3. Deletar usuários" +
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

    public void processMessage(String numUser, String textInput) {
        String estadoAtual = userStateManager.getState(numUser);

        String proximoEstado;
        String resposta = "";

        switch(estadoAtual) {
            case UserStateManagerService.MENU_PRINCIPAL:
                proximoEstado = MAPA_MENU_PRINCIPAL.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;
            case "SUBMENU_RESUMO":
                proximoEstado = MAPA_MENU_RESUMO.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;
            case "SUBMENU_RELATORIO":
                proximoEstado = MAPA_MENU_RELATORIO.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;
            case "SUBMENU_GESTAO_USUARIOS":
                proximoEstado = MAPA_MENU_GESTAO_USUARIOS.getOrDefault(textInput, "ESTADO_INVALIDO");
                break;
            default:
                proximoEstado = UserStateManagerService.MENU_PRINCIPAL;
        }

        switch(proximoEstado) {
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
            case "7_DIAS_RESUMO":
                break;
            case "15_DIAS_RESUMO":
                break;
            case "30_DIAS_RESUMO":
                break;
            case "7_DIAS_RELATORIO":
                break;
            case "15_DIAS_RELATORIO":
                break;
            case "30_DIAS_RELATORIO":
                break;
            case "CADASTRAR_USUARIOS":
                break;
            case "LISTAR_USUARIOS":
                break;
            case "DELETAR_USUARIOS":
                break;
            case "ESTADO_INVALIDO":
                resposta = "Opção inválida!";
                proximoEstado = estadoAtual;
                break;
        }

        messageService.sendMessage(numUser, resposta);
        userStateManager.setState(numUser, proximoEstado);
    }
}
