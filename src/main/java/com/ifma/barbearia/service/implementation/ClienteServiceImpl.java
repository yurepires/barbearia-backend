package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ClienteDto;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.exceptions.ClienteAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.ClienteMapper;
import com.ifma.barbearia.repository.ClienteRepository;
import com.ifma.barbearia.service.IClienteService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements IClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteMapper clienteMapper;

    @Override
    public void criarCliente(ClienteDto clienteDto) {
        Optional<Cliente> optionalCliente = clienteRepository.findByEmail(clienteDto.getEmail());
        if (optionalCliente.isPresent()) {
            throw new ClienteAlreadyExistsException("Cliente já registrado com esse email: " + clienteDto.getEmail());
        }
        Cliente cliente = clienteMapper.toEntity(clienteDto);
        if (cliente.getSenha() != null) {
            cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
        }
        clienteRepository.save(cliente);
    }

    @Override
    public ClienteDto buscarCliente(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", email));
        return clienteMapper.toDto(cliente);
    }

    @Override
    public Cliente buscarEntidadeClientePorEmail(String email) {
        return clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", email));
    }

    @Override
    public List<ClienteDto> buscarTodosClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDto)
                .toList();
    }

    @Override
    public boolean atualizarCliente(ClienteDto clienteDto) {
        Cliente cliente = clienteRepository.findByEmail(clienteDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", clienteDto.getEmail()));
        clienteMapper.updateEntity(clienteDto, cliente);
        clienteRepository.save(cliente);
        return true;
    }

    @Override
    public boolean deletarCliente(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", email));
        clienteRepository.delete(cliente);
        return true;
    }

}
