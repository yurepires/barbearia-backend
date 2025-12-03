package com.ifma.barbearia.mappers;

import com.ifma.barbearia.DTOs.AgendamentoDto;
import com.ifma.barbearia.entities.Agendamento;
import com.ifma.barbearia.entities.Barbeiro;
import com.ifma.barbearia.entities.Cliente;
import com.ifma.barbearia.entities.Servico;

public class AgendamentoMapper {

    public static AgendamentoDto mapToAgendamentoDto(Agendamento agendamento, AgendamentoDto agendamentoDto) {
        agendamentoDto.setId(agendamento.getAgendamentoId());
        agendamentoDto.setHorario(agendamento.getHorario());
        agendamentoDto.setStatus(agendamento.getStatus());
        agendamentoDto.setClienteEmail(agendamento.getCliente().getEmail());
        agendamentoDto.setServicoId(agendamento.getServico().getServicoId());
        agendamentoDto.setBarbeiroEmail(agendamento.getBarbeiro().getEmail());
        return agendamentoDto;
    }

    public static Agendamento mapToAgendamento(AgendamentoDto agendamentoDto, Agendamento agendamento, Cliente cliente, Servico servico, Barbeiro barbeiro) {
        agendamento.setHorario(agendamentoDto.getHorario());
        agendamento.setStatus(agendamentoDto.getStatus());
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setBarbeiro(barbeiro);
        return agendamento;
    }

}
