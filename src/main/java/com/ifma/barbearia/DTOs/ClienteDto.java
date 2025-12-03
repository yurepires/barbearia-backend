package com.ifma.barbearia.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Schema(
        name = "Cliente",
        description = "Schema de informações de Cliente"
)
@Data
public class ClienteDto {

    @Schema(
            description = "Nome do cliente", example = "João Maria"
    )
    private String nome;

    @Schema(
            description = "Endereço de email do cliente", example = "joao@email.com"
    )
    @Email(message = "Por favor, insira um endereço de email válido!")
    private String email;

    @Schema(
            description = "Número de telefone do cliente", example = "(86) 9 9999-9999"
    )
    private String telefone;

    private String senha;

}
