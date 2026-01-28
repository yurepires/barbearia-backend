package com.ifma.barbearia.controller;

import com.ifma.barbearia.dto.ErrorResponseDto;
import com.ifma.barbearia.dto.OtpRequestDto;
import com.ifma.barbearia.dto.OtpValidateDto;
import com.ifma.barbearia.dto.OtpResponseDto;
import com.ifma.barbearia.dto.AuthRequest;
import com.ifma.barbearia.dto.AuthResponse;
import com.ifma.barbearia.service.IClienteOtpService;
import com.ifma.barbearia.service.IClienteAuthService;
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
    private final IClienteAuthService clienteAuthService;

    public ClienteAuthController(IClienteOtpService clienteOtpService, IClienteAuthService clienteAuthService) {
        this.clienteOtpService = clienteOtpService;
        this.clienteAuthService = clienteAuthService;
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

    @Operation(
            summary = "Login com senha",
            description = "REST API para logar com email e senha"
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
    @PostMapping("/login-senha")
    public ResponseEntity<AuthResponse> loginSenha(@RequestBody AuthRequest authRequest) {
        AuthResponse response = clienteAuthService.autenticarComSenha(authRequest);
        return ResponseEntity.ok(response);
    }
}
