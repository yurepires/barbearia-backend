package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.ServicoDto;
import com.ifma.barbearia.entity.Servico;

import java.util.List;

public interface IServicoService {

    void criarServico(ServicoDto servicoDto);

    ServicoDto buscarServico(Long servicoId);

    /**
     * Busca a entidade Servico por ID (uso interno entre serviços).
     */
    Servico buscarEntidadeServicoPorId(Long servicoId);

    List<ServicoDto> buscarTodosServicos();

    boolean atualizarServico(ServicoDto servicoDto);

    boolean deletarServico(Long servicoId);

}