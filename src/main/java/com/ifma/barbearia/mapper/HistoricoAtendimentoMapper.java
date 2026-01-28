package com.ifma.barbearia.mapper;

import com.ifma.barbearia.dto.HistoricoAtendimentoDto;
import com.ifma.barbearia.entity.HistoricoAtendimento;
import org.springframework.stereotype.Component;

@Component
public class HistoricoAtendimentoMapper {

    public HistoricoAtendimentoDto toDto(HistoricoAtendimento historicoAtendimento) {
        HistoricoAtendimentoDto dto = new HistoricoAtendimentoDto();
        dto.setId(historicoAtendimento.getHistoricoAtendimentoId());
        dto.setDataAtendimento(historicoAtendimento.getData());
        dto.setValorPago(historicoAtendimento.getPagamento().getValor());
        dto.setFormaPagamento(historicoAtendimento.getPagamento().getFormaPagamento());
        dto.setNomeCliente(historicoAtendimento.getCliente().getNome());
        dto.setNomeBarbeiro(historicoAtendimento.getBarbeiro().getNome());
        dto.setNomeServico(historicoAtendimento.getServico().getNome());
        return dto;
    }

}
