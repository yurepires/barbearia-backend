package com.ifma.barbearia.config;

import com.ifma.barbearia.entity.AdmUser;
import com.ifma.barbearia.repository.AdmUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataLoaderConfig {

    @Bean
    public CommandLineRunner loadInitialAdmUser(AdmUserRepository admUserRepository) {
        return args -> {
            if (admUserRepository.findByUsername("admin").isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                AdmUser adm = new AdmUser();
                adm.setUsername("admin");
                adm.setPassword(encoder.encode("admin")); // senha segura
                adm.setRole("ADM");
                admUserRepository.save(adm);
                System.out.println("Usuário ADM inicial criado!");
            }
        };
    }
}
