package com.ifma.barbearia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Barbeiro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "barbeiro_id")
    private Long barbeiroId;

    private String nome;

    private String email;

    private String telefone;

    @OneToMany(mappedBy = "barbeiro")
    private List<Agendamento> agendamentos;

}
