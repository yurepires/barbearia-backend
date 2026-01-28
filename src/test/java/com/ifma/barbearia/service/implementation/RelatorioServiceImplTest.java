package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.*;
import com.ifma.barbearia.entity.*;
import com.ifma.barbearia.mapper.DespesaMapper;
import com.ifma.barbearia.mapper.HistoricoAtendimentoMapper;
import com.ifma.barbearia.mapper.PagamentoMapper;
import com.ifma.barbearia.repository.DespesaRepository;
import com.ifma.barbearia.repository.HistoricoAtendimentoRepository;
import com.ifma.barbearia.repository.PagamentoRepository;
import com.ifma.barbearia.repository.projections.ClienteMaisFrequenteProjection;
import com.ifma.barbearia.repository.projections.ServicoMaisVendidoProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelatorioServiceImplTest {

    @Mock
    private HistoricoAtendimentoRepository historicoAtendimentoRepository;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private HistoricoAtendimentoMapper historicoAtendimentoMapper;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @Mock
    private DespesaMapper despesaMapper;

    @InjectMocks
    private RelatorioServiceImpl relatorioService;

    private LocalDate inicio;
    private LocalDate fim;

    @BeforeEach
    void setUp() {
        inicio = LocalDate.of(2026, 1, 1);
        fim = LocalDate.of(2026, 1, 31);
    }

    @Nested
    @DisplayName("gerarRelatorio")
    class GerarRelatorioTests {

        @Test
        @DisplayName("Deve gerar relatório consolidado com históricos, pagamentos e despesas")
        void deveGerarRelatorioConsolidado() {
            // Arrange
            HistoricoAtendimento historico = new HistoricoAtendimento();
            Pagamento pagamento = new Pagamento();
            Despesa despesa = new Despesa();

            HistoricoAtendimentoDto historicoDto = new HistoricoAtendimentoDto();
            PagamentoDto pagamentoDto = new PagamentoDto();
            DespesaDto despesaDto = new DespesaDto();

            when(historicoAtendimentoRepository.findByDataBetween(any(), any()))
                    .thenReturn(List.of(historico));
            when(pagamentoRepository.findByDataPagamentoBetween(any(), any()))
                    .thenReturn(List.of(pagamento));
            when(despesaRepository.findByDataDespesaBetween(any(), any()))
                    .thenReturn(List.of(despesa));

            when(historicoAtendimentoMapper.toDto(historico)).thenReturn(historicoDto);
            when(pagamentoMapper.toDto(pagamento)).thenReturn(pagamentoDto);
            when(despesaMapper.toDto(despesa)).thenReturn(despesaDto);

            when(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                    .thenReturn(1500.0);
            when(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                    .thenReturn(500.0);

            // Act
            RelatorioDto resultado = relatorioService.gerarRelatorio(inicio, fim);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getDataInicio()).isEqualTo(inicio);
            assertThat(resultado.getDataFim()).isEqualTo(fim);
            assertThat(resultado.getHistoricosAtendimentos()).hasSize(1);
            assertThat(resultado.getPagamentos()).hasSize(1);
            assertThat(resultado.getDespesas()).hasSize(1);
            assertThat(resultado.getValorTotalPagamentos()).isEqualTo(1500.0);
            assertThat(resultado.getValorTotalDespesas()).isEqualTo(500.0);
            assertThat(resultado.getBalancoFinal()).isEqualTo(1000.0);
        }

        @Test
        @DisplayName("Deve retornar balanço negativo quando despesas superam pagamentos")
        void deveRetornarBalancoNegativo() {
            // Arrange
            when(historicoAtendimentoRepository.findByDataBetween(any(), any()))
                    .thenReturn(List.of());
            when(pagamentoRepository.findByDataPagamentoBetween(any(), any()))
                    .thenReturn(List.of());
            when(despesaRepository.findByDataDespesaBetween(any(), any()))
                    .thenReturn(List.of());

            when(pagamentoRepository.sumValorByDataPagamentoBetween(any(), any()))
                    .thenReturn(500.0);
            when(despesaRepository.sumValorByDataDespesaBetween(any(), any()))
                    .thenReturn(1500.0);

            // Act
            RelatorioDto resultado = relatorioService.gerarRelatorio(inicio, fim);

            // Assert
            assertThat(resultado.getBalancoFinal()).isEqualTo(-1000.0);
        }
    }

    @Nested
    @DisplayName("listarServicosMaisVendidos")
    class ListarServicosMaisVendidosTests {

        @Test
        @DisplayName("Deve listar serviços mais vendidos ordenados por quantidade")
        void deveListarServicosMaisVendidos() {
            // Arrange
            ServicoMaisVendidoProjection corte = createServicoProjection(1L, "Corte", 50.0, 10L, 500.0);
            ServicoMaisVendidoProjection barba = createServicoProjection(2L, "Barba", 30.0, 5L, 150.0);

            when(historicoAtendimentoRepository.findServicosMaisVendidos(any(), any()))
                    .thenReturn(List.of(corte, barba));

            // Act
            List<RelatorioServicoMaisVendidoDto> resultado = relatorioService.listarServicosMaisVendidos(inicio, fim);

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNomeServico()).isEqualTo("Corte");
            assertThat(resultado.get(0).getQuantidadeVendas()).isEqualTo(10L);
            assertThat(resultado.get(1).getNomeServico()).isEqualTo("Barba");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há vendas no período")
        void deveRetornarListaVaziaQuandoSemVendas() {
            // Arrange
            when(historicoAtendimentoRepository.findServicosMaisVendidos(any(), any()))
                    .thenReturn(List.of());

            // Act
            List<RelatorioServicoMaisVendidoDto> resultado = relatorioService.listarServicosMaisVendidos(inicio, fim);

            // Assert
            assertThat(resultado).isEmpty();
        }

        private ServicoMaisVendidoProjection createServicoProjection(Long id, String nome, Double preco, Long qtd,
                Double total) {
            return new ServicoMaisVendidoProjection() {
                @Override
                public Long getServicoId() {
                    return id;
                }

                @Override
                public String getNomeServico() {
                    return nome;
                }

                @Override
                public Double getPreco() {
                    return preco;
                }

                @Override
                public Long getQuantidadeVendas() {
                    return qtd;
                }

                @Override
                public Double getValorTotalArrecadado() {
                    return total;
                }
            };
        }
    }

    @Nested
    @DisplayName("listarClientesMaisFrequentes")
    class ListarClientesMaisFrequentesTests {

        @Test
        @DisplayName("Deve listar clientes mais frequentes ordenados por atendimentos")
        void deveListarClientesMaisFrequentes() {
            // Arrange
            ClienteMaisFrequenteProjection joao = createClienteProjection(1L, "João", "joao@teste.com", "11999999999",
                    10L, 500.0);
            ClienteMaisFrequenteProjection maria = createClienteProjection(2L, "Maria", "maria@teste.com",
                    "11888888888", 5L, 250.0);

            when(historicoAtendimentoRepository.findClientesMaisFrequentes(any(), any()))
                    .thenReturn(List.of(joao, maria));

            // Act
            List<RelatorioClienteMaisFrequenteDto> resultado = relatorioService.listarClientesMaisFrequentes(inicio,
                    fim);

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).getNomeCliente()).isEqualTo("João");
            assertThat(resultado.get(0).getQuantidadeAtendimentos()).isEqualTo(10L);
            assertThat(resultado.get(1).getNomeCliente()).isEqualTo("Maria");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há clientes no período")
        void deveRetornarListaVaziaQuandoSemClientes() {
            // Arrange
            when(historicoAtendimentoRepository.findClientesMaisFrequentes(any(), any()))
                    .thenReturn(List.of());

            // Act
            List<RelatorioClienteMaisFrequenteDto> resultado = relatorioService.listarClientesMaisFrequentes(inicio,
                    fim);

            // Assert
            assertThat(resultado).isEmpty();
        }

        private ClienteMaisFrequenteProjection createClienteProjection(Long id, String nome, String email, String tel,
                Long qtd, Double total) {
            return new ClienteMaisFrequenteProjection() {
                @Override
                public Long getClienteId() {
                    return id;
                }

                @Override
                public String getNomeCliente() {
                    return nome;
                }

                @Override
                public String getEmailCliente() {
                    return email;
                }

                @Override
                public String getTelefoneCliente() {
                    return tel;
                }

                @Override
                public Long getQuantidadeAtendimentos() {
                    return qtd;
                }

                @Override
                public Double getValorTotalGasto() {
                    return total;
                }
            };
        }
    }
}
