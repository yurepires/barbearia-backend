package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.ClienteDto;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.context.annotation.Import;
import com.ifma.barbearia.config.SecurityConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("ClienteController - Testes Unitários")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IClienteService iClienteService;

    // Necessários para o SecurityConfig carregar no contexto @WebMvcTest
    @MockitoBean
    private JwtUtil jwtUtil;

    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        clienteDto = new ClienteDto();
        clienteDto.setNome("João Maria");
        clienteDto.setEmail("joao@email.com");
        clienteDto.setTelefone("(86) 9 9999-9999");
        clienteDto.setSenha("minhasenha");
    }

    @Nested
    @DisplayName("POST /api/cliente/criarCliente")
    class CriarCliente {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 201 CREATED quando cliente criado com sucesso")
        void deveRetornar201QuandoClienteCriadoComSucesso() throws Exception {
            // given
            willDoNothing().given(iClienteService).criarCliente(any(ClienteDto.class));

            // when & then
            mockMvc.perform(post("/api/cliente/criarCliente")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));
        }
    }

    @Nested
    @DisplayName("GET /api/cliente/buscarCliente")
    class BuscarCliente {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados do cliente quando encontrado")
        void deveRetornar200ComDadosDoCliente() throws Exception {
            // given
            given(iClienteService.buscarCliente("joao@email.com"))
                    .willReturn(clienteDto);

            // when & then
            mockMvc.perform(get("/api/cliente/buscarCliente")
                            .param("email", "joao@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("João Maria"))
                    .andExpect(jsonPath("$.email").value("joao@email.com"));
        }
    }

    @Nested
    @DisplayName("GET /api/cliente/buscarTodosClientes")
    class BuscarTodosClientes {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de clientes")
        void deveRetornar200ComListaDeClientes() throws Exception {
            // given
            given(iClienteService.buscarTodosClientes())
                    .willReturn(List.of(clienteDto));

            // when & then
            mockMvc.perform(get("/api/cliente/buscarTodosClientes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nome").value("João Maria"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista vazia quando não há clientes")
        void deveRetornar200ComListaVazia() throws Exception {
            // given
            given(iClienteService.buscarTodosClientes()).willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/cliente/buscarTodosClientes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("PUT /api/cliente/atualizarCliente")
    class AtualizarCliente {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 200 OK quando cliente atualizado com sucesso")
        void deveRetornar200QuandoClienteAtualizadoComSucesso() throws Exception {
            // given
            given(iClienteService.atualizarCliente(any(ClienteDto.class))).willReturn(true);

            // when & then
            mockMvc.perform(put("/api/cliente/atualizarCliente")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 417 quando atualização falhar")
        void deveRetornar417QuandoAtualizacaoFalhar() throws Exception {
            // given
            given(iClienteService.atualizarCliente(any(ClienteDto.class))).willReturn(false);

            // when & then
            mockMvc.perform(put("/api/cliente/atualizarCliente")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(clienteDto)))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/cliente/deletarCliente")
    class DeletarCliente {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 200 OK quando cliente deletado com sucesso")
        void deveRetornar200QuandoClienteDeletadoComSucesso() throws Exception {
            // given
            given(iClienteService.deletarCliente("joao@email.com")).willReturn(true);

            // when & then
            mockMvc.perform(delete("/api/cliente/deletarCliente")
                            .with(csrf())
                            .param("email", "joao@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }
    }
}
