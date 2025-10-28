package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.ServicoDto;

import java.util.List;

public interface IServicoService {
    void criarServico(ServicoDto servicoDto);
    ServicoDto buscarServico(Long servicoId);
    List<ServicoDto> buscarTodosServicos();
    boolean atualizarServico(ServicoDto servicoDto);
    boolean deletarServico(Long servicoId);
}