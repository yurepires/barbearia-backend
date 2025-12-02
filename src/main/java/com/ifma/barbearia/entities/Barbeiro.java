package com.ifma.barbearia.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    @JsonIgnore
    private Set<Agendamento> agendamentos = new HashSet<>();

}
