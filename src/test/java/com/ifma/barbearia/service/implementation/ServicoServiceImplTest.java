package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ServicoDto;
import com.ifma.barbearia.entity.Servico;
import com.ifma.barbearia.exceptions.ServicoAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.ServicoMapper;
import com.ifma.barbearia.repository.ServicoRepository;
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
 * Testes unitários para ServicoServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class ServicoServiceImplTest {

    @Mock
    private ServicoRepository servicoRepository;
    @Mock
    private ServicoMapper servicoMapper;

    @InjectMocks
    private ServicoServiceImpl servicoService;

    private Servico servico;
    private ServicoDto servicoDto;

    @BeforeEach
    void setUp() {
        servico = new Servico();
        servico.setServicoId(1L);
        servico.setNome("Corte Simples");
        servico.setDescricao("Corte na tesoura");
        servico.setPreco(35.0);

        servicoDto = new ServicoDto();
        servicoDto.setServicoId(1L);
        servicoDto.setNome("Corte Simples");
        servicoDto.setDescricao("Corte na tesoura");
        servicoDto.setPreco(35.0);
    }

    @Nested
    @DisplayName("Testes de Criar Servico")
    class CriarServicoTests {

        @Test
        @DisplayName("Deve criar servico com sucesso")
        void deveCriarServicoComSucesso() {
            // Arrange
            when(servicoRepository.findByNome(servicoDto.getNome())).thenReturn(Optional.empty());
            when(servicoMapper.toEntity(servicoDto)).thenReturn(servico);

            // Act
            servicoService.criarServico(servicoDto);

            // Assert
            verify(servicoRepository).save(servico);
        }

        @Test
        @DisplayName("Deve lançar exception quando nome já existe")
        void deveLancarExceptionQuandoNomeJaExiste() {
            // Arrange
            when(servicoRepository.findByNome(servicoDto.getNome())).thenReturn(Optional.of(servico));

            // Act & Assert
            assertThatThrownBy(() -> servicoService.criarServico(servicoDto))
                    .isInstanceOf(ServicoAlreadyExistsException.class);

            verify(servicoRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Buscar Servico")
    class BuscarServicoTests {

        @Test
        @DisplayName("Deve buscar servico por id")
        void deveBuscarServicoPorId() {
            // Arrange
            when(servicoRepository.findByServicoId(1L)).thenReturn(Optional.of(servico));
            when(servicoMapper.toDto(servico)).thenReturn(servicoDto);

            // Act
            ServicoDto resultado = servicoService.buscarServico(1L);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getNome()).isEqualTo("Corte Simples");
        }

        @Test
        @DisplayName("Deve lançar exception quando servico não encontrado")
        void deveLancarExceptionQuandoServicoNaoEncontrado() {
            // Arrange
            when(servicoRepository.findByServicoId(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> servicoService.buscarServico(999L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Deve buscar todos os servicos")
        void deveBuscarTodosServicos() {
            // Arrange
            when(servicoRepository.findAll()).thenReturn(List.of(servico));
            when(servicoMapper.toDto(any(Servico.class))).thenReturn(servicoDto);

            // Act
            List<ServicoDto> resultado = servicoService.buscarTodosServicos();

            // Assert
            assertThat(resultado).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Testes de Atualizar Servico")
    class AtualizarServicoTests {

        @Test
        @DisplayName("Deve atualizar servico com sucesso")
        void deveAtualizarServicoComSucesso() {
            // Arrange
            when(servicoRepository.findByServicoId(servicoDto.getServicoId())).thenReturn(Optional.of(servico));

            // Act
            boolean resultado = servicoService.atualizarServico(servicoDto);

            // Assert
            assertThat(resultado).isTrue();
            verify(servicoMapper).updateEntity(servicoDto, servico);
            verify(servicoRepository).save(servico);
        }
    }

    @Nested
    @DisplayName("Testes de Deletar Servico")
    class DeletarServicoTests {

        @Test
        @DisplayName("Deve deletar servico com sucesso")
        void deveDeletarServicoComSucesso() {
            // Arrange
            when(servicoRepository.findByServicoId(1L)).thenReturn(Optional.of(servico));

            // Act
            boolean resultado = servicoService.deletarServico(1L);

            // Assert
            assertThat(resultado).isTrue();
            verify(servicoRepository).delete(servico);
        }
    }
}
