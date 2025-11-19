package com.ifma.barbearia.services;

import com.ifma.barbearia.entities.Agendamento;
import com.ifma.barbearia.entities.HistoricoAtendimento;

import java.util.List;

public interface IHistoricoAtendimentoService {

    void registrar(Agendamento agendamento, Double valorPago);

    List<HistoricoAtendimento> listarPorCliente(Long clienteId);

    List<HistoricoAtendimento> listarPorBarbeiro(Long barbeiroId);

}
