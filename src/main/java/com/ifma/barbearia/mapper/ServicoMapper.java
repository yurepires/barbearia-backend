package com.ifma.barbearia.mapper;

import com.ifma.barbearia.dto.ServicoDto;
import com.ifma.barbearia.entity.Servico;
import org.springframework.stereotype.Component;

@Component
public class ServicoMapper {

    public ServicoDto toDto(Servico servico) {
        ServicoDto dto = new ServicoDto();
        dto.setServicoId(servico.getServicoId());
        dto.setNome(servico.getNome());
        dto.setPreco(servico.getPreco());
        dto.setDescricao(servico.getDescricao());
        return dto;
    }

    public Servico toEntity(ServicoDto dto) {
        Servico servico = new Servico();
        return updateEntity(dto, servico);
    }

    public Servico updateEntity(ServicoDto dto, Servico servico) {
        servico.setServicoId(dto.getServicoId());
        servico.setNome(dto.getNome());
        servico.setPreco(dto.getPreco());
        servico.setDescricao(dto.getDescricao());
        return servico;
    }

}
