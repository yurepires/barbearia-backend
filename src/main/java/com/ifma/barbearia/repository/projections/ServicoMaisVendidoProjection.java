package com.ifma.barbearia.repository.projections;

/**
 * Projection interface para retornar dados agregados de serviços mais vendidos.
 * Evita o uso de Object[] e casts manuais.
 */
public interface ServicoMaisVendidoProjection {

    Long getServicoId();

    String getNomeServico();

    Double getPreco();

    Long getQuantidadeVendas();

    Double getValorTotalArrecadado();
}
