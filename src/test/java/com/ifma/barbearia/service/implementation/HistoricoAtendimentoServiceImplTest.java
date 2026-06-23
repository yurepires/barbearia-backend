package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.entity.*;
import com.ifma.barbearia.entity.enums.StatusAgendamento;
import com.ifma.barbearia.repository.HistoricoAtendimentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("HistoricoAtendimentoServiceImpl - Testes Unitários")
class HistoricoAtendimentoServiceImplTest {

    @Mock
    private HistoricoAtendimentoRepository historicoAtendimentoRepository;

    @InjectMocks
    private HistoricoAtendimentoServiceImpl historicoAtendimentoService;

    private Cliente cliente;
    private Barbeiro barbeiro;
    private Servico servico;
    private Agendamento agendamento;
    private Pagamento pagamento;
    private HistoricoAtendimento historico;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setNome("João Maria");
        cliente.setEmail("joao@email.com");

        barbeiro = new Barbeiro();
        barbeiro.setBarbeiroId(1L);
        barbeiro.setNome("Carlos do Corte");
        barbeiro.setEmail("carlos@email.com");

        servico = new Servico();
        servico.setServicoId(1L);
        servico.setNome("Corte degradê");
        servico.setPreco(35.0);

        agendamento = new Agendamento();
        agendamento.setAgendamentoId(1L);
        agendamento.setHorario(LocalDateTime.of(2026, 12, 20, 10, 0));
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);

        pagamento = new Pagamento();
        pagamento.setPagamentoId(1L);
        pagamento.setAgendamento(agendamento);
        pagamento.setValor(35.0);
        pagamento.setFormaPagamento("PIX");
        pagamento.setDataPagamento(LocalDateTime.of(2026, 12, 20, 10, 30));

        historico = new HistoricoAtendimento();
        historico.setHistoricoAtendimentoId(1L);
        historico.setData(LocalDateTime.of(2026, 12, 20, 10, 30));
        historico.setPagamento(pagamento);
        historico.setCliente(cliente);
        historico.setBarbeiro(barbeiro);
        historico.setServico(servico);
    }

    @Nested
    @DisplayName("registrar()")
    class Registrar {

        @Test
        @DisplayName("Deve registrar histórico de atendimento com dados corretos do agendamento")
        void deveRegistrarHistoricoComDadosCorretos() {
            // given
            ArgumentCaptor<HistoricoAtendimento> captor = ArgumentCaptor.forClass(HistoricoAtendimento.class);

            // when
            historicoAtendimentoService.registrar(agendamento, pagamento);

            // then
            verify(historicoAtendimentoRepository, times(1)).save(captor.capture());
            HistoricoAtendimento salvo = captor.getValue();

            assertThat(salvo.getPagamento()).isEqualTo(pagamento);
            assertThat(salvo.getCliente()).isEqualTo(cliente);
            assertThat(salvo.getBarbeiro()).isEqualTo(barbeiro);
            assertThat(salvo.getServico()).isEqualTo(servico);
            assertThat(salvo.getData()).isNotNull();
        }
    }

    @Nested
    @DisplayName("listarTodos()")
    class ListarTodos {

        @Test
        @DisplayName("Deve retornar lista com todos os históricos")
        void deveRetornarTodosHistoricos() {
            // given
            given(historicoAtendimentoRepository.findAll())
                    .willReturn(List.of(historico));

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarTodos();

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getCliente().getNome()).isEqualTo("João Maria");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há históricos")
        void deveRetornarListaVazia() {
            // given
            given(historicoAtendimentoRepository.findAll())
                    .willReturn(Collections.emptyList());

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarTodos();

            // then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("listarPorCliente()")
    class ListarPorCliente {

        @Test
        @DisplayName("Deve retornar históricos do cliente pelo email")
        void deveRetornarHistoricosDoCliente() {
            // given
            given(historicoAtendimentoRepository.findByCliente_Email("joao@email.com"))
                    .willReturn(List.of(historico));

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorCliente("joao@email.com");

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getCliente().getEmail()).isEqualTo("joao@email.com");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando cliente não tem histórico")
        void deveRetornarListaVaziaParaClienteSemHistorico() {
            // given
            given(historicoAtendimentoRepository.findByCliente_Email("semhistorico@email.com"))
                    .willReturn(Collections.emptyList());

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorCliente("semhistorico@email.com");

            // then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("listarPorBarbeiro()")
    class ListarPorBarbeiro {

        @Test
        @DisplayName("Deve retornar históricos do barbeiro pelo email")
        void deveRetornarHistoricosDoBarbeiro() {
            // given
            given(historicoAtendimentoRepository.findByBarbeiro_Email("carlos@email.com"))
                    .willReturn(List.of(historico));

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorBarbeiro("carlos@email.com");

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getBarbeiro().getEmail()).isEqualTo("carlos@email.com");
        }
    }

    @Nested
    @DisplayName("listarPorServico()")
    class ListarPorServico {

        @Test
        @DisplayName("Deve retornar históricos pelo ID do serviço")
        void deveRetornarHistoricosPorServico() {
            // given
            given(historicoAtendimentoRepository.findByServico_ServicoId(1L))
                    .willReturn(List.of(historico));

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorServico(1L);

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getServico().getServicoId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("listarPorIntervaloDeDatas()")
    class ListarPorIntervaloDeDatas {

        @Test
        @DisplayName("Deve retornar históricos no intervalo de datas")
        void deveRetornarHistoricosNoIntervalo() {
            // given
            LocalDate inicio = LocalDate.of(2026, 12, 1);
            LocalDate fim = LocalDate.of(2026, 12, 31);

            given(historicoAtendimentoRepository.findByDataBetween(
                    inicio.atStartOfDay(), fim.atTime(23, 59, 59)))
                    .willReturn(List.of(historico));

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorIntervaloDeDatas(inicio, fim);

            // then
            assertThat(resultado).hasSize(1);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há históricos no intervalo")
        void deveRetornarListaVaziaQuandoNaoHaHistoricosNoIntervalo() {
            // given
            LocalDate inicio = LocalDate.of(2025, 1, 1);
            LocalDate fim = LocalDate.of(2025, 1, 31);

            given(historicoAtendimentoRepository.findByDataBetween(
                    inicio.atStartOfDay(), fim.atTime(23, 59, 59)))
                    .willReturn(Collections.emptyList());

            // when
            List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorIntervaloDeDatas(inicio, fim);

            // then
            assertThat(resultado).isEmpty();
        }
    }
}
