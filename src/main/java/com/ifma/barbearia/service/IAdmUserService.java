package com.ifma.barbearia.service;

import com.ifma.barbearia.entity.AdmUser;

public interface IAdmUserService {
    AdmUser findByUsername(String username);

    boolean passwordMatches(String raw, String encoded);

}
