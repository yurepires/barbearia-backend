package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.entity.enums.StatusAgendamento;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IAgendamentoService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgendamentoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("AgendamentoController - Testes Unitários")
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IAgendamentoService agendamentoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private AgendamentoDto agendamentoDto;

    @BeforeEach
    void setUp() {
        agendamentoDto = new AgendamentoDto();
        agendamentoDto.setId(1L);
        agendamentoDto.setHorario(LocalDateTime.now().plusDays(1)); // future date
        agendamentoDto.setStatus(StatusAgendamento.PENDENTE);
        agendamentoDto.setClienteEmail("cliente@email.com");
        agendamentoDto.setClienteNome("João");
        agendamentoDto.setServicoId(2L);
        agendamentoDto.setServicoNome("Corte");
        agendamentoDto.setBarbeiroEmail("barbeiro@email.com");
        agendamentoDto.setBarbeiroNome("Carlos");
        agendamentoDto.setBarbeiroEspecialidade("Cabelo");
    }

    @Nested
    @DisplayName("POST /api/agendamento/criarAgendamento")
    class CriarAgendamento {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 201 CREATED ao criar agendamento com sucesso")
        void deveRetornar201AoCriarAgendamentoComSucesso() throws Exception {
            willDoNothing().given(agendamentoService).criarAgendamento(any(AgendamentoDto.class));

            mockMvc.perform(post("/api/agendamento/criarAgendamento")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agendamentoDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));
        }
    }

    @Nested
    @DisplayName("GET /api/agendamento/buscarAgendamento")
    class BuscarAgendamento {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK ao buscar agendamento existente")
        void deveRetornar200AoBuscarAgendamentoExistente() throws Exception {
            given(agendamentoService.buscarAgendamento(1L)).willReturn(agendamentoDto);

            mockMvc.perform(get("/api/agendamento/buscarAgendamento")
                            .param("agendamentoId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.clienteEmail").value("cliente@email.com"));
        }
    }

    @Nested
    @DisplayName("GET /api/agendamento/buscarTodosAgendamentos")
    class BuscarTodosAgendamentos {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de todos os agendamentos")
        void deveRetornar200ComListaDeTodosOsAgendamentos() throws Exception {
            given(agendamentoService.buscarTodosAgendamentos()).willReturn(List.of(agendamentoDto));

            mockMvc.perform(get("/api/agendamento/buscarTodosAgendamentos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1));
        }
    }

    @Nested
    @DisplayName("GET /api/agendamento/buscarPorCliente")
    class BuscarPorCliente {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de agendamentos do cliente")
        void deveRetornar200ComListaDeAgendamentosDoCliente() throws Exception {
            given(agendamentoService.buscarAgendamentosPorCliente("cliente@email.com")).willReturn(List.of(agendamentoDto));

            mockMvc.perform(get("/api/agendamento/buscarPorCliente")
                            .param("clienteEmail", "cliente@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].clienteEmail").value("cliente@email.com"));
        }
    }

    @Nested
    @DisplayName("GET /api/agendamento/buscarPorIntervaloDeDatas")
    class BuscarPorIntervaloDeDatas {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de agendamentos no intervalo")
        void deveRetornar200ComListaDeAgendamentosNoIntervalo() throws Exception {
            LocalDate inicio = LocalDate.now();
            LocalDate fim = LocalDate.now().plusDays(2);

            given(agendamentoService.buscarAgendamentosPorIntervaloDeDatas(inicio, fim)).willReturn(List.of(agendamentoDto));

            mockMvc.perform(get("/api/agendamento/buscarPorIntervaloDeDatas")
                            .param("inicio", inicio.toString())
                            .param("fim", fim.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1));
        }
    }

    @Nested
    @DisplayName("PUT /api/agendamento/atualizarAgendamento")
    class AtualizarAgendamento {

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 200 OK quando atualizar agendamento com sucesso")
        void deveRetornar200AoAtualizarComSucesso() throws Exception {
            given(agendamentoService.atualizarAgendamento(any(AgendamentoDto.class))).willReturn(true);

            mockMvc.perform(put("/api/agendamento/atualizarAgendamento")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agendamentoDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        @WithMockUser(authorities = "ADM")
        @DisplayName("Deve retornar 417 quando atualização falhar")
        void deveRetornar417AoAtualizarComFalha() throws Exception {
            given(agendamentoService.atualizarAgendamento(any(AgendamentoDto.class))).willReturn(false);

            mockMvc.perform(put("/api/agendamento/atualizarAgendamento")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(agendamentoDto)))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }
    }

    @Nested
    @DisplayName("PATCH /api/agendamento/cancelarAgendamento")
    class CancelarAgendamento {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK quando cancelar agendamento com sucesso")
        void deveRetornar200AoCancelarComSucesso() throws Exception {
            given(agendamentoService.cancelarAgendamento(1L)).willReturn(true);

            mockMvc.perform(patch("/api/agendamento/cancelarAgendamento")
                            .with(csrf())
                            .param("agendamentoId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 417 quando cancelamento falhar")
        void deveRetornar417AoCancelarComFalha() throws Exception {
            given(agendamentoService.cancelarAgendamento(1L)).willReturn(false);

            mockMvc.perform(patch("/api/agendamento/cancelarAgendamento")
                            .with(csrf())
                            .param("agendamentoId", "1"))
                    .andExpect(status().isExpectationFailed())
                    .andExpect(jsonPath("$.statusCode").value("417"));
        }
    }

    @Nested
    @DisplayName("PATCH /api/agendamento/concluirAgendamento")
    class ConcluirAgendamento {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK quando concluir agendamento com sucesso")
        void deveRetornar200AoConcluirComSucesso() throws Exception {
            willDoNothing().given(agendamentoService).concluirAgendamento(1L, "PIX");

            mockMvc.perform(patch("/api/agendamento/concluirAgendamento")
                            .with(csrf())
                            .param("agendamentoId", "1")
                            .param("formaPagamento", "PIX"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }
    }
}
