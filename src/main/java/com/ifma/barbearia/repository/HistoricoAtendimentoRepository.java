package com.ifma.barbearia.repository;

import com.ifma.barbearia.entity.HistoricoAtendimento;
import com.ifma.barbearia.repository.projections.ClienteMaisFrequenteProjection;
import com.ifma.barbearia.repository.projections.ServicoMaisVendidoProjection;
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
       @Query("SELECT h.servico.servicoId AS servicoId, h.servico.nome AS nomeServico, h.servico.preco AS preco, COUNT(h) AS quantidadeVendas, SUM(h.pagamento.valor) AS valorTotalArrecadado " +
                     "FROM HistoricoAtendimento h " +
                     "WHERE h.data BETWEEN :inicio AND :fim " +
                     "GROUP BY h.servico.servicoId, h.servico.nome, h.servico.preco " +
                     "ORDER BY quantidadeVendas DESC")
       List<ServicoMaisVendidoProjection> findServicosMaisVendidos(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

       // clientes mais frequentes em um intervalo de tempo
       @Query("SELECT h.cliente.clienteId AS clienteId, h.cliente.nome AS nomeCliente, h.cliente.email AS emailCliente, h.cliente.telefone AS telefoneCliente, COUNT(h) AS quantidadeAtendimentos, SUM(h.pagamento.valor) AS valorTotalGasto " +
                     "FROM HistoricoAtendimento h " +
                     "WHERE h.data BETWEEN :inicio AND :fim " +
                     "GROUP BY h.cliente.clienteId, h.cliente.nome, h.cliente.email, h.cliente.telefone " +
                     "ORDER BY quantidadeAtendimentos DESC")
       List<ClienteMaisFrequenteProjection> findClientesMaisFrequentes(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

}
