package com.ifma.barbearia.mappers;

import com.ifma.barbearia.DTOs.ServicoDto;
import com.ifma.barbearia.entities.Servico;

public class ServicoMapper {

    public static ServicoDto mapToServicoDto(Servico servico, ServicoDto servicoDto) {
        servicoDto.setServicoId(servico.getServicoId());
        servicoDto.setNome(servico.getNome());
        servicoDto.setPreco(servico.getPreco());
        servicoDto.setDescricao(servico.getDescricao());
        return servicoDto;
    }
    public static Servico mapToServico(ServicoDto servicoDto, Servico servico) {
        servico.setServicoId(servicoDto.getServicoId());
        servico.setNome(servicoDto.getNome());
        servico.setPreco(servicoDto.getPreco());
        servico.setDescricao(servicoDto.getDescricao());
        return servico;
    }
}
