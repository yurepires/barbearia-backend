package com.ifma.barbearia.mappers;

import com.ifma.barbearia.DTOs.PagamentoDto;
import com.ifma.barbearia.entities.Pagamento;

public class PagamentoMapper {

    public static PagamentoDto mapToDto(Pagamento pagamento, PagamentoDto pagamentoDto) {
        pagamentoDto.setPagamentoId(pagamento.getPagamentoId());
        pagamentoDto.setAgendamentoId(pagamento.getAgendamento().getAgendamentoId());
        pagamentoDto.setValor(pagamento.getValor());
        pagamentoDto.setFormaPagamento(pagamento.getFormaPagamento());
        pagamentoDto.setStatus(pagamento.getStatus());
        pagamentoDto.setDataPagamento(pagamento.getDataPagamento());
        return pagamentoDto;
    }

    public static Pagamento mapToEntity(PagamentoDto pagamentoDto, Pagamento pagamento) {
        pagamento.setPagamentoId(pagamentoDto.getPagamentoId());
        pagamento.setValor(pagamentoDto.getValor());
        pagamento.setFormaPagamento(pagamentoDto.getFormaPagamento());
        pagamento.setStatus(pagamentoDto.getStatus());
        pagamento.setDataPagamento(pagamentoDto.getDataPagamento());
        return pagamento;
    }

}
