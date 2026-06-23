package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.DespesaDto;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IDespesaService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DespesaController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("DespesaController - Testes Unitários")
class DespesaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IDespesaService iDespesaService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private DespesaDto despesaDto;

    @BeforeEach
    void setUp() {
        despesaDto = new DespesaDto();
        despesaDto.setDespesaId(1L);
        despesaDto.setDescricao("Energia Elétrica");
        despesaDto.setValor(350.0);
        despesaDto.setDataDespesa(LocalDate.now());
    }

    @Nested
    @DisplayName("POST /api/despesas")
    class RegistrarDespesa {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 201 CREATED quando despesa registrada com sucesso")
        void deveRetornar201QuandoDespesaRegistradaComSucesso() throws Exception {
            willDoNothing().given(iDespesaService).criarDespesa(any(DespesaDto.class));

            mockMvc.perform(post("/api/despesas")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(despesaDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));
        }
    }

    @Nested
    @DisplayName("GET /api/despesas")
    class ListarTodasDespesas {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista de despesas")
        void deveRetornar200ComListaDeDespesas() throws Exception {
            given(iDespesaService.buscarTodasDespesas()).willReturn(List.of(despesaDto));

            mockMvc.perform(get("/api/despesas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].descricao").value("Energia Elétrica"))
                    .andExpect(jsonPath("$[0].valor").value(350.0));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com lista vazia quando não há despesas")
        void deveRetornar200ComListaVazia() throws Exception {
            given(iDespesaService.buscarTodasDespesas()).willReturn(List.of());

            mockMvc.perform(get("/api/despesas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }
}
