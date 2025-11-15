package com.ifma.barbearia.mappers;

import com.ifma.barbearia.DTOs.BarbeiroDto;
import com.ifma.barbearia.entities.Barbeiro;

public class BarbeiroMapper {
    public static BarbeiroDto mapToBarbeiroDto(Barbeiro barbeiro, BarbeiroDto barbeiroDto) {
        barbeiroDto.setNome(barbeiro.getNome());
        barbeiroDto.setEmail(barbeiro.getEmail());
        barbeiroDto.setTelefone(barbeiro.getTelefone());
        return barbeiroDto;
    }

    public static Barbeiro mapToBarbeiro(BarbeiroDto barbeiroDto, Barbeiro barbeiro) {
        barbeiro.setNome(barbeiroDto.getNome());
        barbeiro.setEmail(barbeiroDto.getEmail());
        barbeiro.setTelefone(barbeiroDto.getTelefone());
        return barbeiro;
    }
}
