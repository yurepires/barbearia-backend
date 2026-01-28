package com.ifma.barbearia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Schema(
        name = "Barbeiro",
        description = "Schema de informações de Barbeiro"
)
@Data
public class BarbeiroDto {

    @Schema(
            description = "Nome do barbeiro", example = "Carlos do Corte"
    )
    private String nome;

    @Schema(
            description = "Endereço de email do barbeiro", example = "carlosdocorte@email.com"
    )
    @Email(message = "Por favor, insira um endereço de email válido!")
    private String email;

    @Schema(
            description = "Número de telefone do barbeiro", example = "(86) 9 8888-8888"
    )
    private String telefone;

    @Schema(
            description = "Especialidade do barbeiro", example = "Corte e Barba"
    )
    private String especialidade;
}
