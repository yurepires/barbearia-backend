package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.ClienteDto;
import com.ifma.barbearia.entity.Cliente;

import java.util.List;

public interface IClienteService {

    void criarCliente(ClienteDto clienteDto);

    ClienteDto buscarCliente(String email);

    /**
     * Busca a entidade Cliente por email (uso interno entre serviços).
     */
    Cliente buscarEntidadeClientePorEmail(String email);

    List<ClienteDto> buscarTodosClientes();

    boolean atualizarCliente(ClienteDto clienteDto);

    boolean deletarCliente(String email);

}