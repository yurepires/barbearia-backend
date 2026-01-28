package com.ifma.barbearia.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testes unitários para JwtUtil.
 * Valida geração, extração e validação de tokens JWT.
 */
class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Secret de teste com pelo menos 32 caracteres para HS256
    private static final String TEST_SECRET = "chave-secreta-de-teste-com-32-chars!";

    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        // Injetar o secret via reflection (simulando @Value)
        Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, TEST_SECRET);

        // Chamar init() para inicializar a SecretKey
        jwtUtil.init();
    }

    @Test
    @DisplayName("Deve gerar token válido com username e role")
    void deveGerarTokenValidoComUsernameERole() {
        // Arrange
        String username = "usuario@teste.com";
        String role = "CLIENTE";

        // Act
        String token = jwtUtil.generateToken(username, role);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT tem 3 partes
    }

    @Test
    @DisplayName("Deve extrair username do token")
    void deveExtrairUsernameDoToken() {
        // Arrange
        String username = "usuario@teste.com";
        String token = jwtUtil.generateToken(username, "CLIENTE");

        // Act
        String extractedUsername = jwtUtil.extractUsername(token);

        // Assert
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    @DisplayName("Deve extrair role do token")
    void deveExtrairRoleDoToken() {
        // Arrange
        String role = "ADM";
        String token = jwtUtil.generateToken("admin@teste.com", role);

        // Act
        String extractedRole = jwtUtil.extractRole(token);

        // Assert
        assertThat(extractedRole).isEqualTo(role);
    }

    @Test
    @DisplayName("Deve retornar true para token válido")
    void deveRetornarTrueParaTokenValido() {
        // Arrange
        String token = jwtUtil.generateToken("usuario@teste.com", "CLIENTE");

        // Act
        boolean isValid = jwtUtil.isTokenValid(token);

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false para token inválido")
    void deveRetornarFalseParaTokenInvalido() {
        // Arrange
        String tokenInvalido = "token.invalido.aqui";

        // Act
        boolean isValid = jwtUtil.isTokenValid(tokenInvalido);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Deve retornar false para token nulo")
    void deveRetornarFalseParaTokenNulo() {
        // Act
        boolean isValid = jwtUtil.isTokenValid(null);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Deve extrair todas as claims do token")
    void deveExtrairTodasAsClaimsDoToken() {
        // Arrange
        String username = "usuario@teste.com";
        String role = "CLIENTE";
        String token = jwtUtil.generateToken(username, role);

        // Act
        Claims claims = jwtUtil.extractAllClaims(token);

        // Assert
        assertThat(claims.getSubject()).isEqualTo(username);
        assertThat(claims.get("role", String.class)).isEqualTo(role);
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
    }
}
