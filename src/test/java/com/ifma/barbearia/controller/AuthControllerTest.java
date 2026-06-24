package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.AuthRequest;
import com.ifma.barbearia.entity.AdmUser;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IAdmUserService;
import com.ifma.barbearia.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("AuthController - Testes Unitários")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IAdmUserService admUserService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private AuthRequest authRequest;
    private AdmUser admUser;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
        authRequest.setUsername("admin");
        authRequest.setPassword("senha123");

        admUser = new AdmUser(1L, "admin", "encoded_password", "ADM");
    }

    @Nested
    @DisplayName("POST /api/admin/auth/login")
    class Login {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com token quando credenciais administrativas estão corretas")
        void deveRetornar200ComTokenQuandoCredenciaisCorretas() throws Exception {
            given(admUserService.findByUsername("admin")).willReturn(admUser);
            given(admUserService.passwordMatches("senha123", "encoded_password")).willReturn(true);
            given(jwtUtil.generateToken("admin", "ADM")).willReturn("jwt-token-admin");

            mockMvc.perform(post("/api/admin/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt-token-admin"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 401 UNAUTHORIZED quando usuário não existe")
        void deveRetornar401QuandoUsuarioNaoExiste() throws Exception {
            given(admUserService.findByUsername("admin")).willReturn(null);

            mockMvc.perform(post("/api/admin/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(content().string("Usuário ou senha inválidos"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 401 UNAUTHORIZED quando senha não bate")
        void deveRetornar401QuandoSenhaIncorreta() throws Exception {
            given(admUserService.findByUsername("admin")).willReturn(admUser);
            given(admUserService.passwordMatches("senha123", "encoded_password")).willReturn(false);

            mockMvc.perform(post("/api/admin/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 401 UNAUTHORIZED quando usuário não possui papel de ADM")
        void deveRetornar401QuandoUsuarioNaoAdm() throws Exception {
            AdmUser userComum = new AdmUser(1L, "admin", "encoded_password", "USER");

            given(admUserService.findByUsername("admin")).willReturn(userComum);
            given(admUserService.passwordMatches("senha123", "encoded_password")).willReturn(true);

            mockMvc.perform(post("/api/admin/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authRequest)))
                    .andExpect(status().isUnauthorized());
        }
    }
}
