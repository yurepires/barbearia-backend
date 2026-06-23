package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.BarbeiroDto;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IBarbeiroService;
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

@WebMvcTest(BarbeiroController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("BarbeiroController - Testes Unitários")
class BarbeiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IBarbeiroService iBarbeiroService;

    // Necessários para o SecurityConfig carregar no contexto @WebMvcTest
    @MockitoBean
    private JwtUtil jwtUtil;

    private BarbeiroDto barbeiroDto;

    @BeforeEach
    void setUp() {
        barbeiroDto = new BarbeiroDto();
        barbeiroDto.setNome("Carlos do Corte");
        barbeiroDto.setEmail("carlos@email.com");
        barbeiroDto.setTelefone("(86) 9 8888-8888");
        barbeiroDto.setEspecialidade("Corte e Barba");
    }

    @Nested
    @DisplayName("POST /api/barbeiro/criarBarbeiro")
    class CriarBarbeiro {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 201 CREATED quando barbeiro criado com sucesso")
        void deveRetornar201QuandoBarbeiroCriadoComSucesso() throws Exception {
            // given
            willDoNothing().given(iBarbeiroService).criarBarbeiro(any(BarbeiroDto.class));

            // when & then
            mockMvc.perform(post("/api/barbeiro/criarBarbeiro")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(barbeiroDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));
        }
    }

    @Nested
    @DisplayName("GET /api/barbeiro/buscarBarbeiro")
    class BuscarBarbeiro {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados do barbeiro quando encontrado")
        void deveRetornar200ComDadosDoBarbeiro() throws Exception {
            // given
            given(iBarbeiroService.buscarBarbeiro("carlos@email.com"))
                    .willReturn(barbeiroDto);

            // when & then
            mockMvc.perform(get("/api/barbeiro/buscarBarbeiro")
                            .param("email", "carlos@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Carlos do Corte"))
                    .andExpect(jsonPath("$.email").value("carlos@email.com"))
                    .andExpect(jsonPath("$.especialidade").value("Corte e Barba"));
        }
    }

    @Nested
    @DisplayName("GET /api/barbeiro/buscarTodosBarbeiros")
    class BuscarTodosBarbeiros {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de barbeiros")
        void deveRetornar200ComListaDeBarbeiros() throws Exception {
            // given
            given(iBarbeiroService.buscarTodosBarbeiros())
                    .willReturn(List.of(barbeiroDto));

            // when & then
            mockMvc.perform(get("/api/barbeiro/buscarTodosBarbeiros"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nome").value("Carlos do Corte"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista vazia quando não há barbeiros")
        void deveRetornar200ComListaVazia() throws Exception {
            // given
            given(iBarbeiroService.buscarTodosBarbeiros()).willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/barbeiro/buscarTodosBarbeiros"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("PUT /api/barbeiro/atualizarBarbeiro")
    class AtualizarBarbeiro {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 200 OK quando barbeiro atualizado com sucesso")
        void deveRetornar200QuandoBarbeiroAtualizadoComSucesso() throws Exception {
            // given
            given(iBarbeiroService.atualizarBarbeiro(any(BarbeiroDto.class))).willReturn(true);

            // when & then
            mockMvc.perform(put("/api/barbeiro/atualizarBarbeiro")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(barbeiroDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 417 quando atualização falhar")
        void deveRetornar417QuandoAtualizacaoFalhar() throws Exception {
            // given
            given(iBarbeiroService.atualizarBarbeiro(any(BarbeiroDto.class))).willReturn(false);

            // when & then
            mockMvc.perform(put("/api/barbeiro/atualizarBarbeiro")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(barbeiroDto)))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/barbeiro/deletarBarbeiro")
    class DeletarBarbeiro {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 200 OK quando barbeiro deletado com sucesso")
        void deveRetornar200QuandoBarbeiroDeletadoComSucesso() throws Exception {
            // given
            given(iBarbeiroService.deletarBarbeiro("carlos@email.com")).willReturn(true);

            // when & then
            mockMvc.perform(delete("/api/barbeiro/deletarBarbeiro")
                            .with(csrf())
                            .param("email", "carlos@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 417 quando exclusão falhar")
        void deveRetornar417QuandoExclusaoFalhar() throws Exception {
            // given
            given(iBarbeiroService.deletarBarbeiro("carlos@email.com")).willReturn(false);

            // when & then
            mockMvc.perform(delete("/api/barbeiro/deletarBarbeiro")
                            .with(csrf())
                            .param("email", "carlos@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }
    }
}
