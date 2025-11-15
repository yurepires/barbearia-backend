package com.ifma.barbearia.repositories;

import com.ifma.barbearia.entities.AdmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmUserRepository extends JpaRepository<AdmUser, Long> {
    Optional<AdmUser> findByUsername(String username);
}
