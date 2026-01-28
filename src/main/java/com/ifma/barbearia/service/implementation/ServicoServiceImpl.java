package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ServicoDto;
import com.ifma.barbearia.entity.Servico;
import com.ifma.barbearia.exceptions.ServicoAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.ServicoMapper;
import com.ifma.barbearia.repository.ServicoRepository;
import com.ifma.barbearia.service.IServicoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ServicoServiceImpl implements IServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMapper servicoMapper;

    @Override
    public void criarServico(ServicoDto servicoDto) {
        Optional<Servico> optionalServico = servicoRepository.findByNome(servicoDto.getNome());
        if (optionalServico.isPresent()) {
            throw new ServicoAlreadyExistsException("Serviço já registrado com esse nome: " + servicoDto.getNome());
        }
        Servico servico = servicoMapper.toEntity(servicoDto);
        servicoRepository.save(servico);
    }

    @Override
    public ServicoDto buscarServico(Long servicoId) {
        Servico servico = servicoRepository.findByServicoId(servicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", "id", Long.toString(servicoId)));
        return servicoMapper.toDto(servico);
    }

    @Override
    public Servico buscarEntidadeServicoPorId(Long servicoId) {
        return servicoRepository.findByServicoId(servicoId)
                .orElseThrow(() -> new ResourceNotFoundException("Serviço", "id", Long.toString(servicoId)));
    }

    @Override
    public List<ServicoDto> buscarTodosServicos() {
        return servicoRepository.findAll().stream()
                .map(servicoMapper::toDto)
                .toList();
    }

    @Override
    public boolean atualizarServico(ServicoDto servicoDto) {
        Servico servico = servicoRepository.findByServicoId(servicoDto.getServicoId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("Serviço", "id", Long.toString(servicoDto.getServicoId())));
        servicoMapper.updateEntity(servicoDto, servico);
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
