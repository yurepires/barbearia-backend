package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.ErrorResponseDto;
import com.ifma.barbearia.dto.RelatorioClienteMaisFrequenteDto;
import com.ifma.barbearia.dto.RelatorioDto;
import com.ifma.barbearia.dto.RelatorioServicoMaisVendidoDto;
import com.ifma.barbearia.constants.RelatorioConstants;
import com.ifma.barbearia.constants.CommonConstants;
import com.ifma.barbearia.service.IRelatorioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "Relatório REST API",
        description = "REST APIs para gerar relatórios consolidados com históricos de atendimentos, pagamentos e despesas"
)
@RestController
@RequestMapping(path = "/api/relatorios", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class RelatorioController {

    private final IRelatorioService iRelatorioService;

    @Operation(
            summary = "Gerar Relatório por Intervalo de Datas",
            description = "REST API para gerar um relatório consolidado contendo históricos de atendimentos, pagamentos e despesas dentro de um intervalo de datas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_200,
                    description = RelatorioConstants.MESSAGE_200),
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_400,
                    description = "Bad Request - Parâmetros inválidos (datas obrigatórias ou formato inválido)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_500,
                    description = RelatorioConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/relatorioPorIntervaloDeData")
    public ResponseEntity<RelatorioDto> gerarRelatorio(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException(RelatorioConstants.MESSAGE_400_DATA_INVALIDA);
        }

        RelatorioDto relatorio = iRelatorioService.gerarRelatorio(inicio, fim);
        return ResponseEntity.status(HttpStatus.OK).body(relatorio);
    }

    @Operation(
            summary = "Listar Serviços Mais Vendidos",
            description = "REST API para listar os serviços mais vendidos dentro de um intervalo de datas, ordenados pela quantidade de vendas"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_200,
                    description = "Lista de serviços mais vendidos obtida com sucesso"),
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_400,
                    description = "Bad Request - Parâmetros inválidos (datas obrigatórias ou formato inválido)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_500,
                    description = RelatorioConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/servicosMaisVendidos")
    public ResponseEntity<List<RelatorioServicoMaisVendidoDto>> listarServicosMaisVendidos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException(RelatorioConstants.MESSAGE_400_DATA_INVALIDA);
        }

        List<RelatorioServicoMaisVendidoDto> servicosMaisVendidos = iRelatorioService
                .listarServicosMaisVendidos(inicio, fim);
        return ResponseEntity.status(HttpStatus.OK).body(servicosMaisVendidos);
    }

    @Operation(
            summary = "Listar Clientes Mais Frequentes",
            description = "REST API para listar os clientes mais frequentes dentro de um intervalo de datas, ordenados pela quantidade de atendimentos"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_200,
                    description = "Lista de clientes mais frequentes obtida com sucesso"),
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_400,
                    description = "Bad Request - Parâmetros inválidos (datas obrigatórias ou formato inválido)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = CommonConstants.STATUS_500,
                    description = RelatorioConstants.MESSAGE_500,
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/clientesMaisFrequentes")
    public ResponseEntity<List<RelatorioClienteMaisFrequenteDto>> listarClientesMaisFrequentes(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException(RelatorioConstants.MESSAGE_400_DATA_INVALIDA);
        }

        List<RelatorioClienteMaisFrequenteDto> clientesMaisFrequentes = iRelatorioService
                .listarClientesMaisFrequentes(inicio, fim);
        return ResponseEntity.status(HttpStatus.OK).body(clientesMaisFrequentes);
    }

}