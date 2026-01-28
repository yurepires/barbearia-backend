package com.ifma.barbearia.dto;

import com.ifma.barbearia.entity.enums.StatusAgendamento;
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
        private StatusAgendamento status;

        @Schema(
                description = "Email do cliente que está fazendo um agendamento", example = "joao@email.com"
        )
        @NotNull(message = "O cliente é obrigatório.")
        private String clienteEmail;

        @Schema(
                description = "Nome do cliente", example = "João Santos"
        )
        private String clienteNome;

        @Schema(
                description = "Id do serviço"
        )
        @NotNull(message = "O serviço é obrigatório.")
        private Long servicoId;

        @Schema(
                description = "Nome do serviço", example = "Corte de Cabelo"
        )
        private String servicoNome;

        @Schema(
                description = "Email do barbeiro que irá realizar o atendimento do cliente", example = "carlosdocorte@email.com"
        )
        @NotNull(message = "O barbeiro é obrigatório.")
        private String barbeiroEmail;

        @Schema(
                description = "Nome do barbeiro", example = "Carlos Silva"
        )
        private String barbeiroNome;

}
