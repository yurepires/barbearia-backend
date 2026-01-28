package com.ifma.barbearia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "ServicoMaisVendido",
        description = "Schema para representar um serviço e sua quantidade de vendas"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioServicoMaisVendidoDto {

    @Schema(
            description = "ID do serviço",
            example = "1"
    )
    private Long servicoId;

    @Schema(
            description = "Nome do serviço",
            example = "Corte + Barba"
    )
    private String nomeServico;

    @Schema(
            description = "Preço do serviço",
            example = "50.00"
    )
    private Double preco;

    @Schema(
            description = "Quantidade de vezes que o serviço foi vendido",
            example = "25"
    )
    private Long quantidadeVendas;

    @Schema(
            description = "Valor total arrecadado com este serviço",
            example = "1250.00"
    )
    private Double valorTotalArrecadado;

}

