package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.*;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IClienteAuthService;
import com.ifma.barbearia.service.IClienteOtpService;
import com.ifma.barbearia.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteAuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("ClienteAuthController - Testes Unitários")
class ClienteAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IClienteOtpService clienteOtpService;

    @MockitoBean
    private IClienteAuthService clienteAuthService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Nested
    @DisplayName("POST /api/cliente/auth/gerarOtp")
    class GerarOtp {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK após enviar código OTP por email")
        void deveRetornar200AposEnviarOtp() throws Exception {
            OtpRequestDto request = new OtpRequestDto();
            request.setEmail("cliente@email.com");

            willDoNothing().given(clienteOtpService).enviarCodigoPorEmail(any(OtpRequestDto.class));

            mockMvc.perform(post("/api/cliente/auth/gerarOtp")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("OTP enviado para o email."));
        }
    }

    @Nested
    @DisplayName("POST /api/cliente/auth/validarOtp")
    class ValidarOtp {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados de sucesso da validação de OTP")
        void deveRetornar200ComDadosSucessoValidacao() throws Exception {
            OtpValidateDto validateDto = new OtpValidateDto();
            validateDto.setEmail("cliente@email.com");
            validateDto.setOtp("123456");

            OtpResponseDto response = new OtpResponseDto("jwt-token");

            given(clienteOtpService.validarCodigo(any(OtpValidateDto.class))).willReturn(response);

            mockMvc.perform(post("/api/cliente/auth/validarOtp")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validateDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token"));
        }
    }

    @Nested
    @DisplayName("POST /api/cliente/auth/login-senha")
    class LoginSenha {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados de autenticação de login por senha")
        void deveRetornar200ComDadosAutenticacao() throws Exception {
            AuthRequest authRequest = new AuthRequest();
            authRequest.setUsername("cliente@email.com");
            authRequest.setPassword("senha123");
            AuthResponse authResponse = new AuthResponse("jwt-token-login");

            given(clienteAuthService.autenticarComSenha(any(AuthRequest.class))).willReturn(authResponse);

            mockMvc.perform(post("/api/cliente/auth/login-senha")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token-login"));
        }
    }
}
