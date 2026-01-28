package com.ifma.barbearia.mapper;

import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.entity.Agendamento;
import com.ifma.barbearia.entity.Barbeiro;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.entity.Servico;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public AgendamentoDto toDto(Agendamento agendamento) {
        AgendamentoDto dto = new AgendamentoDto();
        dto.setId(agendamento.getAgendamentoId());
        dto.setHorario(agendamento.getHorario());
        dto.setStatus(agendamento.getStatus());
        dto.setClienteEmail(agendamento.getCliente().getEmail());
        dto.setServicoId(agendamento.getServico().getServicoId());
        dto.setBarbeiroEmail(agendamento.getBarbeiro().getEmail());
        return dto;
    }

    public Agendamento toEntity(AgendamentoDto dto, Cliente cliente, Servico servico, Barbeiro barbeiro) {
        Agendamento agendamento = new Agendamento();
        return updateEntity(dto, agendamento, cliente, servico, barbeiro);
    }

    public Agendamento updateEntity(AgendamentoDto dto, Agendamento agendamento, Cliente cliente, Servico servico,
            Barbeiro barbeiro) {
        agendamento.setHorario(dto.getHorario());
        agendamento.setStatus(dto.getStatus());
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setBarbeiro(barbeiro);
        return agendamento;
    }

}
