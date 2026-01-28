package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.AuthRequest;
import com.ifma.barbearia.dto.AuthResponse;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.repository.ClienteRepository;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IClienteAuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de autenticação de clientes via senha.
 */
@Service
@AllArgsConstructor
public class ClienteAuthServiceImpl implements IClienteAuthService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse autenticarComSenha(AuthRequest authRequest) {
        Cliente cliente = clienteRepository.findByEmail(authRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", "email", authRequest.getUsername()));

        if (cliente.getSenha() == null || !passwordEncoder.matches(authRequest.getPassword(), cliente.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtUtil.generateToken(cliente.getEmail(), "CLIENTE");
        return new AuthResponse(token);
    }

}
