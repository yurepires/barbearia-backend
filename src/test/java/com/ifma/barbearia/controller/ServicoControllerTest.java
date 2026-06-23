package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.ServicoDto;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IServicoService;
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

@WebMvcTest(ServicoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("ServicoController - Testes Unitários")
class ServicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IServicoService iServicoService;

    // Necessários para o SecurityConfig carregar no contexto @WebMvcTest
    @MockitoBean
    private JwtUtil jwtUtil;

    private ServicoDto servicoDto;

    @BeforeEach
    void setUp() {
        servicoDto = new ServicoDto();
        servicoDto.setServicoId(1L);
        servicoDto.setNome("Corte degradê");
        servicoDto.setPreco(35.0);
        servicoDto.setDescricao("Corte degradê na régua máxima.");
    }

    @Nested
    @DisplayName("POST /api/servico/criarServico")
    class CriarServico {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 201 CREATED quando serviço criado com sucesso")
        void deveRetornar201QuandoServicoCriadoComSucesso() throws Exception {
            // given
            willDoNothing().given(iServicoService).criarServico(any(ServicoDto.class));

            // when & then
            mockMvc.perform(post("/api/servico/criarServico")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(servicoDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));
        }
    }

    @Nested
    @DisplayName("GET /api/servico/buscarServico")
    class BuscarServico {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados do serviço quando encontrado por ID")
        void deveRetornar200ComDadosDoServico() throws Exception {
            // given
            given(iServicoService.buscarServico(1L)).willReturn(servicoDto);

            // when & then
            mockMvc.perform(get("/api/servico/buscarServico")
                            .param("servicoId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Corte degradê"))
                    .andExpect(jsonPath("$.preco").value(35.0));
        }
    }

    @Nested
    @DisplayName("GET /api/servico/buscarTodosServicos")
    class BuscarTodosServicos {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de serviços")
        void deveRetornar200ComListaDeServicos() throws Exception {
            // given
            given(iServicoService.buscarTodosServicos()).willReturn(List.of(servicoDto));

            // when & then
            mockMvc.perform(get("/api/servico/buscarTodosServicos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nome").value("Corte degradê"))
                    .andExpect(jsonPath("$[0].preco").value(35.0));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista vazia quando não há serviços")
        void deveRetornar200ComListaVazia() throws Exception {
            // given
            given(iServicoService.buscarTodosServicos()).willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/servico/buscarTodosServicos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("PUT /api/servico/atualizarServico")
    class AtualizarServico {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 200 OK quando serviço atualizado com sucesso")
        void deveRetornar200QuandoServicoAtualizadoComSucesso() throws Exception {
            // given
            given(iServicoService.atualizarServico(any(ServicoDto.class))).willReturn(true);

            // when & then
            mockMvc.perform(put("/api/servico/atualizarServico")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(servicoDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 417 quando atualização falhar")
        void deveRetornar417QuandoAtualizacaoFalhar() throws Exception {
            // given
            given(iServicoService.atualizarServico(any(ServicoDto.class))).willReturn(false);

            // when & then
            mockMvc.perform(put("/api/servico/atualizarServico")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(servicoDto)))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/servico/deletarServico")
    class DeletarServico {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 200 OK quando serviço deletado com sucesso")
        void deveRetornar200QuandoServicoDeletadoComSucesso() throws Exception {
            // given
            given(iServicoService.deletarServico(1L)).willReturn(true);

            // when & then
            mockMvc.perform(delete("/api/servico/deletarServico")
                            .with(csrf())
                            .param("servicoId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 417 quando exclusão falhar")
        void deveRetornar417QuandoExclusaoFalhar() throws Exception {
            // given
            given(iServicoService.deletarServico(1L)).willReturn(false);

            // when & then
            mockMvc.perform(delete("/api/servico/deletarServico")
                            .with(csrf())
                            .param("servicoId", "1"))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }
    }
}
