package com.ifma.barbearia.mappers;

import com.ifma.barbearia.DTOs.HistoricoAtendimentoDto;
import com.ifma.barbearia.entities.HistoricoAtendimento;

public class HistoricoAtendimentoMapper {

    public static HistoricoAtendimentoDto mapToHistoricoAtendimentoDto(HistoricoAtendimento historicoAtendimento, HistoricoAtendimentoDto historicoAtendimentoDto) {

        historicoAtendimentoDto.setId(historicoAtendimento.getHistoricoAtendimentoId());
        historicoAtendimentoDto.setDataAtendimento(historicoAtendimento.getData());
        historicoAtendimentoDto.setValorPago(historicoAtendimento.getPagamento().getValor());
        historicoAtendimentoDto.setFormaPagamento(historicoAtendimento.getPagamento().getFormaPagamento());
        historicoAtendimentoDto.setNomeCliente(historicoAtendimento.getCliente().getNome());
        historicoAtendimentoDto.setNomeBarbeiro(historicoAtendimento.getBarbeiro().getNome());
        historicoAtendimentoDto.setNomeServico(historicoAtendimento.getServico().getNome());
        return historicoAtendimentoDto;
    }

}
