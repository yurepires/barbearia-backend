package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.AgendamentoDto;
import com.ifma.barbearia.constants.AgendamentoConstants;
import com.ifma.barbearia.entities.Agendamento;
import com.ifma.barbearia.entities.Barbeiro;
import com.ifma.barbearia.entities.Cliente;
import com.ifma.barbearia.entities.Servico;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mappers.AgendamentoMapper;
import com.ifma.barbearia.repositories.AgendamentoRepository;
import com.ifma.barbearia.repositories.BarbeiroRepository;
import com.ifma.barbearia.repositories.ClienteRepository;
import com.ifma.barbearia.repositories.ServicoRepository;
import com.ifma.barbearia.services.IAgendamentoService;
import com.ifma.barbearia.services.IHistoricoAtendimentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AgendamentoServiceImpl implements IAgendamentoService {

    private AgendamentoRepository agendamentoRepository;
    private ClienteRepository clienteRepository;
    private ServicoRepository servicoRepository;
    private BarbeiroRepository barbeiroRepository;
    private IHistoricoAtendimentoService iHistoricoAtendimentoService;

    @Override
    public void criarAgendamento(AgendamentoDto agendamentoDto) {
        Cliente cliente = verificarClienteId(agendamentoDto.getClienteId());
        Servico servico = verificarServicoId(agendamentoDto.getServicoId());
        Barbeiro barbeiro = verificarBarbeiroId(agendamentoDto.getBarbeiroId());

        validarHorario(agendamentoDto.getHorario());

        boolean mesmoBarbeiroHorario = agendamentoRepository.existsByBarbeiro_BarbeiroIdAndHorario(agendamentoDto.getBarbeiroId(), agendamentoDto.getHorario());

        if (mesmoBarbeiroHorario) {
            throw new IllegalArgumentException("Já existe um agendamento para este barbeiro neste horário.");
        }

        Agendamento agendamento = AgendamentoMapper.mapToAgendamento(agendamentoDto, new Agendamento(), cliente, servico, barbeiro);
        agendamento.setStatus(AgendamentoConstants.STATUS_PENDENTE);

        agendamentoRepository.save(agendamento);
    }

    @Override
    public AgendamentoDto buscarAgendamento(Long agendamentoId) {
        Agendamento agendamento = verificarAgendamento(agendamentoId);
        return AgendamentoMapper.mapToAgendamentoDto(agendamento, new AgendamentoDto());
    }

    @Override
    public List<AgendamentoDto> buscarTodosAgendamentos() {
        return agendamentoRepository.findAll().stream()
                .map(agendamento -> AgendamentoMapper.mapToAgendamentoDto(agendamento, new AgendamentoDto()))
                .toList();
    }

    @Override
    public List<AgendamentoDto> buscarAgendamentosPorCliente(Long clienteId) {
        Cliente cliente = verificarClienteId(clienteId);

        List<Agendamento> agendamentos = agendamentoRepository.findByCliente_ClienteId(clienteId);
        return agendamentos.stream()
                .map((Agendamento agendamento) -> AgendamentoMapper.mapToAgendamentoDto(agendamento, new AgendamentoDto()))
                .toList();
    }

    @Override
    public boolean atualizarAgendamento(AgendamentoDto agendamentoDto) {
        Agendamento agendamento = verificarAgendamento(agendamentoDto.getId());

        Cliente cliente = verificarClienteId(agendamentoDto.getClienteId());
        Servico servico = verificarServicoId(agendamentoDto.getServicoId());
        Barbeiro barbeiro = verificarBarbeiroId(agendamentoDto.getBarbeiroId());

        AgendamentoMapper.mapToAgendamento(agendamentoDto, agendamento, cliente, servico, barbeiro);
        agendamentoRepository.save(agendamento);
        return true;
    }

    @Override
    public boolean deletarAgendamento(Long agendamentoId) {
        Agendamento agendamento = verificarAgendamento(agendamentoId);
        agendamentoRepository.delete(agendamento);
        return true;
    }

    @Override
    public void concluirAgendamento(Long agendamentoId) {
        Agendamento agendamento = verificarAgendamento(agendamentoId);

        if (agendamento.getStatus().equals(AgendamentoConstants.STATUS_CANCELADO)) {
            throw new IllegalArgumentException("Não é possível concluir um agendamento cancelado.");
        }

        if (agendamento.getStatus().equals(AgendamentoConstants.STATUS_CONCLUIDO)) {
            throw new IllegalArgumentException("Este agendamento já está concluído.");
        }

        agendamento.setStatus(AgendamentoConstants.STATUS_CONCLUIDO);

        iHistoricoAtendimentoService.registrar(agendamento, agendamento.getServico().getPreco());

    }


    private Agendamento verificarAgendamento(Long agendamentoId) {
        return agendamentoRepository.findById(agendamentoId).orElseThrow(() -> new ResourceNotFoundException("Agendamento", "id", agendamentoId.toString()));
    }

    private Cliente verificarClienteId(Long clienteId) {
        return clienteRepository.findById(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente", "id", clienteId.toString()));
    }

    private Servico verificarServicoId(Long ServicoId) {
        return servicoRepository.findById(ServicoId).orElseThrow(() -> new ResourceNotFoundException("Servico", "id", ServicoId.toString()));
    }

    private Barbeiro verificarBarbeiroId(Long BarbeiroId) {
        return barbeiroRepository.findById(BarbeiroId).orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "id", BarbeiroId.toString()));
    }


    private void validarHorario(LocalDateTime horario) {

        LocalTime aberturaDaBarbearia = LocalTime.of(7, 0);
        LocalTime FechamentoDaBarbearia = LocalTime.of(21, 0);

        if (horario.toLocalTime().isBefore(aberturaDaBarbearia) || horario.toLocalTime().isAfter(FechamentoDaBarbearia)) {
            throw new IllegalArgumentException("Horário fora do expediente da barbearia (07:00–21:00).");
        }

        if (horario.getMinute() % 30 != 0) {
            throw new IllegalArgumentException("O horário deve ser em intervalos de 30 minutos.");
        }
    }
}

