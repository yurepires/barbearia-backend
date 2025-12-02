package com.ifma.barbearia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class HistoricoAtendimento extends EntidadeBase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historicoAtendimentoId;

    private LocalDateTime data;
    private Double valorPago;

    @ManyToOne
    @JoinColumn(name = "barbeiro_id", nullable = false)
    private Barbeiro barbeiro;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private Servico servico;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

}
