package com.ifma.barbearia.mapper;

import com.ifma.barbearia.dto.BarbeiroDto;
import com.ifma.barbearia.entity.Barbeiro;
import org.springframework.stereotype.Component;

@Component
public class BarbeiroMapper {

    public BarbeiroDto toDto(Barbeiro barbeiro) {
        BarbeiroDto dto = new BarbeiroDto();
        dto.setNome(barbeiro.getNome());
        dto.setEmail(barbeiro.getEmail());
        dto.setTelefone(barbeiro.getTelefone());
        dto.setEspecialidade(barbeiro.getEspecialidade());
        return dto;
    }

    public Barbeiro toEntity(BarbeiroDto dto) {
        Barbeiro barbeiro = new Barbeiro();
        return updateEntity(dto, barbeiro);
    }

    public Barbeiro updateEntity(BarbeiroDto dto, Barbeiro barbeiro) {
        barbeiro.setNome(dto.getNome());
        barbeiro.setEmail(dto.getEmail());
        barbeiro.setTelefone(dto.getTelefone());
        barbeiro.setEspecialidade(dto.getEspecialidade());
        return barbeiro;
    }

}
