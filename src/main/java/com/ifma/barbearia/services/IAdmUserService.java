package com.ifma.barbearia.services;

import com.ifma.barbearia.entities.AdmUser;

public interface IAdmUserService {
    AdmUser findByUsername(String username);

    boolean passwordMatches(String raw, String encoded);

}
