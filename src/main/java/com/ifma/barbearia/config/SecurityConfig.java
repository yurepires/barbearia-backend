package com.ifma.barbearia.config;

import com.ifma.barbearia.security.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(c -> {
                })
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/barbeiros/**", "/servicos/**")
                        .hasAuthority("ADM")
                        .requestMatchers(HttpMethod.PUT, "/barbeiros/**", "/clientes/**", "/servicos/**")
                        .hasAuthority("ADM")
                        .requestMatchers(HttpMethod.DELETE, "/barbeiros/**", "/clientes/**", "/servicos/**")
                        .hasAuthority("ADM")
                        .anyRequest().permitAll())
                .addFilterBefore(new JwtAuthFilter(jwtUtil),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    static class JwtAuthFilter extends OncePerRequestFilter {
        private final JwtUtil jwtUtil;

        public JwtAuthFilter(JwtUtil jwtUtil) {
            this.jwtUtil = jwtUtil;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                FilterChain filterChain) throws ServletException, IOException {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    if (JwtUtil.isTokenValid(token)) {
                        String username = JwtUtil.extractUsername(token);
                        String role = JwtUtil.extractRole(token);
                        var authentication = new UsernamePasswordAuthenticationToken(username, null,
                                Collections.singleton(() -> role));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (JwtException ignored) {
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
