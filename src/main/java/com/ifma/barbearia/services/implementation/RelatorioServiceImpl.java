package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.DespesaDto;
import com.ifma.barbearia.DTOs.HistoricoAtendimentoDto;
import com.ifma.barbearia.DTOs.PagamentoDto;
import com.ifma.barbearia.DTOs.RelatorioClienteMaisFrequenteDto;
import com.ifma.barbearia.DTOs.RelatorioDto;
import com.ifma.barbearia.DTOs.RelatorioServicoMaisVendidoDto;
import com.ifma.barbearia.entities.Despesa;
import com.ifma.barbearia.entities.HistoricoAtendimento;
import com.ifma.barbearia.entities.Pagamento;
import com.ifma.barbearia.mappers.DespesaMapper;
import com.ifma.barbearia.mappers.HistoricoAtendimentoMapper;
import com.ifma.barbearia.mappers.PagamentoMapper;
import com.ifma.barbearia.repositories.DespesaRepository;
import com.ifma.barbearia.repositories.HistoricoAtendimentoRepository;
import com.ifma.barbearia.repositories.PagamentoRepository;
import com.ifma.barbearia.services.IRelatorioService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RelatorioServiceImpl implements IRelatorioService {

    private final HistoricoAtendimentoRepository historicoAtendimentoRepository;
    private final PagamentoRepository pagamentoRepository;
    private final DespesaRepository despesaRepository;

    @Override
    public RelatorioDto gerarRelatorio(LocalDate inicio, LocalDate fim) {

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

        List<HistoricoAtendimento> historicos = historicoAtendimentoRepository.findByDataBetween(inicioDateTime, fimDateTime);
        List<HistoricoAtendimentoDto> historicosDto = historicos.stream()
                .map(h -> HistoricoAtendimentoMapper.mapToHistoricoAtendimentoDto(h, new HistoricoAtendimentoDto()))
                .collect(Collectors.toList());

        List<Pagamento> pagamentos = pagamentoRepository.findByDataPagamentoBetween(inicioDateTime, fimDateTime);
        List<PagamentoDto> pagamentosDto = pagamentos.stream()
                .map(p -> PagamentoMapper.mapToDto(p, new PagamentoDto()))
                .collect(Collectors.toList());

        List<Despesa> despesas = despesaRepository.findByDataDespesaBetween(inicio, fim);
        List<DespesaDto> despesasDto = despesas.stream()
                .map(d -> DespesaMapper.mapToDto(d, new DespesaDto()))
                .collect(Collectors.toList());

        Double valorTotalPagamentos = pagamentoRepository.sumValorByDataPagamentoBetween(inicioDateTime, fimDateTime);

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

        List<Object[]> resultados = historicoAtendimentoRepository.findServicosMaisVendidos(inicioDateTime, fimDateTime);

        return resultados.stream()
                .map(row -> new RelatorioServicoMaisVendidoDto(
                        (Long) row[0],           // servicoId
                        (String) row[1],         // nomeServico
                        (Double) row[2],         // preco
                        (Long) row[3],           // quantidadeVendas
                        (Double) row[4]          // valorTotalArrecadado
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<RelatorioClienteMaisFrequenteDto> listarClientesMaisFrequentes(LocalDate inicio, LocalDate fim) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX);

        List<Object[]> resultados = historicoAtendimentoRepository.findClientesMaisFrequentes(inicioDateTime, fimDateTime);

        return resultados.stream()
                .map(row -> new RelatorioClienteMaisFrequenteDto(
                        (Long) row[0],           // clienteId
                        (String) row[1],         // nomeCliente
                        (String) row[2],         // emailCliente
                        (String) row[3],         // telefoneCliente
                        (Long) row[4],           // quantidadeAtendimentos
                        (Double) row[5]          // valorTotalGasto
                ))
                .collect(Collectors.toList());
    }

}
