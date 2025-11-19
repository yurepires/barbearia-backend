package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.AgendamentoDto;

import java.util.List;

public interface IAgendamentoService {

    void criarAgendamento(AgendamentoDto agendamentoDto);

    AgendamentoDto buscarAgendamento(Long agendamentoId);

    List<AgendamentoDto> buscarTodosAgendamentos();

    List<AgendamentoDto> buscarAgendamentosPorCliente(Long clienteId);

    boolean atualizarAgendamento(AgendamentoDto agendamentoDto);

    boolean deletarAgendamento(Long agendamentoId);

    void concluirAgendamento(Long agendamentoId);

}
