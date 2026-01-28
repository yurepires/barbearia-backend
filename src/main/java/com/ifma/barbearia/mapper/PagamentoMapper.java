package com.ifma.barbearia.mapper;

import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.entity.Agendamento;
import com.ifma.barbearia.entity.Pagamento;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {

    public PagamentoDto toDto(Pagamento pagamento) {
        PagamentoDto dto = new PagamentoDto();
        dto.setPagamentoId(pagamento.getPagamentoId());
        dto.setAgendamentoId(pagamento.getAgendamento().getAgendamentoId());
        dto.setValor(pagamento.getValor());
        dto.setFormaPagamento(pagamento.getFormaPagamento());
        dto.setDataPagamento(pagamento.getDataPagamento());
        return dto;
    }

    public Pagamento toEntity(PagamentoDto dto, Agendamento agendamento) {
        Pagamento pagamento = new Pagamento();
        return updateEntity(dto, pagamento, agendamento);
    }

    public Pagamento updateEntity(PagamentoDto dto, Pagamento pagamento, Agendamento agendamento) {
        pagamento.setPagamentoId(dto.getPagamentoId());
        pagamento.setValor(dto.getValor());
        pagamento.setFormaPagamento(dto.getFormaPagamento());
        pagamento.setDataPagamento(dto.getDataPagamento());
        pagamento.setAgendamento(agendamento);
        return pagamento;
    }

}
