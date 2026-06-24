package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.HistoricoAtendimentoDto;
import com.ifma.barbearia.entity.HistoricoAtendimento;
import com.ifma.barbearia.mapper.HistoricoAtendimentoMapper;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IHistoricoAtendimentoService;
import com.ifma.barbearia.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HistoricoAtendimentoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("HistoricoAtendimentoController - Testes Unitários")
class HistoricoAtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IHistoricoAtendimentoService iHistoricoAtendimentoService;

    @MockitoBean
    private HistoricoAtendimentoMapper historicoAtendimentoMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    private HistoricoAtendimento entity;
    private HistoricoAtendimentoDto dto;

    @BeforeEach
    void setUp() {
        entity = new HistoricoAtendimento();
        dto = new HistoricoAtendimentoDto();
        dto.setId(1L);
        dto.setNomeCliente("João");
        dto.setNomeBarbeiro("Carlos");
        dto.setNomeServico("Corte");
        dto.setValorPago(35.0);
        dto.setDataAtendimento(LocalDateTime.now());
    }

    @Nested
    @DisplayName("GET /api/historicoAtendimento/listarTodos")
    class ListarTodos {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de todos os históricos")
        void deveRetornar200ComListaDeTodosOsHistoricos() throws Exception {
            given(iHistoricoAtendimentoService.listarTodos()).willReturn(List.of(entity));
            given(historicoAtendimentoMapper.toDto(any(HistoricoAtendimento.class))).willReturn(dto);

            mockMvc.perform(get("/api/historicoAtendimento/listarTodos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nomeCliente").value("João"))
                    .andExpect(jsonPath("$[0].nomeBarbeiro").value("Carlos"))
                    .andExpect(jsonPath("$[0].nomeServico").value("Corte"));
        }
    }

    @Nested
    @DisplayName("GET /api/historicoAtendimento/listarPorCliente")
    class ListarPorCliente {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com histórico do cliente")
        void deveRetornar200ComHistoricoDoCliente() throws Exception {
            given(iHistoricoAtendimentoService.listarPorCliente("joao@email.com")).willReturn(List.of(entity));
            given(historicoAtendimentoMapper.toDto(any(HistoricoAtendimento.class))).willReturn(dto);

            mockMvc.perform(get("/api/historicoAtendimento/listarPorCliente")
                            .param("clienteEmail", "joao@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nomeCliente").value("João"));
        }
    }

    @Nested
    @DisplayName("GET /api/historicoAtendimento/listarPorBarbeiro")
    class ListarPorBarbeiro {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com histórico do barbeiro")
        void deveRetornar200ComHistoricoDoBarbeiro() throws Exception {
            given(iHistoricoAtendimentoService.listarPorBarbeiro("carlos@email.com")).willReturn(List.of(entity));
            given(historicoAtendimentoMapper.toDto(any(HistoricoAtendimento.class))).willReturn(dto);

            mockMvc.perform(get("/api/historicoAtendimento/listarPorBarbeiro")
                            .param("barbeiroEmail", "carlos@email.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nomeBarbeiro").value("Carlos"));
        }
    }

    @Nested
    @DisplayName("GET /api/historicoAtendimento/listarPorServico")
    class ListarPorServico {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com histórico do serviço")
        void deveRetornar200ComHistoricoDoServico() throws Exception {
            given(iHistoricoAtendimentoService.listarPorServico(10L)).willReturn(List.of(entity));
            given(historicoAtendimentoMapper.toDto(any(HistoricoAtendimento.class))).willReturn(dto);

            mockMvc.perform(get("/api/historicoAtendimento/listarPorServico")
                            .param("servicoId", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nomeServico").value("Corte"));
        }
    }

    @Nested
    @DisplayName("GET /api/historicoAtendimento/listarPorIntervaloDeData")
    class ListarPorIntervaloDeData {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com histórico no intervalo de datas")
        void deveRetornar200ComHistoricoNoIntervalo() throws Exception {
            LocalDate inicio = LocalDate.of(2026, 6, 1);
            LocalDate fim = LocalDate.of(2026, 6, 30);

            given(iHistoricoAtendimentoService.listarPorIntervaloDeDatas(inicio, fim)).willReturn(List.of(entity));
            given(historicoAtendimentoMapper.toDto(any(HistoricoAtendimento.class))).willReturn(dto);

            mockMvc.perform(get("/api/historicoAtendimento/listarPorIntervaloDeData")
                            .param("inicio", "2026-06-01")
                            .param("fim", "2026-06-30"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nomeCliente").value("João"));
        }
    }
}
