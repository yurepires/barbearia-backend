package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.BarbeiroDto;
import com.ifma.barbearia.DTOs.ErrorResponseDto;
import com.ifma.barbearia.DTOs.ResponseDto;
import com.ifma.barbearia.constants.BarbeiroConstants;
import com.ifma.barbearia.services.IBarbeiroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Barbeiros REST API",
        description = "CRUD REST APIs para criar, atualizar, buscar e deletar detalhes de barbeiros"
)
@RestController
@RequestMapping(path = "/api/barbeiro", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class BarbeiroController {

    private IBarbeiroService iBarbeiroService;

    @Operation(
            summary = "Criar Conta de Barbeiro REST API",
            description = "REST API para criar conta de barbeiro"
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
    @PostMapping("/criarBarbeiro")
    public ResponseEntity<ResponseDto> criarBarbeiro(@Valid @RequestBody BarbeiroDto barbeiroDto) {
        iBarbeiroService.criarBarbeiro(barbeiroDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(BarbeiroConstants.STATUS_201, BarbeiroConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Buscar detalhes de barbeiro REST API",
            description = "REST API para buscar detalhes de barbeiro pelo email fornecido"
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
    @GetMapping("/buscarBarbeiro")
    public ResponseEntity<BarbeiroDto> buscarBarbeiro(
            @RequestParam @Email(message = "Por favor, insira um endereço de email válido!") String email) {
        BarbeiroDto barbeiroDto = iBarbeiroService.buscarBarbeiro(email);
        return ResponseEntity.status(HttpStatus.OK).body(barbeiroDto);
    }

    @Operation(
            summary = "Buscar detalhes de todos os barbeiros REST API",
            description = "REST API para buscar detalhes de todos os barbeiros"
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
    @GetMapping("/buscarTodosBarbeiros")
    public ResponseEntity<List<BarbeiroDto>> buscarTodosBarbeiros() {
        List<BarbeiroDto> barbeiroDtos = iBarbeiroService.buscarTodosBarbeiros();
        return ResponseEntity.status(HttpStatus.OK).body(barbeiroDtos);
    }

    @Operation(
            summary = "Atualizar detalhes de barbeiro REST API",
            description = "REST API para atualizar detalhes de um barbeiro pelo email fornecido"
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
    @PutMapping("/atualizarBarbeiro")
    public ResponseEntity<ResponseDto> atualizarBarbeiro(@Valid @RequestBody BarbeiroDto barbeiroDto) {
        boolean atualizado = iBarbeiroService.atualizarBarbeiro(barbeiroDto);
        if (atualizado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_200, BarbeiroConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_417, BarbeiroConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Deletar barbeiro REST API",
            description = "REST API para deletar um barbeiro pelo email fornecido"
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
    @DeleteMapping("/deletarBarbeiro")
    public ResponseEntity<ResponseDto> deletarBarbeiro(
            @RequestParam @Email(message = "Por favor, insira um endereço de email válido!") String email) {
        boolean deletado = iBarbeiroService.deletarBarbeiro(email);
        if (deletado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_200, BarbeiroConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(BarbeiroConstants.STATUS_417, BarbeiroConstants.MESSAGE_417_DELETE));
        }
    }

}
