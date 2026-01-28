package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.entity.AdmUser;
import com.ifma.barbearia.repository.AdmUserRepository;
import com.ifma.barbearia.service.IAdmUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdmUserImpl implements IAdmUserService {

    private final AdmUserRepository repo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdmUser findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }

    @Override
    public boolean passwordMatches(String raw, String encoded) {
        return passwordEncoder.matches(raw, encoded);
    }
}
