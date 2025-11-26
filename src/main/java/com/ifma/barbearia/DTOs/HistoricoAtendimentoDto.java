package com.ifma.barbearia.DTOs;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoricoAtendimentoDto {

    private Long id;
    private LocalDateTime dataAtendimento;
    private Double valorPago;

    private String nomeCliente;
    private String nomeBarbeiro;
    private String nomeServico;

}
