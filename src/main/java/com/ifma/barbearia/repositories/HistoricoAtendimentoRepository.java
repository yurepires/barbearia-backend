package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.HistoricoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoAtendimentoRepository extends JpaRepository<HistoricoAtendimento, Long> {

    // histórico por cliente
    List<HistoricoAtendimento> findByCliente_Email(String email);

    // histórico por barbeiro
    List<HistoricoAtendimento> findByBarbeiro_Email(String barbeiroEmail);

    List<HistoricoAtendimento> findByServico_ServicoId(Long servicoId);

    // histórico de um intervalo de tempo
    List<HistoricoAtendimento> findByDataBetween(LocalDateTime data, LocalDateTime data2);

}
