package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.AgendamentoDto;

import java.time.LocalDate;
import java.util.List;

public interface IAgendamentoService {

    void criarAgendamento(AgendamentoDto agendamentoDto);

    AgendamentoDto buscarAgendamento(Long agendamentoId);

    List<AgendamentoDto> buscarTodosAgendamentos();

    List<AgendamentoDto> buscarAgendamentosPorCliente(Long clienteId);

    List<AgendamentoDto> buscarAgendamentosPorIntervaloDeDatas(LocalDate inicio, LocalDate fim);

    boolean atualizarAgendamento(AgendamentoDto agendamentoDto);

    boolean cancelarAgendamento(Long agendamentoId);

    void concluirAgendamento(Long agendamentoId);

}
