package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.OtpRequestDto;
import com.ifma.barbearia.dto.OtpResponseDto;
import com.ifma.barbearia.dto.OtpValidateDto;
import com.ifma.barbearia.entity.ClienteOtp;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.repository.ClienteOtpRepository;
import com.ifma.barbearia.repository.ClienteRepository;
import com.ifma.barbearia.service.IClienteOtpService;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.EmailService;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.time.LocalDateTime;

@Service
public class ClienteOtpServiceImpl implements IClienteOtpService {
    private final ClienteOtpRepository otpRepository;
    private final ClienteRepository clienteRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public ClienteOtpServiceImpl(ClienteOtpRepository otpRepository, ClienteRepository clienteRepository,
            EmailService emailService, JwtUtil jwtUtil) {
        this.otpRepository = otpRepository;
        this.clienteRepository = clienteRepository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    public void enviarCodigoPorEmail(OtpRequestDto request) {
        // verificar se o cliente existe
        Cliente cliente = clienteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        // gerar código aleatório
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(10);

        ClienteOtp clienteOtp = new ClienteOtp(null, request.getEmail(), otp, expiration, false);
        otpRepository.save(clienteOtp);

        // enviar e-mail com código
        emailService.enviarEmail(request.getEmail(), "Seu código de acesso", "Seu código para login é: " + otp);
    }

    @Override
    @Transactional
    public OtpResponseDto validarCodigo(OtpValidateDto validateDto) {
        ClienteOtp clienteOtp = otpRepository
                .findFirstByEmailAndOtpAndUsedFalseOrderByExpirationDesc(validateDto.getEmail(), validateDto.getOtp())
                .orElseThrow(() -> new RuntimeException("Código inválido ou expirado"));
        if (clienteOtp.getExpiration().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Código expirado, solicite um novo código");

        // marca o código como usado
        clienteOtp.setUsed(true);
        otpRepository.save(clienteOtp);

        // recupera o cliente para pegar nome/id e pôr no JWT
        Cliente cliente = clienteRepository.findByEmail(validateDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        String token = jwtUtil.generateToken(cliente.getEmail(), "CLIENTE");
        return new OtpResponseDto(token);
    }
}
