package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IPagamentoService;
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

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagamentoController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("PagamentoController - Testes Unitários")
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IPagamentoService iPagamentoService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private PagamentoDto pagamentoDto;

    @BeforeEach
    void setUp() {
        pagamentoDto = new PagamentoDto();
        pagamentoDto.setPagamentoId(1L);
        pagamentoDto.setAgendamentoId(10L);
        pagamentoDto.setValor(50.0);
        pagamentoDto.setFormaPagamento("PIX");
        pagamentoDto.setDataPagamento(LocalDateTime.now());
    }

    @Nested
    @DisplayName("GET /api/pagamentos/{pagamentoId}")
    class BuscarPagamento {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados do pagamento quando encontrado")
        void deveRetornar200ComDadosDoPagamento() throws Exception {
            given(iPagamentoService.buscarPagamento(1L)).willReturn(pagamentoDto);

            mockMvc.perform(get("/api/pagamentos/{pagamentoId}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pagamentoId").value(1))
                    .andExpect(jsonPath("$.agendamentoId").value(10))
                    .andExpect(jsonPath("$.valor").value(50.0))
                    .andExpect(jsonPath("$.formaPagamento").value("PIX"));
        }
    }

    @Nested
    @DisplayName("GET /api/pagamentos/agendamento/{agendamentoId}")
    class BuscarPagamentoPorAgendamento {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados do pagamento quando encontrado por agendamentoId")
        void deveRetornar200ComDadosDoPagamentoPorAgendamento() throws Exception {
            given(iPagamentoService.buscarPagamentoPorAgendamento(10L)).willReturn(pagamentoDto);

            mockMvc.perform(get("/api/pagamentos/agendamento/{agendamentoId}", 10L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pagamentoId").value(1))
                    .andExpect(jsonPath("$.agendamentoId").value(10))
                    .andExpect(jsonPath("$.valor").value(50.0));
        }
    }
}
