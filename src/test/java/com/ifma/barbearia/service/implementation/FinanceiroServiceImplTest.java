package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ResumoFinanceiroDto;
import com.ifma.barbearia.repository.DespesaRepository;
import com.ifma.barbearia.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("FinanceiroServiceImpl - Testes Unitários")
class FinanceiroServiceImplTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private DespesaRepository despesaRepository;

    @InjectMocks
    private FinanceiroServiceImpl financeiroService;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    @BeforeEach
    void setUp() {
        dataInicio = LocalDate.of(2026, 6, 1);
        dataFim = LocalDate.of(2026, 6, 30);
    }

    @Nested
    @DisplayName("obterResumo()")
    class ObterResumo {

        @Test
        @DisplayName("Deve retornar resumo financeiro com saldo positivo corretamente calculado")
        void deveRetornarResumoFinanceiroComSaldoPositivo() {
            // given
            LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
            LocalDateTime fimDateTime = dataFim.atTime(LocalTime.MAX);

            given(pagamentoRepository.sumValorByDataPagamentoBetween(inicioDateTime, fimDateTime))
                    .willReturn(3500.00);
            given(despesaRepository.sumValorByDataDespesaBetween(dataInicio, dataFim))
                    .willReturn(1200.50);

            // when
            ResumoFinanceiroDto resultado = financeiroService.obterResumo(dataInicio, dataFim);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getTotalEntradas()).isEqualTo(3500.00);
            assertThat(resultado.getTotalSaidas()).isEqualTo(1200.50);
            assertThat(resultado.getSaldo()).isEqualTo(2299.50);
            assertThat(resultado.getDataInicio()).isEqualTo(dataInicio);
            assertThat(resultado.getDataFim()).isEqualTo(dataFim);
        }

        @Test
        @DisplayName("Deve retornar saldo negativo quando despesas superam pagamentos")
        void deveRetornarSaldoNegativoQuandoDespesasSuperamPagamentos() {
            // given
            LocalDateTime inicioDateTime = dataInicio.atStartOfDay();
            LocalDateTime fimDateTime = dataFim.atTime(LocalTime.MAX);

            given(pagamentoRepository.sumValorByDataPagamentoBetween(inicioDateTime, fimDateTime))
                    .willReturn(500.00);
            given(despesaRepository.sumValorByDataDespesaBetween(dataInicio, dataFim))
                    .willReturn(1000.00);

            // when
            ResumoFinanceiroDto resultado = financeiroService.obterResumo(dataInicio, dataFim);

            // then
            assertThat(resultado.getSaldo()).isNegative();
            assertThat(resultado.getSaldo()).isEqualTo(-500.00);
        }

        @Test
        @DisplayName("Deve consultar repositórios com datas convertidas corretamente")
        void deveConsultarRepositoriosComDatasConvertidas() {
            // given
            LocalDateTime inicioEsperado = dataInicio.atStartOfDay();
            LocalDateTime fimEsperado = dataFim.atTime(LocalTime.MAX);

            given(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                    .willReturn(0.0);
            given(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                    .willReturn(0.0);

            // when
            financeiroService.obterResumo(dataInicio, dataFim);

            // then
            verify(pagamentoRepository, times(1))
                    .sumValorByDataPagamentoBetween(inicioEsperado, fimEsperado);
            verify(despesaRepository, times(1))
                    .sumValorByDataDespesaBetween(dataInicio, dataFim);
        }

        @Test
        @DisplayName("Deve retornar saldo zerado quando não há pagamentos nem despesas")
        void deveRetornarSaldoZeradoQuandoNaoHaMovimentacao() {
            // given
            given(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                    .willReturn(0.0);
            given(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                    .willReturn(0.0);

            // when
            ResumoFinanceiroDto resultado = financeiroService.obterResumo(dataInicio, dataFim);

            // then
            assertThat(resultado.getTotalEntradas()).isZero();
            assertThat(resultado.getTotalSaidas()).isZero();
            assertThat(resultado.getSaldo()).isZero();
        }
    }
}
