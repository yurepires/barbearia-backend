package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.entity.Pagamento;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.PagamentoMapper;
import com.ifma.barbearia.repository.PagamentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceImplTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @InjectMocks
    private PagamentoServiceImpl pagamentoService;

    @Test
    @DisplayName("Deve buscar pagamento pelo ID")
    void deveBuscarPagamentoPeloId() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setPagamentoId(1L);
        pagamento.setValor(100.0);

        PagamentoDto dto = new PagamentoDto();
        dto.setValor(100.0);

        when(pagamentoRepository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(pagamentoMapper.toDto(pagamento)).thenReturn(dto);

        // Act
        PagamentoDto resultado = pagamentoService.buscarPagamento(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getValor()).isEqualTo(100.0);
        verify(pagamentoRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exception quando pagamento não encontrado")
    void deveLancarExceptionQuandoPagamentoNaoEncontrado() {
        // Arrange
        when(pagamentoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> pagamentoService.buscarPagamento(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pagamento");
    }

    @Test
    @DisplayName("Deve buscar pagamento por agendamento")
    void deveBuscarPagamentoPorAgendamento() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setPagamentoId(1L);

        PagamentoDto dto = new PagamentoDto();

        when(pagamentoRepository.findByAgendamento_AgendamentoId(10L))
                .thenReturn(Optional.of(pagamento));
        when(pagamentoMapper.toDto(pagamento)).thenReturn(dto);

        // Act
        PagamentoDto resultado = pagamentoService.buscarPagamentoPorAgendamento(10L);

        // Assert
        assertThat(resultado).isNotNull();
        verify(pagamentoRepository).findByAgendamento_AgendamentoId(10L);
    }

    @Test
    @DisplayName("Deve lançar exception quando pagamento por agendamento não encontrado")
    void deveLancarExceptionQuandoPagamentoPorAgendamentoNaoEncontrado() {
        // Arrange
        when(pagamentoRepository.findByAgendamento_AgendamentoId(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> pagamentoService.buscarPagamentoPorAgendamento(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Pagamento");
    }

    @Test
    @DisplayName("Deve salvar pagamento com sucesso")
    void deveSalvarPagamentoComSucesso() {
        // Arrange
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(150.0);

        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        // Act
        Pagamento resultado = pagamentoService.salvarPagamento(pagamento);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getValor()).isEqualTo(150.0);
        verify(pagamentoRepository).save(pagamento);
    }
}
