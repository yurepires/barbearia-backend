package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.HistoricoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // serviços mais vendidos em um intervalo de tempo
    @Query("SELECT h.servico.servicoId, h.servico.nome, h.servico.preco, COUNT(h) as quantidade, SUM(h.pagamento.valor) as valorTotal " +
           "FROM HistoricoAtendimento h " +
           "WHERE h.data BETWEEN :inicio AND :fim " +
           "GROUP BY h.servico.servicoId, h.servico.nome, h.servico.preco " +
           "ORDER BY quantidade DESC")
    List<Object[]> findServicosMaisVendidos(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // clientes mais frequentes em um intervalo de tempo
    @Query("SELECT h.cliente.clienteId, h.cliente.nome, h.cliente.email, h.cliente.telefone, COUNT(h) as quantidade, SUM(h.pagamento.valor) as valorTotal " +
           "FROM HistoricoAtendimento h " +
           "WHERE h.data BETWEEN :inicio AND :fim " +
           "GROUP BY h.cliente.clienteId, h.cliente.nome, h.cliente.email, h.cliente.telefone " +
           "ORDER BY quantidade DESC")
    List<Object[]> findClientesMaisFrequentes(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

}
