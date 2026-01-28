package com.ifma.barbearia.mapper;

import com.ifma.barbearia.dto.ClienteDto;
import com.ifma.barbearia.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteDto toDto(Cliente cliente) {
        ClienteDto dto = new ClienteDto();
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        return dto;
    }

    public Cliente toEntity(ClienteDto dto) {
        Cliente cliente = new Cliente();
        return updateEntity(dto, cliente);
    }

    public Cliente updateEntity(ClienteDto dto, Cliente cliente) {
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        if (dto.getSenha() != null) {
            cliente.setSenha(dto.getSenha());
        }
        return cliente;
    }

}
