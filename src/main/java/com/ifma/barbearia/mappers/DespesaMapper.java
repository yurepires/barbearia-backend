package com.ifma.barbearia.mappers;

import com.ifma.barbearia.DTOs.DespesaDto;
import com.ifma.barbearia.entities.Despesa;

public class DespesaMapper {

    public static DespesaDto mapToDto(Despesa despesa, DespesaDto despesaDto) {
        despesaDto.setDespesaId(despesa.getDespesaId());
        despesaDto.setDescricao(despesa.getDescricao());
        despesaDto.setValor(despesa.getValor());
        despesaDto.setDataDespesa(despesa.getDataDespesa());
        return despesaDto;
    }

    public static Despesa mapToEntity(DespesaDto despesaDto, Despesa despesa) {
        despesa.setDespesaId(despesaDto.getDespesaId());
        despesa.setDescricao(despesaDto.getDescricao());
        despesa.setValor(despesaDto.getValor());
        despesa.setDataDespesa(despesaDto.getDataDespesa());
        return despesa;
    }

}
