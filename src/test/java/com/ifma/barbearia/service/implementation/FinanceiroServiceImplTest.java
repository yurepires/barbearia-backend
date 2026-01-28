package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ResumoFinanceiroDto;
import com.ifma.barbearia.repository.DespesaRepository;
import com.ifma.barbearia.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinanceiroServiceImplTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private DespesaRepository despesaRepository;

    @InjectMocks
    private FinanceiroServiceImpl financeiroService;

    private LocalDate inicio;
    private LocalDate fim;

    @BeforeEach
    void setUp() {
        inicio = LocalDate.of(2026, 1, 1);
        fim = LocalDate.of(2026, 1, 31);
    }

    @Test
    @DisplayName("Deve calcular resumo financeiro com saldo positivo")
    void deveCalcularResumoComSaldoPositivo() {
        // Arrange
        when(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                .thenReturn(5000.0);
        when(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                .thenReturn(2000.0);

        // Act
        ResumoFinanceiroDto resultado = financeiroService.obterResumo(inicio, fim);

        // Assert
        assertThat(resultado.getTotalEntradas()).isEqualTo(5000.0);
        assertThat(resultado.getTotalSaidas()).isEqualTo(2000.0);
        assertThat(resultado.getSaldo()).isEqualTo(3000.0);
        assertThat(resultado.getDataInicio()).isEqualTo(inicio);
        assertThat(resultado.getDataFim()).isEqualTo(fim);
    }

    @Test
    @DisplayName("Deve calcular resumo financeiro com saldo negativo")
    void deveCalcularResumoComSaldoNegativo() {
        // Arrange
        when(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                .thenReturn(1000.0);
        when(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                .thenReturn(3500.0);

        // Act
        ResumoFinanceiroDto resultado = financeiroService.obterResumo(inicio, fim);

        // Assert
        assertThat(resultado.getSaldo()).isEqualTo(-2500.0);
    }

    @Test
    @DisplayName("Deve calcular resumo financeiro com saldo zero")
    void deveCalcularResumoComSaldoZero() {
        // Arrange
        when(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                .thenReturn(1500.0);
        when(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                .thenReturn(1500.0);

        // Act
        ResumoFinanceiroDto resultado = financeiroService.obterResumo(inicio, fim);

        // Assert
        assertThat(resultado.getSaldo()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Deve retornar valores zerados quando não há movimentação")
    void deveRetornarZeradoQuandoSemMovimentacao() {
        // Arrange
        when(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                .thenReturn(0.0);
        when(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                .thenReturn(0.0);

        // Act
        ResumoFinanceiroDto resultado = financeiroService.obterResumo(inicio, fim);

        // Assert
        assertThat(resultado.getTotalEntradas()).isEqualTo(0.0);
        assertThat(resultado.getTotalSaidas()).isEqualTo(0.0);
        assertThat(resultado.getSaldo()).isEqualTo(0.0);
    }
}
