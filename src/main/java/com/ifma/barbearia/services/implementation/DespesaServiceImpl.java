package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.DespesaDto;
import com.ifma.barbearia.entities.Despesa;
import com.ifma.barbearia.mappers.DespesaMapper;
import com.ifma.barbearia.repositories.DespesaRepository;
import com.ifma.barbearia.services.IDespesaService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DespesaServiceImpl implements IDespesaService {

    private DespesaRepository despesaRepository;

    @Override
    public void criarDespesa(DespesaDto despesaDto) {
        Despesa despesa = new Despesa();
        DespesaMapper.mapToEntity(despesaDto, despesa);
        despesaRepository.save(despesa);
    }

    @Override
    public List<DespesaDto> buscarTodasDespesas() {
        List<Despesa> despesas = despesaRepository.findAll();
        return despesas.stream()
                .map(despesa -> DespesaMapper.mapToDto(despesa, new DespesaDto()))
                .collect(Collectors.toList());
    }

}
