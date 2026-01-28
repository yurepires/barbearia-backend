package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.DespesaDto;

import java.util.List;

public interface IDespesaService {

    void criarDespesa(DespesaDto despesaDto);

    List<DespesaDto> buscarTodasDespesas();

}
