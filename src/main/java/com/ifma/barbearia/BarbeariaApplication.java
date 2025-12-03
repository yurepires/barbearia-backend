package com.ifma.barbearia;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAware")
@OpenAPIDefinition(
		info = @Info(
				title = "Barbearia REST API Documentation",
				description = "Barbearia do seu Marcos REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Barbearia",
						email = "barbearia@example.com",
						url = "https://github.com/yurepires/barbearia-backend"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.apache.org/licenses/LICENSE-2.0"
				)
		)
)
public class BarbeariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarbeariaApplication.class, args);
	}

}
