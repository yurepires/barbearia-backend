package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.BarbeiroDto;
import com.ifma.barbearia.entity.Barbeiro;
import com.ifma.barbearia.exceptions.BarbeiroAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.BarbeiroMapper;
import com.ifma.barbearia.repository.BarbeiroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BarbeiroServiceImpl - Testes Unitários")
class BarbeiroServiceImplTest {

    @Mock
    private BarbeiroRepository barbeiroRepository;

    @Mock
    private BarbeiroMapper barbeiroMapper;

    @InjectMocks
    private BarbeiroServiceImpl barbeiroService;

    private Barbeiro barbeiro;
    private BarbeiroDto barbeiroDto;

    @BeforeEach
    void setUp() {
        barbeiro = new Barbeiro();
        barbeiro.setBarbeiroId(1L);
        barbeiro.setNome("Carlos do Corte");
        barbeiro.setEmail("carlos@email.com");
        barbeiro.setTelefone("(86) 9 8888-8888");
        barbeiro.setEspecialidade("Corte e Barba");

        barbeiroDto = new BarbeiroDto();
        barbeiroDto.setNome("Carlos do Corte");
        barbeiroDto.setEmail("carlos@email.com");
        barbeiroDto.setTelefone("(86) 9 8888-8888");
        barbeiroDto.setEspecialidade("Corte e Barba");
    }

    @Nested
    @DisplayName("criarBarbeiro()")
    class CriarBarbeiro {

        @Test
        @DisplayName("Deve criar barbeiro com sucesso quando email não existe")
        void deveCriarBarbeiroComSucesso() {
            // given (arrange)
            given(barbeiroRepository.findByEmail(barbeiroDto.getEmail()))
                    .willReturn(Optional.empty());
            given(barbeiroMapper.toEntity(barbeiroDto))
                    .willReturn(barbeiro);

            // when (act)
            barbeiroService.criarBarbeiro(barbeiroDto);

            // then (assert)
            verify(barbeiroRepository, times(1)).save(barbeiro);
            verify(barbeiroMapper, times(1)).toEntity(barbeiroDto);
        }

        @Test
        @DisplayName("Deve lançar exceção quando email já está cadastrado")
        void deveLancarExcecaoQuandoEmailJaExiste() {
            // given
            given(barbeiroRepository.findByEmail(barbeiroDto.getEmail()))
                    .willReturn(Optional.of(barbeiro));

            // when & then
            assertThatThrownBy(() -> barbeiroService.criarBarbeiro(barbeiroDto))
                    .isInstanceOf(BarbeiroAlreadyExistsException.class)
                    .hasMessageContaining(barbeiroDto.getEmail());

            verify(barbeiroRepository, never()).save(any(Barbeiro.class));
        }
    }

    @Nested
    @DisplayName("buscarBarbeiro()")
    class BuscarBarbeiro {

        @Test
        @DisplayName("Deve retornar DTO do barbeiro quando encontrado por email")
        void deveRetornarBarbeiroQuandoEncontrado() {
            // given
            given(barbeiroRepository.findByEmail("carlos@email.com"))
                    .willReturn(Optional.of(barbeiro));
            given(barbeiroMapper.toDto(barbeiro))
                    .willReturn(barbeiroDto);

            // when
            BarbeiroDto resultado = barbeiroService.buscarBarbeiro("carlos@email.com");

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getNome()).isEqualTo("Carlos do Corte");
            assertThat(resultado.getEmail()).isEqualTo("carlos@email.com");
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando barbeiro não existe")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            // given
            given(barbeiroRepository.findByEmail("inexistente@email.com"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> barbeiroService.buscarBarbeiro("inexistente@email.com"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("inexistente@email.com");
        }
    }

    @Nested
    @DisplayName("buscarTodosBarbeiros()")
    class BuscarTodosBarbeiros {

        @Test
        @DisplayName("Deve retornar lista de barbeiros")
        void deveRetornarListaDeBarbeiros() {
            // given
            given(barbeiroRepository.findAll())
                    .willReturn(List.of(barbeiro));
            given(barbeiroMapper.toDto(barbeiro))
                    .willReturn(barbeiroDto);

            // when
            List<BarbeiroDto> resultado = barbeiroService.buscarTodosBarbeiros();

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNome()).isEqualTo("Carlos do Corte");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há barbeiros")
        void deveRetornarListaVaziaQuandoNaoHaBarbeiros() {
            // given
            given(barbeiroRepository.findAll())
                    .willReturn(Collections.emptyList());

            // when
            List<BarbeiroDto> resultado = barbeiroService.buscarTodosBarbeiros();

            // then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("atualizarBarbeiro()")
    class AtualizarBarbeiro {

        @Test
        @DisplayName("Deve atualizar barbeiro existente com sucesso")
        void deveAtualizarBarbeiroComSucesso() {
            // given
            given(barbeiroRepository.findByEmail(barbeiroDto.getEmail()))
                    .willReturn(Optional.of(barbeiro));

            // when
            boolean resultado = barbeiroService.atualizarBarbeiro(barbeiroDto);

            // then
            assertThat(resultado).isTrue();
            verify(barbeiroMapper, times(1)).updateEntity(barbeiroDto, barbeiro);
            verify(barbeiroRepository, times(1)).save(barbeiro);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar barbeiro inexistente")
        void deveLancarExcecaoAoAtualizarInexistente() {
            // given
            given(barbeiroRepository.findByEmail(barbeiroDto.getEmail()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> barbeiroService.atualizarBarbeiro(barbeiroDto))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(barbeiroRepository, never()).save(any(Barbeiro.class));
        }
    }

    @Nested
    @DisplayName("deletarBarbeiro()")
    class DeletarBarbeiro {

        @Test
        @DisplayName("Deve deletar barbeiro existente com sucesso")
        void deveDeletarBarbeiroComSucesso() {
            // given
            given(barbeiroRepository.findByEmail("carlos@email.com"))
                    .willReturn(Optional.of(barbeiro));

            // when
            boolean resultado = barbeiroService.deletarBarbeiro("carlos@email.com");

            // then
            assertThat(resultado).isTrue();
            verify(barbeiroRepository, times(1)).delete(barbeiro);
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar barbeiro inexistente")
        void deveLancarExcecaoAoDeletarInexistente() {
            // given
            given(barbeiroRepository.findByEmail("inexistente@email.com"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> barbeiroService.deletarBarbeiro("inexistente@email.com"))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(barbeiroRepository, never()).delete(any(Barbeiro.class));
        }
    }
}
