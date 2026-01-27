package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.PagamentoDto;
import com.ifma.barbearia.entities.Pagamento;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mappers.PagamentoMapper;
import com.ifma.barbearia.repositories.PagamentoRepository;
import com.ifma.barbearia.services.IPagamentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PagamentoServiceImpl implements IPagamentoService {

    private PagamentoRepository pagamentoRepository;

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
