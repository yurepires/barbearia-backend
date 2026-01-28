package com.ifma.barbearia.mapper;

import com.ifma.barbearia.dto.DespesaDto;
import com.ifma.barbearia.entity.Despesa;
import org.springframework.stereotype.Component;

@Component
public class DespesaMapper {

    public DespesaDto toDto(Despesa despesa) {
        DespesaDto dto = new DespesaDto();
        dto.setDespesaId(despesa.getDespesaId());
        dto.setDescricao(despesa.getDescricao());
        dto.setValor(despesa.getValor());
        dto.setDataDespesa(despesa.getDataDespesa());
        return dto;
    }

    public Despesa toEntity(DespesaDto dto) {
        Despesa despesa = new Despesa();
        return updateEntity(dto, despesa);
    }

    public Despesa updateEntity(DespesaDto dto, Despesa despesa) {
        despesa.setDespesaId(dto.getDespesaId());
        despesa.setDescricao(dto.getDescricao());
        despesa.setValor(dto.getValor());
        despesa.setDataDespesa(dto.getDataDespesa());
        return despesa;
    }

}
