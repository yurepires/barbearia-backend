package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.entity.AdmUser;
import com.ifma.barbearia.repository.AdmUserRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("AdmUserImpl - Testes Unitários")
class AdmUserImplTest {

    @Mock
    private AdmUserRepository repo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdmUserImpl admUserService;

    private AdmUser admUser;

    @BeforeEach
    void setUp() {
        admUser = new AdmUser();
        admUser.setId(1L);
        admUser.setUsername("admin");
        admUser.setPassword("$2a$10$hashSenhaAdmin");
        admUser.setRole("ADM");
    }

    @Nested
    @DisplayName("findByUsername()")
    class FindByUsername {

        @Test
        @DisplayName("Deve retornar AdmUser quando username existe")
        void deveRetornarAdmUserQuandoUsernameExiste() {
            // given
            given(repo.findByUsername("admin"))
                    .willReturn(Optional.of(admUser));

            // when
            AdmUser resultado = admUserService.findByUsername("admin");

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getUsername()).isEqualTo("admin");
            assertThat(resultado.getRole()).isEqualTo("ADM");
            verify(repo, times(1)).findByUsername("admin");
        }

        @Test
        @DisplayName("Deve retornar null quando username não existe")
        void deveRetornarNullQuandoUsernameNaoExiste() {
            // given
            given(repo.findByUsername("inexistente"))
                    .willReturn(Optional.empty());

            // when
            AdmUser resultado = admUserService.findByUsername("inexistente");

            // then
            assertThat(resultado).isNull();
            verify(repo, times(1)).findByUsername("inexistente");
        }
    }

    @Nested
    @DisplayName("passwordMatches()")
    class PasswordMatches {

        @Test
        @DisplayName("Deve retornar true quando senha bate com o hash")
        void deveRetornarTrueQuandoSenhaCorreta() {
            // given
            given(passwordEncoder.matches("senhaCorreta", "$2a$10$hashSenhaAdmin"))
                    .willReturn(true);

            // when
            boolean resultado = admUserService.passwordMatches("senhaCorreta", "$2a$10$hashSenhaAdmin");

            // then
            assertThat(resultado).isTrue();
            verify(passwordEncoder, times(1)).matches("senhaCorreta", "$2a$10$hashSenhaAdmin");
        }

        @Test
        @DisplayName("Deve retornar false quando senha não bate com o hash")
        void deveRetornarFalseQuandoSenhaErrada() {
            // given
            given(passwordEncoder.matches("senhaErrada", "$2a$10$hashSenhaAdmin"))
                    .willReturn(false);

            // when
            boolean resultado = admUserService.passwordMatches("senhaErrada", "$2a$10$hashSenhaAdmin");

            // then
            assertThat(resultado).isFalse();
            verify(passwordEncoder, times(1)).matches("senhaErrada", "$2a$10$hashSenhaAdmin");
        }
    }
}
