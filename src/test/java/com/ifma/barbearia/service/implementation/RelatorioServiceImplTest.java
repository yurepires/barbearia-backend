package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.*;
import com.ifma.barbearia.entity.Despesa;
import com.ifma.barbearia.entity.HistoricoAtendimento;
import com.ifma.barbearia.entity.Pagamento;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RelatorioServiceImpl - Testes Unitários")
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

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private LocalDateTime inicioDateTime;
    private LocalDateTime fimDateTime;

    @BeforeEach
    void setUp() {
        dataInicio = LocalDate.of(2026, 6, 1);
        dataFim = LocalDate.of(2026, 6, 30);
        inicioDateTime = dataInicio.atStartOfDay();
        fimDateTime = dataFim.atTime(LocalTime.MAX);
    }

    @Nested
    @DisplayName("gerarRelatorio()")
    class GerarRelatorio {

        @Test
        @DisplayName("Deve gerar relatório completo com todos os dados preenchidos")
        void deveGerarRelatorioCompletoComSucesso() {
            // given
            HistoricoAtendimento historico = new HistoricoAtendimento();
            Pagamento pagamento = new Pagamento();
            Despesa despesa = new Despesa();

            HistoricoAtendimentoDto historicoDto = new HistoricoAtendimentoDto();
            PagamentoDto pagamentoDto = new PagamentoDto();
            DespesaDto despesaDto = new DespesaDto();

            given(historicoAtendimentoRepository.findByDataBetween(inicioDateTime, fimDateTime))
                    .willReturn(List.of(historico));
            given(pagamentoRepository.findByDataPagamentoBetween(inicioDateTime, fimDateTime))
                    .willReturn(List.of(pagamento));
            given(despesaRepository.findByDataDespesaBetween(dataInicio, dataFim))
                    .willReturn(List.of(despesa));
            given(pagamentoRepository.sumValorByDataPagamentoBetween(inicioDateTime, fimDateTime))
                    .willReturn(1500.00);
            given(despesaRepository.sumValorByDataDespesaBetween(dataInicio, dataFim))
                    .willReturn(500.00);
            given(historicoAtendimentoMapper.toDto(historico)).willReturn(historicoDto);
            given(pagamentoMapper.toDto(pagamento)).willReturn(pagamentoDto);
            given(despesaMapper.toDto(despesa)).willReturn(despesaDto);

            // when
            RelatorioDto resultado = relatorioService.gerarRelatorio(dataInicio, dataFim);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getDataInicio()).isEqualTo(dataInicio);
            assertThat(resultado.getDataFim()).isEqualTo(dataFim);
            assertThat(resultado.getHistoricosAtendimentos()).hasSize(1);
            assertThat(resultado.getPagamentos()).hasSize(1);
            assertThat(resultado.getDespesas()).hasSize(1);
            assertThat(resultado.getValorTotalPagamentos()).isEqualTo(1500.00);
            assertThat(resultado.getValorTotalDespesas()).isEqualTo(500.00);
            assertThat(resultado.getBalancoFinal()).isEqualTo(1000.00);
        }

        @Test
        @DisplayName("Deve gerar relatório com listas vazias quando não há dados no período")
        void deveGerarRelatorioVazioQuandoNaoHaDadosNoPeriodo() {
            // given
            given(historicoAtendimentoRepository.findByDataBetween(inicioDateTime, fimDateTime))
                    .willReturn(Collections.emptyList());
            given(pagamentoRepository.findByDataPagamentoBetween(inicioDateTime, fimDateTime))
                    .willReturn(Collections.emptyList());
            given(despesaRepository.findByDataDespesaBetween(dataInicio, dataFim))
                    .willReturn(Collections.emptyList());
            given(pagamentoRepository.sumValorByDataPagamentoBetween(inicioDateTime, fimDateTime))
                    .willReturn(0.0);
            given(despesaRepository.sumValorByDataDespesaBetween(dataInicio, dataFim))
                    .willReturn(0.0);

            // when
            RelatorioDto resultado = relatorioService.gerarRelatorio(dataInicio, dataFim);

            // then
            assertThat(resultado.getHistoricosAtendimentos()).isEmpty();
            assertThat(resultado.getPagamentos()).isEmpty();
            assertThat(resultado.getDespesas()).isEmpty();
            assertThat(resultado.getBalancoFinal()).isZero();
        }
    }

    @Nested
    @DisplayName("listarServicosMaisVendidos()")
    class ListarServicosMaisVendidos {

        @Test
        @DisplayName("Deve retornar lista de serviços mais vendidos mapeados")
        void deveRetornarServicosMaisVendidos() {
            // given
            ServicoMaisVendidoProjection projection = mock(ServicoMaisVendidoProjection.class);
            given(projection.getServicoId()).willReturn(1L);
            given(projection.getNomeServico()).willReturn("Corte de cabelo");
            given(projection.getPreco()).willReturn(30.00);
            given(projection.getQuantidadeVendas()).willReturn(15L);
            given(projection.getValorTotalArrecadado()).willReturn(450.00);

            given(historicoAtendimentoRepository.findServicosMaisVendidos(inicioDateTime, fimDateTime))
                    .willReturn(List.of(projection));

            // when
            List<RelatorioServicoMaisVendidoDto> resultado = relatorioService.listarServicosMaisVendidos(dataInicio, dataFim);

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNomeServico()).isEqualTo("Corte de cabelo");
            assertThat(resultado.get(0).getQuantidadeVendas()).isEqualTo(15L);
            assertThat(resultado.get(0).getValorTotalArrecadado()).isEqualTo(450.00);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há serviços vendidos no período")
        void deveRetornarListaVaziaQuandoNaoHaServicosVendidos() {
            // given
            given(historicoAtendimentoRepository.findServicosMaisVendidos(any(), any()))
                    .willReturn(Collections.emptyList());

            // when
            List<RelatorioServicoMaisVendidoDto> resultado = relatorioService.listarServicosMaisVendidos(dataInicio, dataFim);

            // then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("listarClientesMaisFrequentes()")
    class ListarClientesMaisFrequentes {

        @Test
        @DisplayName("Deve retornar lista de clientes mais frequentes mapeados")
        void deveRetornarClientesMaisFrequentes() {
            // given
            ClienteMaisFrequenteProjection projection = mock(ClienteMaisFrequenteProjection.class);
            given(projection.getClienteId()).willReturn(1L);
            given(projection.getNomeCliente()).willReturn("João Silva");
            given(projection.getEmailCliente()).willReturn("joao@email.com");
            given(projection.getTelefoneCliente()).willReturn("(86) 9 9999-9999");
            given(projection.getQuantidadeAtendimentos()).willReturn(8L);
            given(projection.getValorTotalGasto()).willReturn(240.00);

            given(historicoAtendimentoRepository.findClientesMaisFrequentes(inicioDateTime, fimDateTime))
                    .willReturn(List.of(projection));

            // when
            List<RelatorioClienteMaisFrequenteDto> resultado = relatorioService.listarClientesMaisFrequentes(dataInicio, dataFim);

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNomeCliente()).isEqualTo("João Silva");
            assertThat(resultado.get(0).getQuantidadeAtendimentos()).isEqualTo(8L);
            assertThat(resultado.get(0).getValorTotalGasto()).isEqualTo(240.00);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há clientes frequentes no período")
        void deveRetornarListaVaziaQuandoNaoHaClientesFrequentes() {
            // given
            given(historicoAtendimentoRepository.findClientesMaisFrequentes(any(), any()))
                    .willReturn(Collections.emptyList());

            // when
            List<RelatorioClienteMaisFrequenteDto> resultado = relatorioService.listarClientesMaisFrequentes(dataInicio, dataFim);

            // then
            assertThat(resultado).isEmpty();
        }
    }
}
