package com.ifma.barbearia.services.implementation;

import com.ifma.barbearia.entities.AdmUser;
import com.ifma.barbearia.repositories.AdmUserRepository;
import com.ifma.barbearia.services.IAdmUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdmUserImpl implements IAdmUserService {

    private final AdmUserRepository repo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AdmUserImpl(AdmUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public AdmUser findByUsername(String username) {
        return repo.findByUsername(username).orElse(null);
    }

    @Override
    public boolean passwordMatches(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}
