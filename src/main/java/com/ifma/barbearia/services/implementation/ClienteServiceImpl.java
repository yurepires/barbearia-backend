package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.ClienteDto;
import com.ifma.barbearia.entities.Cliente;
import com.ifma.barbearia.exceptions.ClienteAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mappers.ClienteMapper;
import com.ifma.barbearia.repositories.ClienteRepository;
import com.ifma.barbearia.services.IClienteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements IClienteService {

    private ClienteRepository clienteRepository;

    @Override
    public void criarCliente(ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.mapToCliente(clienteDto, new Cliente());
        Optional<Cliente> optionalCliente = clienteRepository.findByEmail(cliente.getEmail());
        if (optionalCliente.isPresent()) {
            throw new ClienteAlreadyExistsException("Cliente já registrado com esse email: " + clienteDto.getEmail());
        }
        clienteRepository.save(cliente);
    }

    @Override
    public ClienteDto buscarCliente(String email) {
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", email));
        return ClienteMapper.mapToClienteDto(cliente, new ClienteDto());
    }

    @Override
    public List<ClienteDto> buscarTodosClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<ClienteDto> clienteDtos = new ArrayList<>();
        for (Cliente cliente : clientes) {
            clienteDtos.add(ClienteMapper.mapToClienteDto(cliente, new ClienteDto()));
        }
        return clienteDtos;
    }

    @Override
    public boolean atualizarCliente(ClienteDto clienteDto) {
        Cliente cliente = clienteRepository.findByEmail(clienteDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", clienteDto.getEmail()));
        ClienteMapper.mapToCliente(clienteDto, cliente);
        clienteRepository.save(cliente);
        return true;
    }

    @Override
    public boolean deletarCliente(String email) {
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", email));
        clienteRepository.delete(cliente);
        return true;
    }
}
