package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.PagamentoDto;

public interface IPagamentoService {


    PagamentoDto buscarPagamento(Long pagamentoId);

    PagamentoDto buscarPagamentoPorAgendamento(Long agendamentoId);

}
