package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByAgendamento_AgendamentoId(Long agendamentoId);

    @Query("SELECT COALESCE(SUM(p.valor), 0.0) FROM Pagamento p WHERE p.dataPagamento BETWEEN :inicio AND :fim AND p.status = :status")
    Double sumValorByDataPagamentoBetweenAndStatus(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim, @Param("status") String status);

}
