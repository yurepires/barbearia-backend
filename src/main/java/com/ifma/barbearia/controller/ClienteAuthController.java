package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.OtpRequestDto;
import com.ifma.barbearia.DTOs.OtpValidateDto;
import com.ifma.barbearia.DTOs.OtpResponseDto;
import com.ifma.barbearia.DTOs.AuthRequest;
import com.ifma.barbearia.DTOs.AuthResponse;
import com.ifma.barbearia.services.IClienteOtpService;
import com.ifma.barbearia.services.IClienteService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/cliente/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteAuthController {

    private final IClienteOtpService clienteOtpService;
    private final IClienteService clienteService;

    public ClienteAuthController(IClienteOtpService clienteOtpService, IClienteService clienteService) {
        this.clienteOtpService = clienteOtpService;
        this.clienteService = clienteService;
    }

    @PostMapping("/gerarOtp")
    public ResponseEntity<String> gerarOtp(@RequestBody OtpRequestDto request) {
        clienteOtpService.enviarCodigoPorEmail(request);
        return ResponseEntity.ok("OTP enviado para o email.");
    }

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
