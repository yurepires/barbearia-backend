package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.ErrorResponseDto;
import com.ifma.barbearia.DTOs.OtpRequestDto;
import com.ifma.barbearia.DTOs.OtpValidateDto;
import com.ifma.barbearia.DTOs.OtpResponseDto;
import com.ifma.barbearia.DTOs.AuthRequest;
import com.ifma.barbearia.DTOs.AuthResponse;
import com.ifma.barbearia.services.IClienteOtpService;
import com.ifma.barbearia.services.IClienteService;
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
    private final IClienteService clienteService;

    public ClienteAuthController(IClienteOtpService clienteOtpService, IClienteService clienteService) {
        this.clienteOtpService = clienteOtpService;
        this.clienteService = clienteService;
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

    @PostMapping("/login-senha")
    public ResponseEntity<AuthResponse> loginSenha(@RequestBody AuthRequest authRequest) {
        AuthResponse response = clienteService.autenticarComSenha(authRequest);
        return ResponseEntity.ok(response);
    }
}
