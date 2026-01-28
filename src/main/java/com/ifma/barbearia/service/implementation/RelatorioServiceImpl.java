package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.DespesaDto;
import com.ifma.barbearia.dto.HistoricoAtendimentoDto;
import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.dto.RelatorioClienteMaisFrequenteDto;
import com.ifma.barbearia.dto.RelatorioDto;
import com.ifma.barbearia.dto.RelatorioServicoMaisVendidoDto;
import com.ifma.barbearia.entity.Despesa;
import com.ifma.barbearia.entity.HistoricoAtendimento;
import com.ifma.barbearia.entity.Pagamento;
import com.ifma.barbearia.mapper.DespesaMapper;
import com.ifma.barbearia.mapper.HistoricoAtendimentoMapper;
import com.ifma.barbearia.mapper.PagamentoMapper;
import com.ifma.barbearia.repository.projections.ClienteMaisFrequenteProjection;
import com.ifma.barbearia.repository.DespesaRepository;
import com.ifma.barbearia.repository.HistoricoAtendimentoRepository;
import com.ifma.barbearia.repository.PagamentoRepository;
import com.ifma.barbearia.repository.projections.ServicoMaisVendidoProjection;
import com.ifma.barbearia.service.IRelatorioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RelatorioServiceImpl implements IRelatorioService {

        private final HistoricoAtendimentoRepository historicoAtendimentoRepository;
        private final PagamentoRepository pagamentoRepository;
        private final DespesaRepository despesaRepository;
        private final HistoricoAtendimentoMapper historicoAtendimentoMapper;
        private final PagamentoMapper pagamentoMapper;
        private final DespesaMapper despesaMapper;

        @Override
        public RelatorioDto gerarRelatorio(LocalDate inicio, LocalDate fim) {

                LocalDateTime inicioDateTime = inicio.atStartOfDay();
                LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

                List<HistoricoAtendimento> historicos = historicoAtendimentoRepository.findByDataBetween(inicioDateTime,
                                fimDateTime);
                List<HistoricoAtendimentoDto> historicosDto = historicos.stream()
                                .map(historicoAtendimentoMapper::toDto)
                                .toList();

                List<Pagamento> pagamentos = pagamentoRepository.findByDataPagamentoBetween(inicioDateTime,
                                fimDateTime);
                List<PagamentoDto> pagamentosDto = pagamentos.stream()
                                .map(pagamentoMapper::toDto)
                                .toList();

                List<Despesa> despesas = despesaRepository.findByDataDespesaBetween(inicio, fim);
                List<DespesaDto> despesasDto = despesas.stream()
                                .map(despesaMapper::toDto)
                                .toList();

                Double valorTotalPagamentos = pagamentoRepository.sumValorByDataPagamentoBetween(inicioDateTime,
                                fimDateTime);

                Double valorTotalDespesas = despesaRepository.sumValorByDataDespesaBetween(inicio, fim);

                RelatorioDto relatorio = new RelatorioDto();
                relatorio.setDataInicio(inicio);
                relatorio.setDataFim(fim);
                relatorio.setHistoricosAtendimentos(historicosDto);
                relatorio.setPagamentos(pagamentosDto);
                relatorio.setDespesas(despesasDto);
                relatorio.setValorTotalPagamentos(valorTotalPagamentos);
                relatorio.setValorTotalDespesas(valorTotalDespesas);
                relatorio.setBalancoFinal(valorTotalPagamentos - valorTotalDespesas);

                return relatorio;
        }

        @Override
        public List<RelatorioServicoMaisVendidoDto> listarServicosMaisVendidos(LocalDate inicio, LocalDate fim) {
                LocalDateTime inicioDateTime = inicio.atStartOfDay();
                LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

                List<ServicoMaisVendidoProjection> resultados = historicoAtendimentoRepository.findServicosMaisVendidos(
                                inicioDateTime,
                                fimDateTime);

                return resultados.stream()
                                .map(projection -> new RelatorioServicoMaisVendidoDto(
                                                projection.getServicoId(),
                                                projection.getNomeServico(),
                                                projection.getPreco(),
                                                projection.getQuantidadeVendas(),
                                                projection.getValorTotalArrecadado()))
                                .toList();
        }

        @Override
        public List<RelatorioClienteMaisFrequenteDto> listarClientesMaisFrequentes(LocalDate inicio, LocalDate fim) {
                LocalDateTime inicioDateTime = inicio.atStartOfDay();
                LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

                List<ClienteMaisFrequenteProjection> resultados = historicoAtendimentoRepository
                                .findClientesMaisFrequentes(inicioDateTime,
                                                fimDateTime);

                return resultados.stream()
                                .map(projection -> new RelatorioClienteMaisFrequenteDto(
                                                projection.getClienteId(),
                                                projection.getNomeCliente(),
                                                projection.getEmailCliente(),
                                                projection.getTelefoneCliente(),
                                                projection.getQuantidadeAtendimentos(),
                                                projection.getValorTotalGasto()))
                                .toList();
        }

}
