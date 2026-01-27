package com.ifma.barbearia.services;

import com.ifma.barbearia.entities.Agendamento;
import com.ifma.barbearia.entities.HistoricoAtendimento;
import com.ifma.barbearia.entities.Pagamento;

import java.time.LocalDate;
import java.util.List;

public interface IHistoricoAtendimentoService {

    void registrar(Agendamento agendamento, Pagamento pagamento);

    List<HistoricoAtendimento> listarTodos();

    List<HistoricoAtendimento> listarPorCliente(String clienteEmail);

    List<HistoricoAtendimento> listarPorBarbeiro(String barbeiroEmail);

    List<HistoricoAtendimento> listarPorServico(Long servicoId);

    List<HistoricoAtendimento> listarPorIntervaloDeDatas(LocalDate inicio, LocalDate fim);

}
