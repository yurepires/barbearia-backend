package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    boolean existsByBarbeiro_BarbeiroIdAndHorario(Long barbeiroId, LocalDateTime horario);

}
