package com.ifma.barbearia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Pagamento extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pagamentoId;

    @ManyToOne
    @JoinColumn(name = "agendamento_id", nullable = false)
    private Agendamento agendamento;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private String formaPagamento;

    @Column(nullable = false)

    private LocalDateTime dataPagamento;

}
