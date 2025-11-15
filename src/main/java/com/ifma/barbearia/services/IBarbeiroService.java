package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.BarbeiroDto;
import java.util.List;

public interface IBarbeiroService {

    void criarBarbeiro(BarbeiroDto BarbeiroDto);

    BarbeiroDto buscarBarbeiro(String email);

    List<BarbeiroDto> buscarTodosBarbeiros();

    boolean atualizarBarbeiro(BarbeiroDto barbeiroDto);

    boolean deletarBarbeiro(String email);

}
