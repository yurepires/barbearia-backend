package com.ifma.barbearia.DTOs;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class OtpValidateDto {
    private String email;
    private String otp;
}
