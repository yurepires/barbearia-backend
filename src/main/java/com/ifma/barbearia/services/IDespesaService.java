package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.DespesaDto;

import java.util.List;

public interface IDespesaService {

    void criarDespesa(DespesaDto despesaDto);

    List<DespesaDto> buscarTodasDespesas();

}
