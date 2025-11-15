package com.ifma.barbearia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    private static final String SECRET = "chave-ultra-mega-secreta-da-barbearia";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION = 86400000; // 1 dia em ms

    // Criação do token JWT
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Parse e validação
    public static Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public static String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public static boolean isTokenValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
