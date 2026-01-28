package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.BarbeiroDto;
import com.ifma.barbearia.entity.Barbeiro;
import com.ifma.barbearia.exceptions.BarbeiroAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.BarbeiroMapper;
import com.ifma.barbearia.repository.BarbeiroRepository;
import com.ifma.barbearia.service.IBarbeiroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BarbeiroServiceImpl implements IBarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeiroMapper barbeiroMapper;

    @Override
    public void criarBarbeiro(BarbeiroDto barbeiroDto) {
        Optional<Barbeiro> optionalBarbeiro = barbeiroRepository.findByEmail(barbeiroDto.getEmail());
        if (optionalBarbeiro.isPresent()) {
            throw new BarbeiroAlreadyExistsException(
                    "Barbeiro já registrado com esse email: " + barbeiroDto.getEmail());
        }
        Barbeiro barbeiro = barbeiroMapper.toEntity(barbeiroDto);
        barbeiroRepository.save(barbeiro);
    }

    @Override
    public BarbeiroDto buscarBarbeiro(String email) {
        Barbeiro barbeiro = barbeiroRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "email", email));
        return barbeiroMapper.toDto(barbeiro);
    }

    @Override
    public Barbeiro buscarEntidadeBarbeiroPorEmail(String email) {
        return barbeiroRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "email", email));
    }

    @Override
    public List<BarbeiroDto> buscarTodosBarbeiros() {
        return barbeiroRepository.findAll().stream()
                .map(barbeiroMapper::toDto)
                .toList();
    }

    @Override
    public boolean atualizarBarbeiro(BarbeiroDto barbeiroDto) {
        Barbeiro barbeiro = barbeiroRepository.findByEmail(barbeiroDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "email", barbeiroDto.getEmail()));
        barbeiroMapper.updateEntity(barbeiroDto, barbeiro);
        barbeiroRepository.save(barbeiro);
        return true;
    }

    @Override
    public boolean deletarBarbeiro(String email) {
        Barbeiro barbeiro = barbeiroRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "email", email));
        barbeiroRepository.delete(barbeiro);
        return true;
    }

}
