package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.PagamentoDto;
import com.ifma.barbearia.entity.Agendamento;
import com.ifma.barbearia.entity.Pagamento;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.PagamentoMapper;
import com.ifma.barbearia.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PagamentoServiceImpl - Testes Unitários")
class PagamentoServiceImplTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @InjectMocks
    private PagamentoServiceImpl pagamentoService;

    private Pagamento pagamento;
    private PagamentoDto pagamentoDto;
    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        agendamento = new Agendamento();
        agendamento.setAgendamentoId(1L);

        pagamento = new Pagamento();
        pagamento.setPagamentoId(1L);
        pagamento.setAgendamento(agendamento);
        pagamento.setValor(35.0);
        pagamento.setFormaPagamento("PIX");
        pagamento.setDataPagamento(LocalDateTime.of(2026, 12, 20, 10, 30));

        pagamentoDto = new PagamentoDto();
        pagamentoDto.setPagamentoId(1L);
        pagamentoDto.setAgendamentoId(1L);
        pagamentoDto.setValor(35.0);
        pagamentoDto.setFormaPagamento("PIX");
        pagamentoDto.setDataPagamento(LocalDateTime.of(2026, 12, 20, 10, 30));
    }

    @Nested
    @DisplayName("buscarPagamento()")
    class BuscarPagamento {

        @Test
        @DisplayName("Deve retornar DTO do pagamento quando encontrado por ID")
        void deveRetornarPagamentoQuandoEncontrado() {
            // given
            given(pagamentoRepository.findById(1L))
                    .willReturn(Optional.of(pagamento));
            given(pagamentoMapper.toDto(pagamento))
                    .willReturn(pagamentoDto);

            // when
            PagamentoDto resultado = pagamentoService.buscarPagamento(1L);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getPagamentoId()).isEqualTo(1L);
            assertThat(resultado.getValor()).isEqualTo(35.0);
            assertThat(resultado.getFormaPagamento()).isEqualTo("PIX");
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando pagamento não existe")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            // given
            given(pagamentoRepository.findById(99L))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> pagamentoService.buscarPagamento(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("buscarPagamentoPorAgendamento()")
    class BuscarPagamentoPorAgendamento {

        @Test
        @DisplayName("Deve retornar DTO do pagamento quando encontrado por agendamento ID")
        void deveRetornarPagamentoQuandoEncontradoPorAgendamento() {
            // given
            given(pagamentoRepository.findByAgendamento_AgendamentoId(1L))
                    .willReturn(Optional.of(pagamento));
            given(pagamentoMapper.toDto(pagamento))
                    .willReturn(pagamentoDto);

            // when
            PagamentoDto resultado = pagamentoService.buscarPagamentoPorAgendamento(1L);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getAgendamentoId()).isEqualTo(1L);
            assertThat(resultado.getValor()).isEqualTo(35.0);
        }

        @Test
        @DisplayName("Deve lançar exceção quando não há pagamento para o agendamento")
        void deveLancarExcecaoQuandoNaoHaPagamentoParaAgendamento() {
            // given
            given(pagamentoRepository.findByAgendamento_AgendamentoId(99L))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> pagamentoService.buscarPagamentoPorAgendamento(99L))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("salvarPagamento()")
    class SalvarPagamento {

        @Test
        @DisplayName("Deve salvar pagamento e retornar a entidade salva")
        void deveSalvarPagamentoComSucesso() {
            // given
            given(pagamentoRepository.save(pagamento))
                    .willReturn(pagamento);

            // when
            Pagamento resultado = pagamentoService.salvarPagamento(pagamento);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getPagamentoId()).isEqualTo(1L);
            assertThat(resultado.getValor()).isEqualTo(35.0);
            verify(pagamentoRepository, times(1)).save(pagamento);
        }
    }
}
