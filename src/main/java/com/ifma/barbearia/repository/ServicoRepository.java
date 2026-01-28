package com.ifma.barbearia.repository;

import com.ifma.barbearia.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findByNome(String nome);
    Optional<Servico> findByServicoId(Long servicoId);
}