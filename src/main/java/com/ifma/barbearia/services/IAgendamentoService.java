package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.AgendamentoDto;

import java.util.List;

public interface IAgendamentoService {

    void criarAgendamento(AgendamentoDto agendamentoDto);

    AgendamentoDto buscarAgendamento(Long agendamentoId);

    List<AgendamentoDto> buscarTodosAgendamentos();

    boolean atualizarAgendamento(AgendamentoDto agendamentoDto);

    boolean deletarAgendamento(Long agendamentoId);

}
