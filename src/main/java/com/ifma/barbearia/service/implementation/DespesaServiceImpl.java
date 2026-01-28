package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.DespesaDto;
import com.ifma.barbearia.entity.Despesa;
import com.ifma.barbearia.mapper.DespesaMapper;
import com.ifma.barbearia.repository.DespesaRepository;
import com.ifma.barbearia.service.IDespesaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DespesaServiceImpl implements IDespesaService {

    private final DespesaRepository despesaRepository;
    private final DespesaMapper despesaMapper;

    @Override
    public void criarDespesa(DespesaDto despesaDto) {
        Despesa despesa = despesaMapper.toEntity(despesaDto);
        despesaRepository.save(despesa);
    }

    @Override
    public List<DespesaDto> buscarTodasDespesas() {
        return despesaRepository.findAll().stream()
                .map(despesaMapper::toDto)
                .toList();
    }

}
