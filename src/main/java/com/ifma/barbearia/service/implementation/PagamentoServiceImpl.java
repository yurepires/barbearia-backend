package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.entity.Pagamento;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.PagamentoMapper;
import com.ifma.barbearia.repository.PagamentoRepository;
import com.ifma.barbearia.service.IPagamentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PagamentoServiceImpl implements IPagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PagamentoMapper pagamentoMapper;

    @Override
    public PagamentoDto buscarPagamento(Long pagamentoId) {
        Pagamento pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", "id", pagamentoId.toString()));

        return pagamentoMapper.toDto(pagamento);
    }

    @Override
    public PagamentoDto buscarPagamentoPorAgendamento(Long agendamentoId) {
        Pagamento pagamento = pagamentoRepository.findByAgendamento_AgendamentoId(agendamentoId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Pagamento", "agendamentoId", agendamentoId.toString()));

        return pagamentoMapper.toDto(pagamento);
    }

    @Override
    public Pagamento salvarPagamento(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

}
