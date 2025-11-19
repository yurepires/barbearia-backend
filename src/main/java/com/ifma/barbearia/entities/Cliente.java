package com.ifma.barbearia.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "cliente")
    private List<Agendamento> agendamentos;

    @OneToMany(mappedBy = "cliente")
    private List<HistoricoAtendimento> historico;

}
