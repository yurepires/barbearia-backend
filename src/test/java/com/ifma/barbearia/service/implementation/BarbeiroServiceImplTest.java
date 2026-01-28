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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para BarbeiroServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
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
        barbeiro.setEmail("barbeiro@teste.com");
        barbeiro.setNome("Barbeiro Teste");
        barbeiro.setTelefone("11999999999");

        barbeiroDto = new BarbeiroDto();
        barbeiroDto.setEmail("barbeiro@teste.com");
        barbeiroDto.setNome("Barbeiro Teste");
        barbeiroDto.setTelefone("11999999999");
    }

    @Nested
    @DisplayName("Testes de Criar Barbeiro")
    class CriarBarbeiroTests {

        @Test
        @DisplayName("Deve criar barbeiro com sucesso")
        void deveCriarBarbeiroComSucesso() {
            // Arrange
            when(barbeiroRepository.findByEmail(barbeiroDto.getEmail())).thenReturn(Optional.empty());
            when(barbeiroMapper.toEntity(barbeiroDto)).thenReturn(barbeiro);

            // Act
            barbeiroService.criarBarbeiro(barbeiroDto);

            // Assert
            verify(barbeiroRepository).save(barbeiro);
        }

        @Test
        @DisplayName("Deve lançar exception quando email já existe")
        void deveLancarExceptionQuandoEmailJaExiste() {
            // Arrange
            when(barbeiroRepository.findByEmail(barbeiroDto.getEmail())).thenReturn(Optional.of(barbeiro));

            // Act & Assert
            assertThatThrownBy(() -> barbeiroService.criarBarbeiro(barbeiroDto))
                    .isInstanceOf(BarbeiroAlreadyExistsException.class);

            verify(barbeiroRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Buscar Barbeiro")
    class BuscarBarbeiroTests {

        @Test
        @DisplayName("Deve buscar barbeiro por email")
        void deveBuscarBarbeiroPorEmail() {
            // Arrange
            when(barbeiroRepository.findByEmail("barbeiro@teste.com")).thenReturn(Optional.of(barbeiro));
            when(barbeiroMapper.toDto(barbeiro)).thenReturn(barbeiroDto);

            // Act
            BarbeiroDto resultado = barbeiroService.buscarBarbeiro("barbeiro@teste.com");

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getEmail()).isEqualTo("barbeiro@teste.com");
        }

        @Test
        @DisplayName("Deve lançar exception quando barbeiro não encontrado")
        void deveLancarExceptionQuandoBarbeiroNaoEncontrado() {
            // Arrange
            when(barbeiroRepository.findByEmail("inexistente@teste.com")).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> barbeiroService.buscarBarbeiro("inexistente@teste.com"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Deve buscar todos os barbeiros")
        void deveBuscarTodosBarbeiros() {
            // Arrange
            when(barbeiroRepository.findAll()).thenReturn(List.of(barbeiro));
            when(barbeiroMapper.toDto(any(Barbeiro.class))).thenReturn(barbeiroDto);

            // Act
            List<BarbeiroDto> resultado = barbeiroService.buscarTodosBarbeiros();

            // Assert
            assertThat(resultado).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Testes de Atualizar Barbeiro")
    class AtualizarBarbeiroTests {

        @Test
        @DisplayName("Deve atualizar barbeiro com sucesso")
        void deveAtualizarBarbeiroComSucesso() {
            // Arrange
            when(barbeiroRepository.findByEmail(barbeiroDto.getEmail())).thenReturn(Optional.of(barbeiro));

            // Act
            boolean resultado = barbeiroService.atualizarBarbeiro(barbeiroDto);

            // Assert
            assertThat(resultado).isTrue();
            verify(barbeiroMapper).updateEntity(barbeiroDto, barbeiro);
            verify(barbeiroRepository).save(barbeiro);
        }
    }

    @Nested
    @DisplayName("Testes de Deletar Barbeiro")
    class DeletarBarbeiroTests {

        @Test
        @DisplayName("Deve deletar barbeiro com sucesso")
        void deveDeletarBarbeiroComSucesso() {
            // Arrange
            when(barbeiroRepository.findByEmail("barbeiro@teste.com")).thenReturn(Optional.of(barbeiro));

            // Act
            boolean resultado = barbeiroService.deletarBarbeiro("barbeiro@teste.com");

            // Assert
            assertThat(resultado).isTrue();
            verify(barbeiroRepository).delete(barbeiro);
        }
    }
}
