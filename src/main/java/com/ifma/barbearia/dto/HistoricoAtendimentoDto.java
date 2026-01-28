package com.ifma.barbearia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        name = "Histórico de Atendimento",
        description = "Schema de informações de Histórico de Atendimento"
)
@Data
public class HistoricoAtendimentoDto {

    @Schema(
            description = "Id do atendimento"
    )
    private Long id;

    @Schema(
            description = "Data em que o atendimento foi realizado", example = "2025-12-02T17:00"
    )
    private LocalDateTime dataAtendimento;

    @Schema(
            description = "Valor pago no atendimento", example = "49.99"
    )
    private Double valorPago;

    @Schema(
            description = "Forma de pagamento utilizada", example = "PIX"
    )
    private String formaPagamento;

    @Schema(
            description = "Nome do cliente", example = "João Maria"
    )
    private String nomeCliente;

    @Schema(
            description = "Nome do barbeiro", example = "Carlos do Corte"
    )
    private String nomeBarbeiro;

    @Schema(
            description = "Nome do serviço", example = "Corte degradê"
    )
    private String nomeServico;

}
