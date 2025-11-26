package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.AgendamentoDto;
import com.ifma.barbearia.DTOs.AgendamentoRequestDto;

import java.util.List;

public interface IAgendamentoService {

    void criarAgendamento(AgendamentoRequestDto agendamentoRequestDto);

    AgendamentoDto buscarAgendamento(Long agendamentoId);

    List<AgendamentoDto> buscarTodosAgendamentos();

    List<AgendamentoDto> buscarAgendamentosPorCliente(Long clienteId);

    boolean atualizarAgendamento(AgendamentoRequestDto agendamentoRequestDto);

    boolean cancelarAgendamento(Long agendamentoId);

    void concluirAgendamento(Long agendamentoId);

}
