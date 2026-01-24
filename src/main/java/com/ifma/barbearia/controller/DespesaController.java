package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.DespesaDto;
import com.ifma.barbearia.DTOs.ErrorResponseDto;
import com.ifma.barbearia.DTOs.ResponseDto;
import com.ifma.barbearia.constants.DespesaConstants;
import com.ifma.barbearia.services.IDespesaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Despesas REST API",
        description = "REST APIs para registrar despesas da barbearia"
)
@RestController
@RequestMapping(path = "/api/despesas", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class DespesaController {

    private IDespesaService iDespesaService;

    @Operation(
            summary = "Registrar Despesa",
            description = "REST API para registrar uma despesa da barbearia com descrição, valor e data"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED - Despesa registrada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Validação falhou (descrição vazia, valor negativo ou data futura)",
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
    @PostMapping
    public ResponseEntity<ResponseDto> registrarDespesa(@Valid @RequestBody DespesaDto despesaDto) {
        iDespesaService.criarDespesa(despesaDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(DespesaConstants.STATUS_201, DespesaConstants.MESSAGE_201));
    }

}
