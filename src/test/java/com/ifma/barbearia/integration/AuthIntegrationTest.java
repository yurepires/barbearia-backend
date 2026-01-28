package com.ifma.barbearia.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.AuthRequest;
import com.ifma.barbearia.entity.AdmUser;
import com.ifma.barbearia.repository.AdmUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de integração para o fluxo de autenticação de admin.
 * Usa banco H2 em memória e contexto Spring completo.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdmUserRepository admUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        // Limpa e cria usuário admin de teste
        admUserRepository.deleteAll();

        AdmUser admin = new AdmUser();
        admin.setUsername("admin_teste");
        admin.setPassword(passwordEncoder.encode("senha123"));
        admin.setRole("ADM");
        admUserRepository.save(admin);

        authRequest = new AuthRequest();
        authRequest.setUsername("admin_teste");
        authRequest.setPassword("senha123");
    }

    @Test
    @DisplayName("Deve fazer login com sucesso e retornar token JWT")
    void deveFazerLoginComSucessoERetornarToken() throws Exception {
        mockMvc.perform(post("/api/admin/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Deve retornar 401 quando credenciais inválidas")
    void deveRetornar401QuandoCredenciaisInvalidas() throws Exception {
        authRequest.setPassword("senha_errada");

        mockMvc.perform(post("/api/admin/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 401 quando usuário não existe")
    void deveRetornar401QuandoUsuarioNaoExiste() throws Exception {
        authRequest.setUsername("usuario_inexistente");

        mockMvc.perform(post("/api/admin/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve retornar 401 quando usuário não tem role ADM")
    void deveRetornar401QuandoUsuarioNaoEhAdm() throws Exception {
        // Criar usuário sem papel ADM
        AdmUser cliente = new AdmUser();
        cliente.setUsername("cliente_teste");
        cliente.setPassword(passwordEncoder.encode("senha123"));
        cliente.setRole("CLIENTE");
        admUserRepository.save(cliente);

        authRequest.setUsername("cliente_teste");

        mockMvc.perform(post("/api/admin/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized());
    }
}
