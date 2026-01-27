package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query("SELECT COALESCE(SUM(d.valor), 0.0) FROM Despesa d WHERE d.dataDespesa BETWEEN :inicio AND :fim")
    Double sumValorByDataDespesaBetween(@Param("inicio") LocalDate inicio, @Param("fim") LocalDate fim);

    List<Despesa> findByDataDespesaBetween(LocalDate inicio, LocalDate fim);

}
