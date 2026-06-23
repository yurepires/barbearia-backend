package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.DespesaDto;
import com.ifma.barbearia.entity.Despesa;
import com.ifma.barbearia.mapper.DespesaMapper;
import com.ifma.barbearia.repository.DespesaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DespesaServiceImpl - Testes Unitários")
class DespesaServiceImplTest {

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private DespesaMapper despesaMapper;

    @InjectMocks
    private DespesaServiceImpl despesaService;

    private Despesa despesa;
    private DespesaDto despesaDto;

    @BeforeEach
    void setUp() {
        despesa = new Despesa();
        despesa.setDespesaId(1L);
        despesa.setDescricao("Compra de produtos de higiene");
        despesa.setValor(150.50);
        despesa.setDataDespesa(LocalDate.of(2026, 6, 1));

        despesaDto = new DespesaDto();
        despesaDto.setDespesaId(1L);
        despesaDto.setDescricao("Compra de produtos de higiene");
        despesaDto.setValor(150.50);
        despesaDto.setDataDespesa(LocalDate.of(2026, 6, 1));
    }

    @Nested
    @DisplayName("criarDespesa()")
    class CriarDespesa {

        @Test
        @DisplayName("Deve criar despesa com sucesso")
        void deveCriarDespesaComSucesso() {
            // given
            given(despesaMapper.toEntity(despesaDto)).willReturn(despesa);

            // when
            despesaService.criarDespesa(despesaDto);

            // then
            verify(despesaMapper, times(1)).toEntity(despesaDto);
            verify(despesaRepository, times(1)).save(despesa);
        }

        @Test
        @DisplayName("Deve chamar mapper antes de salvar no repositório")
        void deveChamarMapperAntesDoRepositorio() {
            // given
            given(despesaMapper.toEntity(any(DespesaDto.class))).willReturn(despesa);

            // when
            despesaService.criarDespesa(despesaDto);

            // then
            var inOrder = inOrder(despesaMapper, despesaRepository);
            inOrder.verify(despesaMapper).toEntity(despesaDto);
            inOrder.verify(despesaRepository).save(despesa);
        }
    }

    @Nested
    @DisplayName("buscarTodasDespesas()")
    class BuscarTodasDespesas {

        @Test
        @DisplayName("Deve retornar lista de despesas mapeadas para DTO")
        void deveRetornarListaDeDespesas() {
            // given
            given(despesaRepository.findAll()).willReturn(List.of(despesa));
            given(despesaMapper.toDto(despesa)).willReturn(despesaDto);

            // when
            List<DespesaDto> resultado = despesaService.buscarTodasDespesas();

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getDescricao()).isEqualTo("Compra de produtos de higiene");
            assertThat(resultado.get(0).getValor()).isEqualTo(150.50);
            verify(despesaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há despesas")
        void deveRetornarListaVaziaQuandoNaoHaDespesas() {
            // given
            given(despesaRepository.findAll()).willReturn(Collections.emptyList());

            // when
            List<DespesaDto> resultado = despesaService.buscarTodasDespesas();

            // then
            assertThat(resultado).isEmpty();
            verify(despesaMapper, never()).toDto(any(Despesa.class));
        }
    }
}
