package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.RelatorioClienteMaisFrequenteDto;
import com.ifma.barbearia.DTOs.RelatorioDto;
import com.ifma.barbearia.DTOs.RelatorioServicoMaisVendidoDto;

import java.time.LocalDate;
import java.util.List;

public interface IRelatorioService {

    /**
     * Gera um relatório consolidado com históricos de atendimentos, pagamentos e despesas
     * dentro de um intervalo de datas especificado.
     *
     * @param inicio Data de início do período (formato YYYY-MM-DD)
     * @param fim Data de fim do período (formato YYYY-MM-DD)
     * @return RelatorioDto contendo os dados consolidados do período
     */
    RelatorioDto gerarRelatorio(LocalDate inicio, LocalDate fim);

    /**
     * Lista os serviços mais vendidos em um intervalo de datas,
     * ordenados pela quantidade de vendas (decrescente).
     *
     * @param inicio Data de início do período (formato YYYY-MM-DD)
     * @param fim Data de fim do período (formato YYYY-MM-DD)
     * @return Lista de ServicoMaisVendidoDto com informações de vendas
     */
    List<RelatorioServicoMaisVendidoDto> listarServicosMaisVendidos(LocalDate inicio, LocalDate fim);

    /**
     * Lista os clientes mais frequentes em um intervalo de datas,
     * ordenados pela quantidade de atendimentos (decrescente).
     *
     * @param inicio Data de início do período (formato YYYY-MM-DD)
     * @param fim Data de fim do período (formato YYYY-MM-DD)
     * @return Lista de RelatorioClienteMaisFrequenteDto com informações de frequência
     */
    List<RelatorioClienteMaisFrequenteDto> listarClientesMaisFrequentes(LocalDate inicio, LocalDate fim);

}

