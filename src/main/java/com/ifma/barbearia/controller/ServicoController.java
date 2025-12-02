package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.ErrorResponseDto;
import com.ifma.barbearia.DTOs.ServicoDto;
import com.ifma.barbearia.DTOs.ResponseDto;
import com.ifma.barbearia.constants.ServicoConstants;
import com.ifma.barbearia.services.IServicoService;
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

import java.util.List;

@Tag(
        name = "Clientes REST API",
        description = "CRUD REST APIs para criar, atualizar, buscar e deletar detalhes de clientes"
)
@RestController
@RequestMapping(path = "/api/servico", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class ServicoController {

    private IServicoService iServicoService;

    @Operation(
            summary = "Criar Serviço",
            description = "REST API para criar serviço"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping("/criarServico")
    public ResponseEntity<ResponseDto> criarServico(@Valid @RequestBody ServicoDto servicoDto) {
        iServicoService.criarServico(servicoDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ServicoConstants.STATUS_201, ServicoConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Buscar detalhes de um serviço",
            description = "REST API para buscar detalhes de um serviço pelo id fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/buscarServico")
    public ResponseEntity<ServicoDto> buscarServico(@RequestParam Long servicoId) {
        ServicoDto servicoDto = iServicoService.buscarServico(servicoId);
        return ResponseEntity.status(HttpStatus.OK).body(servicoDto);
    }

    @Operation(
            summary = "Buscar detalhes de todos os serviços",
            description = "REST API para buscar detalhes de cliente pelo email fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping("/buscarTodosServicos")
    public ResponseEntity<List<ServicoDto>> buscarTodosServicos() {
        List<ServicoDto> servicoDtos = iServicoService.buscarTodosServicos();
        return ResponseEntity.status(HttpStatus.OK).body(servicoDtos);
    }

    @Operation(
            summary = "Atualizar detalhes de serviço",
            description = "REST API para atualizar detalhes de um cliente pelo email fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PutMapping("/atualizarServico")
    public ResponseEntity<ResponseDto> atualizarServico(@Valid @RequestBody ServicoDto servicoDto) {
        boolean atualizado = iServicoService.atualizarServico(servicoDto);
        if (atualizado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ServicoConstants.STATUS_200, ServicoConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ServicoConstants.STATUS_417, ServicoConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Deletar serviço",
            description = "REST API para deletar um serviço pelo id fornecido"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/deletarServico")
    public ResponseEntity<ResponseDto> deletarServico(@RequestParam Long servicoId) {
        boolean deletado = iServicoService.deletarServico(servicoId);
        if (deletado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ServicoConstants.STATUS_200, ServicoConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ServicoConstants.STATUS_417, ServicoConstants.MESSAGE_417_DELETE));
        }
    }
}