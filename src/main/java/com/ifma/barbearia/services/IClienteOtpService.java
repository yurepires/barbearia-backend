package com.ifma.barbearia.services;

import com.ifma.barbearia.DTOs.OtpRequestDto;
import com.ifma.barbearia.DTOs.OtpResponseDto;
import com.ifma.barbearia.DTOs.OtpValidateDto;

public interface IClienteOtpService {
    void enviarCodigoPorEmail(OtpRequestDto request);

    OtpResponseDto validarCodigo(OtpValidateDto validateDto);
}
