package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.ServicoDto;

public interface IServicoService {
    void criarServico(ServicoDto servicoDto);
    ServicoDto buscarServico(Long servicoId);
    boolean atualizarServico(ServicoDto servicoDto);
    boolean deletarServico(Long servicoId);
}