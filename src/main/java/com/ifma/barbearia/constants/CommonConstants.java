package com.ifma.barbearia.constants;

/**
 * Constantes comuns utilizadas em toda a aplicação.
 * Centraliza mensagens e códigos de status HTTP genéricos para evitar
 * duplicação.
 */
public final class CommonConstants {

    private CommonConstants() {
    }

    // Status HTTP
    public static final String STATUS_200 = "200";
    public static final String STATUS_201 = "201";
    public static final String STATUS_400 = "400";
    public static final String STATUS_417 = "417";
    public static final String STATUS_500 = "500";

    // Mensagens de sucesso genéricas
    public static final String MESSAGE_200 = "Requisição processada com sucesso";

    // Mensagens de erro genéricas
    public static final String MESSAGE_417_UPDATE = "Falha na operação de atualização. Tente novamente ou entre em contato com a equipe de desenvolvimento.";
    public static final String MESSAGE_417_DELETE = "Falha na operação de exclusão. Tente novamente ou entre em contato com a equipe de desenvolvimento.";
    public static final String MESSAGE_500 = "Um erro ocorreu. Tente novamente ou entre em contato com a equipe de desenvolvimento.";

}
