package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.AuthRequest;
import com.ifma.barbearia.entity.AdmUser;
import com.ifma.barbearia.exceptions.GlobalExceptionHandler;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IAdmUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de controller para AuthController.
 * Testa login de administrador.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IAdmUserService admUserService;

    @Mock
    private JwtUtil jwtUtil;

    private AuthController authController;

    private ObjectMapper objectMapper;
    private AuthRequest authRequest;
    private AdmUser admUser;

    @BeforeEach
    void setUp() {
        authController = new AuthController(admUserService, jwtUtil);

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        authRequest = new AuthRequest();
        authRequest.setUsername("admin");
        authRequest.setPassword("senha123");

        admUser = new AdmUser();
        admUser.setUsername("admin");
        admUser.setPassword("$2a$10$hashedPassword");
        admUser.setRole("ADM");
    }

    @Nested
    @DisplayName("POST /api/admin/auth/login")
    class LoginTests {

        @Test
        @DisplayName("Deve fazer login com sucesso")
        void deveFazerLoginComSucesso() throws Exception {
            when(admUserService.findByUsername("admin")).thenReturn(admUser);
            when(admUserService.passwordMatches("senha123", admUser.getPassword())).thenReturn(true);
            when(jwtUtil.generateToken("admin", "ADM")).thenReturn("jwt.token.gerado");

            mockMvc.perform(post("/api/admin/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt.token.gerado"));
        }

        @Test
        @DisplayName("Deve retornar 401 quando usuário não encontrado")
        void deveRetornar401QuandoUsuarioNaoEncontrado() throws Exception {
            when(admUserService.findByUsername("admin")).thenReturn(null);

            mockMvc.perform(post("/api/admin/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Deve retornar 401 quando senha incorreta")
        void deveRetornar401QuandoSenhaIncorreta() throws Exception {
            when(admUserService.findByUsername("admin")).thenReturn(admUser);
            when(admUserService.passwordMatches("senha123", admUser.getPassword())).thenReturn(false);

            mockMvc.perform(post("/api/admin/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Deve retornar 401 quando usuário não é ADM")
        void deveRetornar401QuandoUsuarioNaoEhAdm() throws Exception {
            admUser.setRole("CLIENTE");
            when(admUserService.findByUsername("admin")).thenReturn(admUser);
            when(admUserService.passwordMatches("senha123", admUser.getPassword())).thenReturn(true);

            mockMvc.perform(post("/api/admin/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized());
        }
    }
}
