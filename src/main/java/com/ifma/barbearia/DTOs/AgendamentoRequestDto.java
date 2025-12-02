package com.ifma.barbearia.DTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AgendamentoRequestDto {

    private Long id;

    @NotNull(message = "O horário é obrigatório")
    @Future(message = "O horário para agendamento não pode estar no passado")
    private LocalDateTime horario;

    private String status;

    @NotNull(message = "O cliente é obrigatório.")
    private String clienteEmail;

    @NotNull(message = "O serviço é obrigatório.")
    private Long servicoId;

    @NotNull(message = "O barbeiro é obrigatório.")
    private String barbeiroEmail;

}
