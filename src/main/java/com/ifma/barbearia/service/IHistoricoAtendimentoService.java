package com.ifma.barbearia.service;

import com.ifma.barbearia.entity.Agendamento;
import com.ifma.barbearia.entity.HistoricoAtendimento;
import com.ifma.barbearia.entity.Pagamento;

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
