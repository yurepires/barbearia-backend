package com.ifma.barbearia.controller;

import com.ifma.barbearia.DTOs.OtpRequestDto;
import com.ifma.barbearia.DTOs.OtpValidateDto;
import com.ifma.barbearia.DTOs.OtpResponseDto;
import com.ifma.barbearia.services.IClienteOtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/cliente/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteAuthController {

    private final IClienteOtpService clienteOtpService;

    public ClienteAuthController(IClienteOtpService clienteOtpService) {
        this.clienteOtpService = clienteOtpService;
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

}
