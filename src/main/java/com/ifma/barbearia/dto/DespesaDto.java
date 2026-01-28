package com.ifma.barbearia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(
        name = "Despesa",
        description = "Schema para armazenar informações de despesa"
)
public class DespesaDto {

    @Schema(
            description = "ID da despesa"
    )
    private Long despesaId;

    @NotEmpty(message = "Descrição da despesa não pode ser vazia")
    @Schema(
            description = "Descrição detalhada da despesa",
            example = "Compra de produtos de higiene"
    )
    private String descricao;

    @NotNull(message = "Valor da despesa não pode ser nulo")
    @Positive(message = "Valor da despesa deve ser positivo")
    @Schema(
            description = "Valor da despesa em reais",
            example = "150.50"
    )
    private Double valor;

    @NotNull(message = "Data da despesa não pode ser nula")
    @PastOrPresent(message = "Data da despesa não pode ser futura")
    @Schema(
            description = "Data da despesa",
            example = "2026-01-24"
    )
    private LocalDate dataDespesa;

}
