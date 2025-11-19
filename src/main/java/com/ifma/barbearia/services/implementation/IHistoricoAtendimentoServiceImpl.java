package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.entities.Agendamento;
import com.ifma.barbearia.entities.HistoricoAtendimento;
import com.ifma.barbearia.repositories.HistoricoAtendimentoRepository;
import com.ifma.barbearia.services.IHistoricoAtendimentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class IHistoricoAtendimentoServiceImpl implements IHistoricoAtendimentoService {

    private HistoricoAtendimentoRepository historicoAtendimentoRepository;

    @Override
    public void registrar(Agendamento agendamento, Double valorPago) {
        HistoricoAtendimento historicoAtendimento = new HistoricoAtendimento();
        historicoAtendimento.setData(LocalDateTime.now());
        historicoAtendimento.setValorPago(valorPago);
        historicoAtendimento.setCliente(agendamento.getCliente());
        historicoAtendimento.setBarbeiro(agendamento.getBarbeiro());
        historicoAtendimento.setServico(agendamento.getServico());

        historicoAtendimentoRepository.save(historicoAtendimento);
    }

    @Override
    public List<HistoricoAtendimento> listarPorCliente(Long clienteId) {
        return historicoAtendimentoRepository.findByCliente_ClienteId(clienteId);
    }

    @Override
    public List<HistoricoAtendimento> listarPorBarbeiro(Long barbeiroId) {
        return historicoAtendimentoRepository.findByBarbeiro_BarbeiroId(barbeiroId);
    }
}
