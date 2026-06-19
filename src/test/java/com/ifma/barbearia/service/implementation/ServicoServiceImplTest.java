package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ServicoDto;
import com.ifma.barbearia.entity.Servico;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.exceptions.ServicoAlreadyExistsException;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ServicoServiceImpl - Testes Unitários")
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
        servico.setNome("Corte degradê");
        servico.setPreco(35.0);
        servico.setDescricao("Corte degradê na régua máxima.");

        servicoDto = new ServicoDto();
        servicoDto.setServicoId(1L);
        servicoDto.setNome("Corte degradê");
        servicoDto.setPreco(35.0);
        servicoDto.setDescricao("Corte degradê na régua máxima.");
    }

    @Nested
    @DisplayName("criarServico()")
    class CriarServico {

        @Test
        @DisplayName("Deve criar serviço com sucesso quando nome não existe")
        void deveCriarServicoComSucesso() {
            // given
            given(servicoRepository.findByNome(servicoDto.getNome()))
                    .willReturn(Optional.empty());
            given(servicoMapper.toEntity(servicoDto))
                    .willReturn(servico);

            // when
            servicoService.criarServico(servicoDto);

            // then
            verify(servicoRepository, times(1)).save(servico);
            verify(servicoMapper, times(1)).toEntity(servicoDto);
        }

        @Test
        @DisplayName("Deve lançar exceção quando nome do serviço já está cadastrado")
        void deveLancarExcecaoQuandoNomeJaExiste() {
            // given
            given(servicoRepository.findByNome(servicoDto.getNome()))
                    .willReturn(Optional.of(servico));

            // when & then
            assertThatThrownBy(() -> servicoService.criarServico(servicoDto))
                    .isInstanceOf(ServicoAlreadyExistsException.class)
                    .hasMessageContaining(servicoDto.getNome());

            verify(servicoRepository, never()).save(any(Servico.class));
        }
    }

    @Nested
    @DisplayName("buscarServico()")
    class BuscarServico {

        @Test
        @DisplayName("Deve retornar DTO do serviço quando encontrado por ID")
        void deveRetornarServicoQuandoEncontrado() {
            // given
            given(servicoRepository.findByServicoId(1L))
                    .willReturn(Optional.of(servico));
            given(servicoMapper.toDto(servico))
                    .willReturn(servicoDto);

            // when
            ServicoDto resultado = servicoService.buscarServico(1L);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getNome()).isEqualTo("Corte degradê");
            assertThat(resultado.getPreco()).isEqualTo(35.0);
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando serviço não existe")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            // given
            given(servicoRepository.findByServicoId(99L))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> servicoService.buscarServico(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("buscarEntidadeServicoPorId()")
    class BuscarEntidadeServicoPorId {

        @Test
        @DisplayName("Deve retornar entidade Servico quando encontrada")
        void deveRetornarEntidadeQuandoEncontrada() {
            // given
            given(servicoRepository.findByServicoId(1L))
                    .willReturn(Optional.of(servico));

            // when
            Servico resultado = servicoService.buscarEntidadeServicoPorId(1L);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getServicoId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando entidade não encontrada")
        void deveLancarExcecaoQuandoEntidadeNaoEncontrada() {
            // given
            given(servicoRepository.findByServicoId(99L))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> servicoService.buscarEntidadeServicoPorId(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("buscarTodosServicos()")
    class BuscarTodosServicos {

        @Test
        @DisplayName("Deve retornar lista de serviços")
        void deveRetornarListaDeServicos() {
            // given
            given(servicoRepository.findAll())
                    .willReturn(List.of(servico));
            given(servicoMapper.toDto(servico))
                    .willReturn(servicoDto);

            // when
            List<ServicoDto> resultado = servicoService.buscarTodosServicos();

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNome()).isEqualTo("Corte degradê");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há serviços")
        void deveRetornarListaVaziaQuandoNaoHaServicos() {
            // given
            given(servicoRepository.findAll())
                    .willReturn(Collections.emptyList());

            // when
            List<ServicoDto> resultado = servicoService.buscarTodosServicos();

            // then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("atualizarServico()")
    class AtualizarServico {

        @Test
        @DisplayName("Deve atualizar serviço existente com sucesso")
        void deveAtualizarServicoComSucesso() {
            // given
            given(servicoRepository.findByServicoId(servicoDto.getServicoId()))
                    .willReturn(Optional.of(servico));

            // when
            boolean resultado = servicoService.atualizarServico(servicoDto);

            // then
            assertThat(resultado).isTrue();
            verify(servicoMapper, times(1)).updateEntity(servicoDto, servico);
            verify(servicoRepository, times(1)).save(servico);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar serviço inexistente")
        void deveLancarExcecaoAoAtualizarInexistente() {
            // given
            given(servicoRepository.findByServicoId(servicoDto.getServicoId()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> servicoService.atualizarServico(servicoDto))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(servicoRepository, never()).save(any(Servico.class));
        }
    }

    @Nested
    @DisplayName("deletarServico()")
    class DeletarServico {

        @Test
        @DisplayName("Deve deletar serviço existente com sucesso")
        void deveDeletarServicoComSucesso() {
            // given
            given(servicoRepository.findByServicoId(1L))
                    .willReturn(Optional.of(servico));

            // when
            boolean resultado = servicoService.deletarServico(1L);

            // then
            assertThat(resultado).isTrue();
            verify(servicoRepository, times(1)).delete(servico);
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar serviço inexistente")
        void deveLancarExcecaoAoDeletarInexistente() {
            // given
            given(servicoRepository.findByServicoId(99L))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> servicoService.deletarServico(99L))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(servicoRepository, never()).delete(any(Servico.class));
        }
    }
}
