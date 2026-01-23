package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.PagamentoDto;
import com.ifma.barbearia.entities.Agendamento;
import com.ifma.barbearia.entities.Pagamento;
import com.ifma.barbearia.exceptions.AgendamentoNaoConcluidoException;
import com.ifma.barbearia.exceptions.PagamentoJaExisteException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mappers.PagamentoMapper;
import com.ifma.barbearia.repositories.AgendamentoRepository;
import com.ifma.barbearia.repositories.PagamentoRepository;
import com.ifma.barbearia.services.IPagamentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PagamentoServiceImpl implements IPagamentoService {

    private PagamentoRepository pagamentoRepository;
    private AgendamentoRepository agendamentoRepository;

    @Override
    public void criarPagamento(PagamentoDto pagamentoDto) {
        // Verificar se o agendamento existe
        Agendamento agendamento = agendamentoRepository.findById(pagamentoDto.getAgendamentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Agendamento", "id", pagamentoDto.getAgendamentoId().toString()));

        // Verificar se o agendamento está concluído
        if (!agendamento.getStatus().equals("CONCLUIDO")) {
            throw new AgendamentoNaoConcluidoException("Apenas agendamentos concluídos podem receber pagamento. Status atual: " + agendamento.getStatus());
        }

        // Verificar se já existe pagamento para este agendamento
        Optional<Pagamento> pagamentoExistente = pagamentoRepository.findByAgendamento_AgendamentoId(pagamentoDto.getAgendamentoId());
        if (pagamentoExistente.isPresent()) {
            throw new PagamentoJaExisteException("Já existe um pagamento registrado para este agendamento.");
        }

        // Validar se o valor corresponde ao valor do serviço
        Double valorServico = agendamento.getServico().getPreco();
        if (!pagamentoDto.getValor().equals(valorServico)) {
            throw new IllegalArgumentException("O valor do pagamento (" + pagamentoDto.getValor() + ") deve corresponder ao valor do serviço (" + valorServico + ")");
        }

        // Criar e salvar o pagamento
        Pagamento pagamento = new Pagamento();
        PagamentoMapper.mapToEntity(pagamentoDto, pagamento);
        pagamento.setAgendamento(agendamento);

        pagamentoRepository.save(pagamento);
    }

    @Override
    public PagamentoDto buscarPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", "id", pagamentoId.toString()));

        return PagamentoMapper.mapToDto(pagamento, new PagamentoDto());
    }

    @Override
    public PagamentoDto buscarPagamentoPorAgendamento(Long agendamentoId) {
        Pagamento pagamento = pagamentoRepository.findByAgendamento_AgendamentoId(agendamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", "agendamentoId", agendamentoId.toString()));

        return PagamentoMapper.mapToDto(pagamento, new PagamentoDto());
    }

}
