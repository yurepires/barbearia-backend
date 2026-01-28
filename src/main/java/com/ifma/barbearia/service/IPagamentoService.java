package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.entity.Pagamento;

public interface IPagamentoService {

    PagamentoDto buscarPagamento(Long pagamentoId);

    PagamentoDto buscarPagamentoPorAgendamento(Long agendamentoId);

    /**
     * Salva um pagamento (uso interno entre serviços).
     */
    Pagamento salvarPagamento(Pagamento pagamento);

}