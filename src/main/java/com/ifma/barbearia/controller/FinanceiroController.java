package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.ErrorResponseDto;
import com.ifma.barbearia.dto.ResumoFinanceiroDto;
import com.ifma.barbearia.service.IFinanceiroService;
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

@Tag(
        name = "Financeiro REST API",
        description = "REST APIs para consultar resumo financeiro com entradas, saídas e saldo"
)
@RestController
@RequestMapping(path = "/api/financeiro", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class FinanceiroController {

    private IFinanceiroService iFinanceiroService;

    @Operation(
            summary = "Obter Resumo Financeiro",
            description = "REST API para obter resumo financeiro com total de entradas (pagamentos confirmados), total de saídas (despesas) e saldo em um período"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK - Resumo financeiro obtido com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Parâmetros inválidos (datas obrigatórias ou formato inválido)",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/resumo")
    public ResponseEntity<ResumoFinanceiroDto> obterResumo(
            @RequestParam(required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam(required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fim) {

        // Validar que data fim >= data início
        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("A data final não pode ser anterior à data inicial");
        }

        ResumoFinanceiroDto resumo = iFinanceiroService.obterResumo(inicio, fim);
        return ResponseEntity.status(HttpStatus.OK).body(resumo);
    }

}
