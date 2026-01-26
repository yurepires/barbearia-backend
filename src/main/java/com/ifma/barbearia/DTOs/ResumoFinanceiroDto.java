package com.ifma.barbearia.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "ResumoFinanceiro",
        description = "Schema para resumo financeiro com entradas, saídas e saldo"
)
public class ResumoFinanceiroDto {

    @Schema(
            description = "Total de entradas (pagamentos confirmados) no período",
            example = "3500.00"
    )
    private Double totalEntradas;

    @Schema(
            description = "Total de saídas (despesas) no período",
            example = "1200.50"
    )
    private Double totalSaidas;

    @Schema(
            description = "Saldo do período (entradas - saídas)",
            example = "2299.50"
    )
    private Double saldo;

    @Schema(
            description = "Data inicial do período consultado",
            example = "2026-01-01"
    )
    private LocalDate dataInicio;

    @Schema(
            description = "Data final do período consultado",
            example = "2026-01-31"
    )
    private LocalDate dataFim;

}
