package com.ifma.barbearia.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ServicoDto {

    private Long servicoId;

    @NotBlank(message = "O nome do serviço é obrigatório!")
    private String nome;

    @NotNull(message = "O preço do serviço é obrigatório!")
    private Double preco;

    @NotBlank(message = "A descrição do serviço é obrigatória!")
    private String descricao;
}
