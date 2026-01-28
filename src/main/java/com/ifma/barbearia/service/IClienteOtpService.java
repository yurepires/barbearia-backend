package com.ifma.barbearia.service;

import com.ifma.barbearia.dto.OtpRequestDto;
import com.ifma.barbearia.dto.OtpResponseDto;
import com.ifma.barbearia.dto.OtpValidateDto;

public interface IClienteOtpService {
    void enviarCodigoPorEmail(OtpRequestDto request);

    OtpResponseDto validarCodigo(OtpValidateDto validateDto);
}
