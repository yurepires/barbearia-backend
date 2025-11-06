package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.DTOs.BarbeiroDto;
import com.ifma.barbearia.entities.Barbeiro;
import com.ifma.barbearia.exceptions.BarbeiroAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mappers.BarbeiroMapper;
import com.ifma.barbearia.repositories.BarbeiroRepository;
import com.ifma.barbearia.services.IBarbeiroService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BarbeiroServiceImpl implements IBarbeiroService {
    private BarbeiroRepository barbeiroRepository;

    @Override
    public void criarBarbeiro(BarbeiroDto barbeiroDto) {
        Optional<Barbeiro> optionalBarbeiro = barbeiroRepository.findByEmail(barbeiroDto.getEmail());
        if (optionalBarbeiro.isPresent()) {
            throw new BarbeiroAlreadyExistsException("Barbeiro já registrado com esse email: " + barbeiroDto.getEmail());
        }
        Barbeiro barbeiro = BarbeiroMapper.mapToBarbeiro(barbeiroDto, new Barbeiro());
        barbeiroRepository.save(barbeiro);
    }

    @Override
    public BarbeiroDto buscarBarbeiro(String email) {
        Barbeiro barbeiro = barbeiroRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "email", email));
        return BarbeiroMapper.mapToBarbeiroDto(barbeiro, new BarbeiroDto());
    }

    @Override
    public List<BarbeiroDto> buscarTodosBarbeiros() {
        return barbeiroRepository.findAll().stream()
                .map(barbeiro -> BarbeiroMapper.mapToBarbeiroDto(barbeiro, new BarbeiroDto()))
                .toList();
    }

    @Override
    public boolean atualizarBarbeiro(BarbeiroDto barbeiroDto) {
        Barbeiro barbeiro = barbeiroRepository.findByEmail(barbeiroDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "email", barbeiroDto.getEmail()));
        BarbeiroMapper.mapToBarbeiro(barbeiroDto, barbeiro);
        barbeiroRepository.save(barbeiro);
        return true;
    }

    @Override
    public boolean deletarBarbeiro(String email) {
        Barbeiro barbeiro = barbeiroRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Barbeiro", "email", email));
        barbeiroRepository.delete(barbeiro);
        return true;
    }

}
