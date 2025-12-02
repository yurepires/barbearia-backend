package com.ifma.barbearia.DTOs;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ClienteDto {

    private String nome;

    @Email(message = "Por favor, insira um endereço de email válido!")
    private String email;

    private String telefone;

    private String senha;

}
