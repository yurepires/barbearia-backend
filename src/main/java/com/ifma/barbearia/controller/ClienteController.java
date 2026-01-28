package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.ClienteDto;
import com.ifma.barbearia.dto.ErrorResponseDto;
import com.ifma.barbearia.dto.ResponseDto;
import com.ifma.barbearia.constants.ClienteConstants;
import com.ifma.barbearia.constants.CommonConstants;
import com.ifma.barbearia.service.IClienteService;
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
        name = "Clientes REST API",
        description = "CRUD REST APIs para criar, atualizar, buscar e deletar detalhes de clientes"
)
@RestController
@RequestMapping(path = "/api/cliente", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Validated
public class ClienteController {

    private IClienteService iClienteService;

    @Operation(
            summary = "Criar Conta de Cliente REST API",
            description = "REST API para criar conta de cliente"
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
    @PostMapping("/criarCliente")
    public ResponseEntity<ResponseDto> criarCliente(@Valid @RequestBody ClienteDto clienteDto) {
        iClienteService.criarCliente(clienteDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CommonConstants.STATUS_201, ClienteConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Buscar detalhes de cliente REST API",
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
    @GetMapping("/buscarCliente")
    public ResponseEntity<ClienteDto> buscarCliente(
            @RequestParam @Email(message = "Por favor, insira um endereço de email válido!") String email) {
        ClienteDto clienteDto = iClienteService.buscarCliente(email);
        return ResponseEntity.status(HttpStatus.OK).body(clienteDto);
    }

    @Operation(
            summary = "Buscar detalhes de todos os clientes REST API",
            description = "REST API para buscar detalhes de todos os clientes"
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
    @GetMapping("/buscarTodosClientes")
    public ResponseEntity<List<ClienteDto>> buscarTodosClientes() {
        List<ClienteDto> clienteDtos = iClienteService.buscarTodosClientes();
        return ResponseEntity.status(HttpStatus.OK).body(clienteDtos);
    }

    @Operation(
            summary = "Atualizar detalhes de cliente REST API",
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
    @PutMapping("/atualizarCliente")
    public ResponseEntity<ResponseDto> atualizarCliente(@Valid @RequestBody ClienteDto clienteDto) {
        boolean atualizado = iClienteService.atualizarCliente(clienteDto);
        if (atualizado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CommonConstants.STATUS_200, CommonConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(CommonConstants.STATUS_417,
                            CommonConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Deletar cliente REST API",
            description = "REST API para deletar um cliente pelo email fornecido"
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
    @DeleteMapping("/deletarCliente")
    public ResponseEntity<ResponseDto> deletarCliente(
            @RequestParam @Email(message = "Por favor, insira um endereço de email válido!") String email) {
        boolean deletado = iClienteService.deletarCliente(email);
        if (deletado) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CommonConstants.STATUS_200, CommonConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(CommonConstants.STATUS_417,
                            CommonConstants.MESSAGE_417_DELETE));
        }
    }

}
