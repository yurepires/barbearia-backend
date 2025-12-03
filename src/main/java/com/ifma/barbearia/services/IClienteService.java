package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.ClienteDto;
import com.ifma.barbearia.DTOs.AuthRequest;
import com.ifma.barbearia.DTOs.AuthResponse;
import java.util.List;

public interface IClienteService {

    void criarCliente(ClienteDto clienteDto);

    ClienteDto buscarCliente(String email);

    List<ClienteDto> buscarTodosClientes();

    boolean atualizarCliente(ClienteDto clienteDto);

    boolean deletarCliente(String email);

    AuthResponse autenticarComSenha(AuthRequest authRequest);
}
