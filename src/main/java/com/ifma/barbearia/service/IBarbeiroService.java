package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.BarbeiroDto;
import com.ifma.barbearia.entity.Barbeiro;

import java.util.List;

public interface IBarbeiroService {

    void criarBarbeiro(BarbeiroDto BarbeiroDto);

    BarbeiroDto buscarBarbeiro(String email);

    /**
     * Busca a entidade Barbeiro por email (uso interno entre serviços).
     */
    Barbeiro buscarEntidadeBarbeiroPorEmail(String email);

    List<BarbeiroDto> buscarTodosBarbeiros();

    boolean atualizarBarbeiro(BarbeiroDto barbeiroDto);

    boolean deletarBarbeiro(String email);

}