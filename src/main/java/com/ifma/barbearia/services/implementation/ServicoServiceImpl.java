package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.ServicoDto;
import com.ifma.barbearia.entities.Servico;
import com.ifma.barbearia.exceptions.ServicoAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mappers.ServicoMapper;
import com.ifma.barbearia.repositories.ServicoRepository;
import com.ifma.barbearia.services.IServicoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ServicoServiceImpl implements IServicoService {

    private ServicoRepository servicoRepository;

    @Override
    public void criarServico(ServicoDto servicoDto) {
        // Exemplo: verifica se já existe serviço com mesmo nome
        Optional<Servico> optionalServico = servicoRepository.findByNome(servicoDto.getNome());
        if (optionalServico.isPresent()) {
            throw new ServicoAlreadyExistsException("Serviço já registrado com esse nome: " + servicoDto.getNome());
        }
        Servico servico = ServicoMapper.mapToServico(servicoDto, new Servico());
        servicoRepository.save(servico);
    }

    @Override
    public ServicoDto buscarServico(Long servicoId) {
        Servico servico = servicoRepository.findByServicoId(servicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", "id", Long.toString(servicoId)));
        return ServicoMapper.mapToServicoDto(servico, new ServicoDto());
    }

    @Override
    public boolean atualizarServico(ServicoDto servicoDto) {
        // Aqui, espera-se que o DTO tenha o id do serviço
        Servico servico = servicoRepository.findByServicoId(servicoDto.getServicoId())
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", "id", Long.toString(servicoDto.getServicoId())));
        ServicoMapper.mapToServico(servicoDto, servico);
        servicoRepository.save(servico);
        return true;
    }

    @Override
    public boolean deletarServico(Long servicoId) {
        Servico servico = servicoRepository.findByServicoId(servicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", "id", Long.toString(servicoId)));
        servicoRepository.delete(servico);
        return true;
    }
}
