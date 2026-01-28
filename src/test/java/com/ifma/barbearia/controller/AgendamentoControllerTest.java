package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.exceptions.GlobalExceptionHandler;
import com.ifma.barbearia.exceptions.HorarioIndisponivelException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.service.IAgendamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de controller para AgendamentoController.
 * Usa MockMvc standalone para teste isolado sem carregar contexto Spring.
 */
@ExtendWith(MockitoExtension.class)
class AgendamentoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IAgendamentoService agendamentoService;

    @InjectMocks
    private AgendamentoController agendamentoController;

    private ObjectMapper objectMapper;
    private AgendamentoDto agendamentoDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(agendamentoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        agendamentoDto = new AgendamentoDto();
        agendamentoDto.setId(1L);
        agendamentoDto.setClienteEmail("cliente@teste.com");
        agendamentoDto.setBarbeiroEmail("barbeiro@teste.com");
        agendamentoDto.setServicoId(1L);
        agendamentoDto.setHorario(LocalDateTime.of(2026, 2, 1, 10, 0));
    }

    @Nested
    @DisplayName("POST /api/agendamento/criarAgendamento")
    class CriarAgendamentoTests {

        @Test
        @DisplayName("Deve criar agendamento com sucesso")
        void deveCriarAgendamentoComSucesso() throws Exception {
            doNothing().when(agendamentoService).criarAgendamento(any(AgendamentoDto.class));

            mockMvc.perform(post("/api/agendamento/criarAgendamento")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(agendamentoDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));

            verify(agendamentoService).criarAgendamento(any(AgendamentoDto.class));
        }

        @Test
        @DisplayName("Deve retornar erro quando barbeiro indisponível")
        void deveRetornarErroQuandoBarbeiroIndisponivel() throws Exception {
            doThrow(new HorarioIndisponivelException("Barbeiro ocupado"))
                    .when(agendamentoService).criarAgendamento(any(AgendamentoDto.class));

            mockMvc.perform(post("/api/agendamento/criarAgendamento")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(agendamentoDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/agendamento/buscarAgendamento")
    class BuscarAgendamentoTests {

        @Test
        @DisplayName("Deve buscar agendamento pelo ID")
        void deveBuscarAgendamentoPeloId() throws Exception {
            when(agendamentoService.buscarAgendamento(1L)).thenReturn(agendamentoDto);

            mockMvc.perform(get("/api/agendamento/buscarAgendamento")
                    .param("agendamentoId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.clienteEmail").value("cliente@teste.com"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando agendamento não encontrado")
        void deveRetornar404QuandoAgendamentoNaoEncontrado() throws Exception {
            when(agendamentoService.buscarAgendamento(999L))
                    .thenThrow(new ResourceNotFoundException("Agendamento", "id", "999"));

            mockMvc.perform(get("/api/agendamento/buscarAgendamento")
                    .param("agendamentoId", "999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/agendamento/buscarTodosAgendamentos")
    class BuscarTodosAgendamentosTests {

        @Test
        @DisplayName("Deve buscar todos os agendamentos")
        void deveBuscarTodosAgendamentos() throws Exception {
            AgendamentoDto dto2 = new AgendamentoDto();
            dto2.setId(2L);
            dto2.setClienteEmail("outro@teste.com");

            when(agendamentoService.buscarTodosAgendamentos()).thenReturn(List.of(agendamentoDto, dto2));

            mockMvc.perform(get("/api/agendamento/buscarTodosAgendamentos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("PATCH /api/agendamento/cancelarAgendamento")
    class CancelarAgendamentoTests {

        @Test
        @DisplayName("Deve cancelar agendamento com sucesso")
        void deveCancelarAgendamentoComSucesso() throws Exception {
            when(agendamentoService.cancelarAgendamento(1L)).thenReturn(true);

            mockMvc.perform(patch("/api/agendamento/cancelarAgendamento")
                    .param("agendamentoId", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }
    }

    @Nested
    @DisplayName("PATCH /api/agendamento/concluirAgendamento")
    class ConcluirAgendamentoTests {

        @Test
        @DisplayName("Deve concluir agendamento com sucesso")
        void deveConcluirAgendamentoComSucesso() throws Exception {
            doNothing().when(agendamentoService).concluirAgendamento(1L, "PIX");

            mockMvc.perform(patch("/api/agendamento/concluirAgendamento")
                    .param("agendamentoId", "1")
                    .param("formaPagamento", "PIX"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }
    }
}
