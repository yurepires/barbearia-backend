package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.ErrorResponseDto;
import com.ifma.barbearia.DTOs.OtpRequestDto;
import com.ifma.barbearia.DTOs.OtpValidateDto;
import com.ifma.barbearia.DTOs.OtpResponseDto;
import com.ifma.barbearia.services.IClienteOtpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Cliente Auth REST API",
        description = "REST API para login de cliente"
)
@RestController
@RequestMapping(path = "/api/cliente/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteAuthController {

    private final IClienteOtpService clienteOtpService;

    public ClienteAuthController(IClienteOtpService clienteOtpService) {
        this.clienteOtpService = clienteOtpService;
    }

    @Operation(
            summary = "Enviar código OTP para o email do cliente",
            description = "REST API para enviar código OTP para o email do cliente"
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
    @PostMapping("/gerarOtp")
    public ResponseEntity<String> gerarOtp(@RequestBody OtpRequestDto request) {
        clienteOtpService.enviarCodigoPorEmail(request);
        return ResponseEntity.ok("OTP enviado para o email.");
    }

    @Operation(
            summary = "Validar código OTP",
            description = "REST API para validar código OTP"
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
    @PostMapping("/validarOtp")
    public ResponseEntity<OtpResponseDto> validarOtp(@RequestBody OtpValidateDto validateDto) {
        OtpResponseDto response = clienteOtpService.validarCodigo(validateDto);
        return ResponseEntity.ok(response);
    }

}
