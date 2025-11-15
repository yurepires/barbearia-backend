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
                        .requestMatchers("/api/admin/login").permitAll()
                        .requestMatchers("/api/cliente/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/barbeiro/**", "/api/servico/**")
                        .hasAuthority("ADM")
                        .requestMatchers(HttpMethod.PUT, "/api/barbeiro/**", "/api/cliente/**", "/api/servico/**")
                        .hasAuthority("ADM")
                        .requestMatchers(HttpMethod.DELETE, "/api/barbeiro/**", "/api/cliente/**", "/api/servico/**")
                        .hasAuthority("ADM")

                        .anyRequest().permitAll())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> response
                                .sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")))
                .addFilterBefore(new JwtAuthFilter(),
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    static class JwtAuthFilter extends OncePerRequestFilter {

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
