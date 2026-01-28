package com.ifma.barbearia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Schema(
        name = "Relatório",
        description = "Schema para relatório consolidado com históricos de atendimentos, pagamentos e despesas"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioDto {

    @Schema(
            description = "Data de início do período do relatório",
            example = "2025-01-01"
    )
    private LocalDate dataInicio;

    @Schema(
            description = "Data de fim do período do relatório",
            example = "2025-01-31"
    )
    private LocalDate dataFim;

    @Schema(
            description = "Lista de históricos de atendimentos no período"
    )
    private List<HistoricoAtendimentoDto> historicosAtendimentos;

    @Schema(
            description = "Lista de pagamentos no período"
    )
    private List<PagamentoDto> pagamentos;

    @Schema(
            description = "Lista de despesas no período"
    )
    private List<DespesaDto> despesas;

    @Schema(
            description = "Soma dos valores dos pagamentos confirmados",
            example = "1500.00"
    )
    private Double valorTotalPagamentos;

    @Schema(
            description = "Soma dos valores das despesas",
            example = "500.00"
    )
    private Double valorTotalDespesas;

    @Schema(
            description = "Balanço final (valor total de pagamentos menos valor total de despesas)",
            example = "1000.00"
    )
    private Double balancoFinal;

}

