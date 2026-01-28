package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.ErrorResponseDto;
import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.service.IPagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Pagamentos REST API",
        description = "REST APIs para buscar pagamentos de agendamentos. Os pagamentos são criados automaticamente ao concluir um agendamento."
)
@RestController
@RequestMapping(path = "/api/pagamentos", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class PagamentoController {

    private IPagamentoService iPagamentoService;


    @Operation(
            summary = "Buscar Pagamento",
            description = "REST API para buscar detalhes de um pagamento pelo ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pagamento não encontrado",
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
    @GetMapping("/{pagamentoId}")
    public ResponseEntity<PagamentoDto> buscarPagamento(@PathVariable Long pagamentoId) {
        PagamentoDto pagamentoDto = iPagamentoService.buscarPagamento(pagamentoId);
        return ResponseEntity.status(HttpStatus.OK).body(pagamentoDto);
    }

    @Operation(
            summary = "Buscar Pagamento por Agendamento",
            description = "REST API para buscar um pagamento pelo ID do agendamento"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pagamento não encontrado para o agendamento",
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
    @GetMapping("/agendamento/{agendamentoId}")
    public ResponseEntity<PagamentoDto> buscarPagamentoPorAgendamento(@PathVariable Long agendamentoId) {
        PagamentoDto pagamentoDto = iPagamentoService.buscarPagamentoPorAgendamento(agendamentoId);
        return ResponseEntity.status(HttpStatus.OK).body(pagamentoDto);
    }

}
