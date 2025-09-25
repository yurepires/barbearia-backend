package com.ifma.barbearia.mappers;

import com.ifma.barbearia.DTOs.ClienteDto;
import com.ifma.barbearia.entities.Cliente;

public class ClienteMapper {

    public static ClienteDto mapToClienteDto(Cliente cliente, ClienteDto clienteDto) {
        clienteDto.setNome(cliente.getNome());
        clienteDto.setEmail(clienteDto.getEmail());
        clienteDto.setTelefone(clienteDto.getTelefone());
        return clienteDto;
    }

    public static Cliente mapToCliente(ClienteDto clienteDto, Cliente cliente) {
        cliente.setNome(clienteDto.getNome());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setTelefone(clienteDto.getTelefone());
        return cliente;
    }

}
