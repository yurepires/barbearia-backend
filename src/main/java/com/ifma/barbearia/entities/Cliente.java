package com.ifma.barbearia.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Cliente extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;

    private String nome;

    private String email;

    private String telefone;

    private String senha;
    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<Agendamento> agendamentos = new HashSet<>();

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private Set<HistoricoAtendimento> historico = new HashSet<>();
}
