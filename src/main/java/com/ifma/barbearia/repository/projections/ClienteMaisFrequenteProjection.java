package com.ifma.barbearia.repository.projections;

/**
 * Projection interface para retornar dados agregados de clientes mais
 * frequentes.
 * Evita o uso de Object[] e casts manuais.
 */
public interface ClienteMaisFrequenteProjection {

    Long getClienteId();

    String getNomeCliente();

    String getEmailCliente();

    String getTelefoneCliente();

    Long getQuantidadeAtendimentos();

    Double getValorTotalGasto();
}
