package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.ResumoFinanceiroDto;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IFinanceiroService;
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

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinanceiroController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("FinanceiroController - Testes Unitários")
class FinanceiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IFinanceiroService iFinanceiroService;

    @MockitoBean
    private JwtUtil jwtUtil;

    private ResumoFinanceiroDto resumoFinanceiroDto;

    @BeforeEach
    void setUp() {
        resumoFinanceiroDto = new ResumoFinanceiroDto();
        resumoFinanceiroDto.setTotalEntradas(1500.0);
        resumoFinanceiroDto.setTotalSaidas(500.0);
        resumoFinanceiroDto.setSaldo(1000.0);
        resumoFinanceiroDto.setDataInicio(LocalDate.of(2026, 6, 1));
        resumoFinanceiroDto.setDataFim(LocalDate.of(2026, 6, 30));
    }

    @Nested
    @DisplayName("GET /api/financeiro/resumo")
    class ObterResumo {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com resumo financeiro para datas válidas")
        void deveRetornar200ComResumoParaDatasValidas() throws Exception {
            LocalDate inicio = LocalDate.of(2026, 6, 1);
            LocalDate fim = LocalDate.of(2026, 6, 30);

            given(iFinanceiroService.obterResumo(inicio, fim)).willReturn(resumoFinanceiroDto);

            mockMvc.perform(get("/api/financeiro/resumo")
                            .param("inicio", "2026-06-01")
                            .param("fim", "2026-06-30"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalEntradas").value(1500.0))
                    .andExpect(jsonPath("$.totalSaidas").value(500.0))
                    .andExpect(jsonPath("$.saldo").value(1000.0));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 400 BAD REQUEST quando a data final for anterior à data inicial")
        void deveRetornar400QuandoDataFimAntesDeInicio() throws Exception {
            mockMvc.perform(get("/api/financeiro/resumo")
                            .param("inicio", "2026-06-30")
                            .param("fim", "2026-06-01"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").exists());
        }
    }
}
