package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.DespesaDto;
import com.ifma.barbearia.entity.Despesa;
import com.ifma.barbearia.mapper.DespesaMapper;
import com.ifma.barbearia.repository.DespesaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DespesaServiceImplTest {

    @Mock
    private DespesaRepository despesaRepository;

    @Mock
    private DespesaMapper despesaMapper;

    @InjectMocks
    private DespesaServiceImpl despesaService;

    @Test
    @DisplayName("Deve criar despesa com sucesso")
    void deveCriarDespesaComSucesso() {
        // Arrange
        DespesaDto dto = new DespesaDto();
        dto.setDescricao("Aluguel");
        dto.setValor(1500.0);
        dto.setDataDespesa(LocalDate.now());

        Despesa despesa = new Despesa();
        despesa.setDescricao("Aluguel");

        when(despesaMapper.toEntity(dto)).thenReturn(despesa);
        when(despesaRepository.save(any(Despesa.class))).thenReturn(despesa);

        // Act
        despesaService.criarDespesa(dto);

        // Assert
        verify(despesaMapper).toEntity(dto);
        verify(despesaRepository).save(despesa);
    }

    @Test
    @DisplayName("Deve buscar todas as despesas")
    void deveBuscarTodasDespesas() {
        // Arrange
        Despesa despesa1 = new Despesa();
        despesa1.setDescricao("Aluguel");
        Despesa despesa2 = new Despesa();
        despesa2.setDescricao("Luz");

        DespesaDto dto1 = new DespesaDto();
        dto1.setDescricao("Aluguel");
        DespesaDto dto2 = new DespesaDto();
        dto2.setDescricao("Luz");

        when(despesaRepository.findAll()).thenReturn(List.of(despesa1, despesa2));
        when(despesaMapper.toDto(despesa1)).thenReturn(dto1);
        when(despesaMapper.toDto(despesa2)).thenReturn(dto2);

        // Act
        List<DespesaDto> resultado = despesaService.buscarTodasDespesas();

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getDescricao()).isEqualTo("Aluguel");
        assertThat(resultado.get(1).getDescricao()).isEqualTo("Luz");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há despesas")
    void deveRetornarListaVaziaQuandoSemDespesas() {
        // Arrange
        when(despesaRepository.findAll()).thenReturn(List.of());

        // Act
        List<DespesaDto> resultado = despesaService.buscarTodasDespesas();

        // Assert
        assertThat(resultado).isEmpty();
    }
}
