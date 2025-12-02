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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.ifma.barbearia.DTOs.AuthRequest;
import com.ifma.barbearia.DTOs.AuthResponse;
import com.ifma.barbearia.security.JwtUtil;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClienteServiceImpl implements IClienteService {

    private ClienteRepository clienteRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void criarCliente(ClienteDto clienteDto) {
        Cliente cliente = ClienteMapper.mapToCliente(clienteDto, new Cliente());
        Optional<Cliente> optionalCliente = clienteRepository.findByEmail(cliente.getEmail());
        if (optionalCliente.isPresent()) {
            throw new ClienteAlreadyExistsException("Cliente já registrado com esse email: " + clienteDto.getEmail());
        }
        if (cliente.getSenha() != null) {
            cliente.setSenha(passwordEncoder.encode(cliente.getSenha()));
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
        return clienteRepository.findAll().stream()
                .map(cliente -> ClienteMapper.mapToClienteDto(cliente, new ClienteDto()))
                .toList();
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

    @Override
    public AuthResponse autenticarComSenha(AuthRequest authRequest) {
        Cliente cliente = clienteRepository.findByEmail(authRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", authRequest.getUsername()));

        if (cliente.getSenha() == null || !passwordEncoder.matches(authRequest.getPassword(), cliente.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = JwtUtil.generateToken(cliente.getEmail(), "CLIENTE");
        return new AuthResponse(token);
    }
}
