package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.PagamentoDto;

public interface IPagamentoService {

    void criarPagamento(PagamentoDto pagamentoDto);

    PagamentoDto buscarPagamento(Long pagamentoId);

    PagamentoDto buscarPagamentoPorAgendamento(Long agendamentoId);

}
