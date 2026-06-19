package com.ifma.barbearia.config;

import com.ifma.barbearia.entity.AdmUser;
import com.ifma.barbearia.repository.AdmUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DataLoaderConfig {

    private static final Logger log = LoggerFactory.getLogger(DataLoaderConfig.class);

    @Value("${ADM_DEFAULT_PASSWORD:#{T(java.util.UUID).randomUUID().toString()}}")
    private String admDefaultPassword;

    @Bean
    public CommandLineRunner loadInitialAdmUser(AdmUserRepository admUserRepository, PasswordEncoder encoder) {
        return args -> {
            if (admUserRepository.findByUsername("admin").isEmpty()) {
                AdmUser adm = new AdmUser();
                adm.setUsername("admin");
                adm.setPassword(encoder.encode(admDefaultPassword));
                adm.setRole("ADM");
                admUserRepository.save(adm);
                log.warn("Usuário ADM inicial criado com senha gerada automaticamente: {}", admDefaultPassword);
                log.warn("Defina ADM_DEFAULT_PASSWORD como variável de ambiente em produção!");
            }
        };
    }
}
