package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.AuthRequest;
import com.ifma.barbearia.dto.AuthResponse;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.repository.ClienteRepository;
import com.ifma.barbearia.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteAuthServiceImpl - Testes Unitários")
class ClienteAuthServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ClienteAuthServiceImpl clienteAuthService;

    private Cliente cliente;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setEmail("joao@email.com");
        cliente.setSenha("$2a$10$hashGerado");

        authRequest = new AuthRequest();
        authRequest.setUsername("joao@email.com");
        authRequest.setPassword("minhasenha");
    }

    @Nested
    @DisplayName("autenticarComSenha()")
    class AutenticarComSenha {

        @Test
        @DisplayName("Deve autenticar com sucesso e retornar token JWT")
        void deveAutenticarComSucessoERetornarToken() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));
            given(passwordEncoder.matches("minhasenha", "$2a$10$hashGerado"))
                    .willReturn(true);
            given(jwtUtil.generateToken("joao@email.com", "CLIENTE"))
                    .willReturn("jwt-token-gerado");

            // when
            AuthResponse resultado = clienteAuthService.autenticarComSenha(authRequest);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getToken()).isEqualTo("jwt-token-gerado");
            verify(jwtUtil, times(1)).generateToken("joao@email.com", "CLIENTE");
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando cliente não existe")
        void deveLancarExcecaoQuandoClienteNaoExiste() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clienteAuthService.autenticarComSenha(authRequest))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(passwordEncoder, never()).matches(anyString(), anyString());
            verify(jwtUtil, never()).generateToken(anyString(), anyString());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando senha é inválida")
        void deveLancarExcecaoQuandoSenhaInvalida() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));
            given(passwordEncoder.matches("minhasenha", "$2a$10$hashGerado"))
                    .willReturn(false);

            // when & then
            assertThatThrownBy(() -> clienteAuthService.autenticarComSenha(authRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Credenciais inválidas");

            verify(jwtUtil, never()).generateToken(anyString(), anyString());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando senha do cliente é nula")
        void deveLancarExcecaoQuandoSenhaDoClienteEhNula() {
            // given
            cliente.setSenha(null);
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));

            // when & then
            assertThatThrownBy(() -> clienteAuthService.autenticarComSenha(authRequest))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Credenciais inválidas");

            verify(jwtUtil, never()).generateToken(anyString(), anyString());
        }
    }
}
