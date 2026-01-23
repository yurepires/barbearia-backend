package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByAgendamento_AgendamentoId(Long agendamentoId);

}
