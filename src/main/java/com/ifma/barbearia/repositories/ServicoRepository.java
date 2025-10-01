package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.Cliente;
import com.ifma.barbearia.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findByNome(String nome);
    Optional<Servico> findByServicoId(Long servicoId);
}