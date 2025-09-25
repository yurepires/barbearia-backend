package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.ClienteDto;

public interface IClienteService {

    void criarCliente(ClienteDto clienteDto);

    ClienteDto buscarCliente(String email);

    boolean atualizarCliente(ClienteDto clienteDto);

    boolean deletarCliente(String email);

}
