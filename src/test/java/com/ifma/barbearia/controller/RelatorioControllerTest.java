package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.RelatorioClienteMaisFrequenteDto;
import com.ifma.barbearia.dto.RelatorioDto;
import com.ifma.barbearia.dto.RelatorioServicoMaisVendidoDto;
import com.ifma.barbearia.security.JwtAuthenticationFilter;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.IRelatorioService;
import com.ifma.barbearia.config.SecurityConfig;
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
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RelatorioController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
@DisplayName("RelatorioController - Testes Unitários")
class RelatorioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IRelatorioService iRelatorioService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @Nested
    @DisplayName("GET /api/relatorios/relatorioPorIntervaloDeData")
    class GerarRelatorio {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com dados do relatório para intervalo válido")
        void deveRetornar200ComRelatorioParaIntervaloValido() throws Exception {
            LocalDate inicio = LocalDate.of(2026, 6, 1);
            LocalDate fim = LocalDate.of(2026, 6, 30);
            RelatorioDto relatorioDto = new RelatorioDto();

            given(iRelatorioService.gerarRelatorio(inicio, fim)).willReturn(relatorioDto);

            mockMvc.perform(get("/api/relatorios/relatorioPorIntervaloDeData")
                            .param("inicio", "2026-06-01")
                            .param("fim", "2026-06-30"))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 400 BAD REQUEST quando a data final for anterior à data inicial")
        void deveRetornar400QuandoDataFimAntesDeInicio() throws Exception {
            mockMvc.perform(get("/api/relatorios/relatorioPorIntervaloDeData")
                            .param("inicio", "2026-06-30")
                            .param("fim", "2026-06-01"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorMessage").exists());
        }
    }

    @Nested
    @DisplayName("GET /api/relatorios/servicosMaisVendidos")
    class ListarServicosMaisVendidos {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com serviços mais vendidos")
        void deveRetornar200ComServicosMaisVendidos() throws Exception {
            LocalDate inicio = LocalDate.of(2026, 6, 1);
            LocalDate fim = LocalDate.of(2026, 6, 30);
            RelatorioServicoMaisVendidoDto dto = new RelatorioServicoMaisVendidoDto(1L, "Corte", 35.0, 10L, 350.0);

            given(iRelatorioService.listarServicosMaisVendidos(inicio, fim)).willReturn(List.of(dto));

            mockMvc.perform(get("/api/relatorios/servicosMaisVendidos")
                            .param("inicio", "2026-06-01")
                            .param("fim", "2026-06-30"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nomeServico").value("Corte"))
                    .andExpect(jsonPath("$[0].quantidadeVendas").value(10))
                    .andExpect(jsonPath("$[0].valorTotalArrecadado").value(350.0));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 400 BAD REQUEST quando a data final for anterior à data inicial")
        void deveRetornar400QuandoDataFimAntesDeInicio() throws Exception {
            mockMvc.perform(get("/api/relatorios/servicosMaisVendidos")
                            .param("inicio", "2026-06-30")
                            .param("fim", "2026-06-01"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/relatorios/clientesMaisFrequentes")
    class ListarClientesMaisFrequentes {

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 200 OK com clientes mais frequentes")
        void deveRetornar200ComClientesMaisFrequentes() throws Exception {
            LocalDate inicio = LocalDate.of(2026, 6, 1);
            LocalDate fim = LocalDate.of(2026, 6, 30);
            RelatorioClienteMaisFrequenteDto dto = new RelatorioClienteMaisFrequenteDto(1L, "João", "joao@email.com", "(86) 9 9999-9999", 5L, 175.0);

            given(iRelatorioService.listarClientesMaisFrequentes(inicio, fim)).willReturn(List.of(dto));

            mockMvc.perform(get("/api/relatorios/clientesMaisFrequentes")
                            .param("inicio", "2026-06-01")
                            .param("fim", "2026-06-30"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nomeCliente").value("João"))
                    .andExpect(jsonPath("$[0].emailCliente").value("joao@email.com"))
                    .andExpect(jsonPath("$[0].quantidadeAtendimentos").value(5))
                    .andExpect(jsonPath("$[0].valorTotalGasto").value(175.0));
        }

        @Test
        @WithMockUser
        @DisplayName("Deve retornar 400 BAD REQUEST quando a data final for anterior à data inicial")
        void deveRetornar400QuandoDataFimAntesDeInicio() throws Exception {
            mockMvc.perform(get("/api/relatorios/clientesMaisFrequentes")
                            .param("inicio", "2026-06-30")
                            .param("fim", "2026-06-01"))
                    .andExpect(status().isBadRequest());
        }
    }
}
