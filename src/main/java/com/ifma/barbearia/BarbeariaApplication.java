package com.ifma.barbearia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
public class BarbeariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarbeariaApplication.class, args);
	}

}
