package com.ifma.barbearia.repository;

import com.ifma.barbearia.entity.ClienteOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteOtpRepository extends JpaRepository<ClienteOtp, Long> {
    Optional<ClienteOtp> findFirstByEmailAndOtpAndUsedFalseOrderByExpirationDesc(String email, String otp);

    Optional<ClienteOtp> findFirstByEmailAndUsedFalseOrderByExpirationDesc(String email);
}
