package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.AuthRequest;
import com.ifma.barbearia.dto.AuthResponse;

/**
 * Serviço responsável pela autenticação de clientes via senha.
 */
public interface IClienteAuthService {

    /**
     * Autentica um cliente usando email e senha.
     * 
     * @param authRequest credenciais do cliente
     * @return token JWT se autenticação bem-sucedida
     */
    AuthResponse autenticarComSenha(AuthRequest authRequest);

}