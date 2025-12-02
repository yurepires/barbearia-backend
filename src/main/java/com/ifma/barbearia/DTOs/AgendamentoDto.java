package com.ifma.barbearia.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(
        name = "Agendamento",
        description = "Schema de informações de Agendamento"
)
@Data
public class AgendamentoDto {

    private Long id;

    @Schema(
            description = "Horário do agendamento", example = "2025-12-02T17:00"
    )
    @NotNull(message = "O horário é obrigatório")
    @Future(message = "O horário para agendamento não pode estar no passado")
    private LocalDateTime horario;

    @Schema(
            description = "Status do agendamento", example = "PENDENTE"
    )
    private String status;

    @Schema(
            description = "Email do cliente que está fazendo um agendamento", example = "joao@email.com"
    )
    @NotNull(message = "O cliente é obrigatório.")
    private String clienteEmail;

    @Schema(
            description = "Id do serviço"
    )
    @NotNull(message = "O serviço é obrigatório.")
    private Long servicoId;

    @Schema(
            description = "Email do barbeiro que irá realizar o atendimento do cliente", example = "carlosdocorte@email.com"
    )
    @NotNull(message = "O barbeiro é obrigatório.")
    private String barbeiroEmail;

}
