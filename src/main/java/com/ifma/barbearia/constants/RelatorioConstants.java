package com.ifma.barbearia.constants;

/**
 * Constantes específicas do domínio Relatório.
 * Para constantes genéricas, use {@link CommonConstants}.
 */
public final class RelatorioConstants {

    private RelatorioConstants() {
    }

    // Mensagens específicas de relatório
    public static final String MESSAGE_200 = "Relatório gerado com sucesso";
    public static final String MESSAGE_400_DATA_INVALIDA = "A data final não pode ser anterior à data inicial";
    public static final String MESSAGE_500 = "Erro interno ao gerar o relatório";

}