package com.ifma.barbearia.repository;

import com.ifma.barbearia.entity.AdmUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmUserRepository extends JpaRepository<AdmUser, Long> {
    Optional<AdmUser> findByUsername(String username);
}
