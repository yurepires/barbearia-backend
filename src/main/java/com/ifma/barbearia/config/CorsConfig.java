package com.ifma.barbearia.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:5173",           // Vite dev server
                        "http://localhost:3000",           // Possível outro frontend
                        "https://*.vercel.app",             // Produção Vercel 
                        "https://barbearia-six-beige.vercel.app" // Produção Vercel
                        
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600); // Cache da configuração CORS por 1 hora
    }
}
