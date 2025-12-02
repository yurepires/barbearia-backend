package com.ifma.barbearia.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(
        name = "Serviço",
        description = "Schema de informações de Serviço"
)
@Data
public class ServicoDto {

    @Schema(
            description = "Id do serviço"
    )
    private Long servicoId;

    @Schema(
            description = "Nome do serviço", example = "Corte degradê"
    )
    @NotBlank(message = "O nome do serviço é obrigatório!")
    private String nome;

    @Schema(
            description = "Preço do serviço", example = "14.99"
    )
    @NotNull(message = "O preço do serviço é obrigatório!")
    private Double preco;

    @Schema(
            description = "Descrição do serviço", example = "Corte degradê na régua máxima."
    )
    @NotBlank(message = "A descrição do serviço é obrigatória!")
    private String descricao;
}
