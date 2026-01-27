package com.ifma.barbearia.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "ClienteMaisFrequente",
        description = "Schema para representar um cliente e sua frequência de atendimentos"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioClienteMaisFrequenteDto {

    @Schema(
            description = "ID do cliente",
            example = "1"
    )
    private Long clienteId;

    @Schema(
            description = "Nome do cliente",
            example = "João Silva"
    )
    private String nomeCliente;

    @Schema(
            description = "Email do cliente",
            example = "joao.silva@email.com"
    )
    private String emailCliente;

    @Schema(
            description = "Telefone do cliente",
            example = "(11) 99999-9999"
    )
    private String telefoneCliente;

    @Schema(
            description = "Quantidade de atendimentos realizados",
            example = "15"
    )
    private Long quantidadeAtendimentos;

    @Schema(
            description = "Valor total gasto pelo cliente",
            example = "750.00"
    )
    private Double valorTotalGasto;

}

