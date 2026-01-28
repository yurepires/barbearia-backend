package com.ifma.barbearia.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Barbeiro extends EntidadeBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barbeiro_id")
    private Long barbeiroId;

    private String nome;

    private String email;

    private String telefone;

}
