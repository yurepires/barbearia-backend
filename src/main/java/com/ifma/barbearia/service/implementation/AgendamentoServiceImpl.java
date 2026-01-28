package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.entity.*;
import com.ifma.barbearia.entity.enums.StatusAgendamento;
import com.ifma.barbearia.exceptions.*;
import com.ifma.barbearia.mapper.AgendamentoMapper;
import com.ifma.barbearia.repository.AgendamentoRepository;
import com.ifma.barbearia.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AgendamentoServiceImpl implements IAgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final IClienteService clienteService;
    private final IServicoService servicoService;
    private final IBarbeiroService barbeiroService;
    private final IPagamentoService pagamentoService;
    private final IHistoricoAtendimentoService historicoAtendimentoService;
    private final AgendamentoMapper agendamentoMapper;

    @Override
    public void criarAgendamento(AgendamentoDto agendamentoDto) {
        Cliente cliente = clienteService.buscarEntidadeClientePorEmail(agendamentoDto.getClienteEmail());
        Servico servico = servicoService.buscarEntidadeServicoPorId(agendamentoDto.getServicoId());
        Barbeiro barbeiro = barbeiroService.buscarEntidadeBarbeiroPorEmail(agendamentoDto.getBarbeiroEmail());

        validarHorario(agendamentoDto.getHorario());

        boolean mesmoBarbeiroHorario = agendamentoRepository.existsByBarbeiro_BarbeiroIdAndHorarioAndStatus(
                barbeiro.getBarbeiroId(), agendamentoDto.getHorario(), StatusAgendamento.PENDENTE);

        if (mesmoBarbeiroHorario) {
            throw new HorarioIndisponivelException("Já existe um agendamento para este barbeiro neste horário.");
        }

        Agendamento agendamento = agendamentoMapper.toEntity(agendamentoDto, cliente, servico, barbeiro);
        agendamento.setStatus(StatusAgendamento.PENDENTE);

        agendamentoRepository.save(agendamento);
    }

    @Override
    public AgendamentoDto buscarAgendamento(Long agendamentoId) {
        Agendamento agendamento = verificarAgendamento(agendamentoId);
        return agendamentoMapper.toDto(agendamento);
    }

    @Override
    public List<AgendamentoDto> buscarTodosAgendamentos() {
        return agendamentoRepository.findAll().stream()
                .map(agendamentoMapper::toDto)
                .toList();
    }

    @Override
    public List<AgendamentoDto> buscarAgendamentosPorCliente(String clienteEmail) {
        Cliente cliente = clienteService.buscarEntidadeClientePorEmail(clienteEmail);

        List<Agendamento> agendamentos = agendamentoRepository.findByCliente_ClienteId(cliente.getClienteId());
        return agendamentos.stream()
                .map(agendamentoMapper::toDto)
                .toList();
    }

    @Override
    public List<AgendamentoDto> buscarAgendamentosPorIntervaloDeDatas(LocalDate inicio, LocalDate fim) {
        return agendamentoRepository.findByHorarioBetween(inicio.atStartOfDay(), fim.atTime(23, 59, 59))
                .stream()
                .map(agendamentoMapper::toDto)
                .toList();
    }

    @Override
    public boolean atualizarAgendamento(AgendamentoDto agendamentoDto) {
        Agendamento agendamento = verificarAgendamento(agendamentoDto.getId());

        Cliente cliente = clienteService.buscarEntidadeClientePorEmail(agendamentoDto.getClienteEmail());
        Servico servico = servicoService.buscarEntidadeServicoPorId(agendamentoDto.getServicoId());
        Barbeiro barbeiro = barbeiroService.buscarEntidadeBarbeiroPorEmail(agendamentoDto.getBarbeiroEmail());

        agendamentoMapper.updateEntity(agendamentoDto, agendamento, cliente, servico, barbeiro);
        agendamentoRepository.save(agendamento);
        return true;
    }

    @Override
    public boolean cancelarAgendamento(Long agendamentoId) {
        Agendamento agendamento = verificarAgendamento(agendamentoId);

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new CancelamentoInvalidoException("Não é possível cancelar um agendamento já concluído.");
        }

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new CancelamentoInvalidoException("Este argumento já foi cancelado.");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
        return true;
    }

    @Override
    @Transactional
    public void concluirAgendamento(Long agendamentoId, String formaPagamento) {
        Agendamento agendamento = verificarAgendamento(agendamentoId);

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new ConclusaoInvalidaException("Não é possível concluir um agendamento cancelado.");
        }

        if (agendamento.getStatus() == StatusAgendamento.CONCLUIDO) {
            throw new ConclusaoInvalidaException("Este agendamento já está concluído.");
        }

        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamentoRepository.save(agendamento);

        Pagamento pagamento = new Pagamento();
        pagamento.setAgendamento(agendamento);
        pagamento.setValor(agendamento.getServico().getPreco());
        pagamento.setFormaPagamento(formaPagamento);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamentoService.salvarPagamento(pagamento);

        historicoAtendimentoService.registrar(agendamento, pagamento);
    }

    private Agendamento verificarAgendamento(Long agendamentoId) {
        return agendamentoRepository.findById(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", "id", agendamentoId.toString()));
    }

    private void validarHorario(LocalDateTime horario) {

        LocalTime aberturaDaBarbearia = LocalTime.of(7, 0);
        LocalTime fechamentoDaBarbearia = LocalTime.of(21, 0);

        if (horario.toLocalTime().isBefore(aberturaDaBarbearia)
                || horario.toLocalTime().isAfter(fechamentoDaBarbearia)) {
            throw new AgendamentoInvalidoException("Horário fora do expediente da barbearia (07:00–21:00).");
        }

        if (horario.getMinute() % 30 != 0) {
            throw new AgendamentoInvalidoException("O horário deve ser em intervalos de 30 minutos.");
        }
    }
}
