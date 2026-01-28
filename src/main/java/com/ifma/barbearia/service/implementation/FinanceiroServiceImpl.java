package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ResumoFinanceiroDto;
import com.ifma.barbearia.repository.DespesaRepository;
import com.ifma.barbearia.repository.PagamentoRepository;
import com.ifma.barbearia.service.IFinanceiroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@AllArgsConstructor
public class FinanceiroServiceImpl implements IFinanceiroService {

    private PagamentoRepository pagamentoRepository;
    private DespesaRepository despesaRepository;

    @Override
    public ResumoFinanceiroDto obterResumo(LocalDate inicio, LocalDate fim) {
        // Converter LocalDate para LocalDateTime para buscar pagamentos
        LocalDateTime inicioDateTime = inicio.atStartOfDay(); // 00:00:00
        LocalDateTime fimDateTime = fim.atTime(LocalTime.MAX); // 23:59:59.999999999

        // Buscar soma de pagamentos no período
        Double totalEntradas = pagamentoRepository.sumValorByDataPagamentoBetween(
                inicioDateTime,
                fimDateTime
        );

        // Buscar soma de despesas no período
        Double totalSaidas = despesaRepository.sumValorByDataDespesaBetween(inicio, fim);

        // Calcular saldo
        Double saldo = totalEntradas - totalSaidas;

        // Retornar DTO populado
        return new ResumoFinanceiroDto(totalEntradas, totalSaidas, saldo, inicio, fim);
    }

}
