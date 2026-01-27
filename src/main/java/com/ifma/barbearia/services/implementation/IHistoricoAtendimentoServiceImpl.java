package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.entities.Agendamento;
import com.ifma.barbearia.entities.HistoricoAtendimento;
import com.ifma.barbearia.entities.Pagamento;
import com.ifma.barbearia.repositories.HistoricoAtendimentoRepository;
import com.ifma.barbearia.services.IHistoricoAtendimentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class IHistoricoAtendimentoServiceImpl implements IHistoricoAtendimentoService {

    private HistoricoAtendimentoRepository historicoAtendimentoRepository;

    @Override
    public void registrar(Agendamento agendamento, Pagamento pagamento) {
        HistoricoAtendimento historicoAtendimento = new HistoricoAtendimento();
        historicoAtendimento.setData(LocalDateTime.now());
        historicoAtendimento.setPagamento(pagamento);
        historicoAtendimento.setCliente(agendamento.getCliente());
        historicoAtendimento.setBarbeiro(agendamento.getBarbeiro());
        historicoAtendimento.setServico(agendamento.getServico());

        historicoAtendimentoRepository.save(historicoAtendimento);
    }

    @Override
    public List<HistoricoAtendimento> listarTodos() {
        return historicoAtendimentoRepository.findAll();
    }

    @Override
    public List<HistoricoAtendimento> listarPorCliente(String clienteEmail) {
        return historicoAtendimentoRepository.findByCliente_Email(clienteEmail);
    }

    @Override
    public List<HistoricoAtendimento> listarPorBarbeiro(String barbeiroEmail) {
        return historicoAtendimentoRepository.findByBarbeiro_Email(barbeiroEmail);
    }

    @Override
    public List<HistoricoAtendimento> listarPorServico(Long servicoId) {
        return historicoAtendimentoRepository.findByServico_ServicoId(servicoId);
    }

    @Override
    public List<HistoricoAtendimento> listarPorIntervaloDeDatas(LocalDate inicio, LocalDate fim) {
        return historicoAtendimentoRepository.findByDataBetween(inicio.atStartOfDay(), fim.atTime(23,59,59));
    }
}
