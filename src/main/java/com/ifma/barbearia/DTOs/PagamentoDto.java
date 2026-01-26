package com.ifma.barbearia.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(
        name = "Pagamento",
        description = "Schema para armazenar informações de pagamento"
)
public class PagamentoDto {

    @Schema(
            description = "ID do pagamento"
    )
    private Long pagamentoId;

    @NotNull(message = "ID do agendamento não pode ser nulo")
    @Schema(
            description = "ID do agendamento associado ao pagamento"
    )
    private Long agendamentoId;

    @NotNull(message = "Valor do pagamento não pode ser nulo")
    @Positive(message = "Valor do pagamento deve ser positivo")
    @Schema(
            description = "Valor do pagamento"
    )
    private Double valor;

    @NotEmpty(message = "Forma de pagamento não pode ser vazia")
    @Schema(
            description = "Forma de pagamento: DINHEIRO, CARTAO_CREDITO, CARTAO_DEBITO, PIX"
    )
    private String formaPagamento;

    @NotEmpty(message = "Status do pagamento não pode ser vazio")
    @Schema(
            description = "Status do pagamento: PENDENTE, CONFIRMADO, CANCELADO"
    )
    private String status;

    @Schema(
            description = "Data e hora do pagamento"
    )
    private LocalDateTime dataPagamento;

}
