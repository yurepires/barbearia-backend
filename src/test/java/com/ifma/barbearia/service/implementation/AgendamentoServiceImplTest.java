package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.entity.*;
import com.ifma.barbearia.entity.enums.StatusAgendamento;
import com.ifma.barbearia.exceptions.*;
import com.ifma.barbearia.mapper.AgendamentoMapper;
import com.ifma.barbearia.repository.AgendamentoRepository;
import com.ifma.barbearia.service.IBarbeiroService;
import com.ifma.barbearia.service.IClienteService;
import com.ifma.barbearia.service.IHistoricoAtendimentoService;
import com.ifma.barbearia.service.IPagamentoService;
import com.ifma.barbearia.service.IServicoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AgendamentoServiceImpl - Testes Unitários")
class AgendamentoServiceImplTest {

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private IClienteService clienteService;

    @Mock
    private IServicoService servicoService;

    @Mock
    private IBarbeiroService barbeiroService;

    @Mock
    private IPagamentoService pagamentoService;

    @Mock
    private IHistoricoAtendimentoService historicoAtendimentoService;

    @Mock
    private AgendamentoMapper agendamentoMapper;

    @InjectMocks
    private AgendamentoServiceImpl agendamentoService;

    private Cliente cliente;
    private Servico servico;
    private Barbeiro barbeiro;
    private Agendamento agendamento;
    private AgendamentoDto agendamentoDto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setNome("João Maria");
        cliente.setEmail("joao@email.com");

        servico = new Servico();
        servico.setServicoId(1L);
        servico.setNome("Corte degradê");
        servico.setPreco(35.0);

        barbeiro = new Barbeiro();
        barbeiro.setBarbeiroId(1L);
        barbeiro.setNome("Carlos do Corte");
        barbeiro.setEmail("carlos@email.com");
        barbeiro.setEspecialidade("Corte e Barba");

        agendamento = new Agendamento();
        agendamento.setAgendamentoId(1L);
        agendamento.setHorario(LocalDateTime.of(2026, 12, 20, 10, 0));
        agendamento.setStatus(StatusAgendamento.PENDENTE);
        agendamento.setCliente(cliente);
        agendamento.setServico(servico);
        agendamento.setBarbeiro(barbeiro);

        agendamentoDto = new AgendamentoDto();
        agendamentoDto.setId(1L);
        agendamentoDto.setHorario(LocalDateTime.of(2026, 12, 20, 10, 0));
        agendamentoDto.setClienteEmail("joao@email.com");
        agendamentoDto.setServicoId(1L);
        agendamentoDto.setBarbeiroEmail("carlos@email.com");
    }

    @Nested
    @DisplayName("criarAgendamento()")
    class CriarAgendamento {

        @Test
        @DisplayName("Deve criar agendamento com sucesso")
        void deveCriarAgendamentoComSucesso() {
            // given
            given(clienteService.buscarEntidadeClientePorEmail("joao@email.com"))
                    .willReturn(cliente);
            given(servicoService.buscarEntidadeServicoPorId(1L))
                    .willReturn(servico);
            given(barbeiroService.buscarEntidadeBarbeiroPorEmail("carlos@email.com"))
                    .willReturn(barbeiro);
            given(agendamentoRepository.existsByBarbeiro_BarbeiroIdAndHorarioAndStatus(
                    eq(1L), any(LocalDateTime.class), eq(StatusAgendamento.PENDENTE)))
                    .willReturn(false);
            given(agendamentoMapper.toEntity(agendamentoDto, cliente, servico, barbeiro))
                    .willReturn(agendamento);

            // when
            agendamentoService.criarAgendamento(agendamentoDto);

            // then
            verify(agendamentoRepository, times(1)).save(agendamento);
            assertThat(agendamento.getStatus()).isEqualTo(StatusAgendamento.PENDENTE);
        }

        @Test
        @DisplayName("Deve lançar exceção quando barbeiro já tem horário ocupado")
        void deveLancarExcecaoQuandoHorarioOcupado() {
            // given
            given(clienteService.buscarEntidadeClientePorEmail("joao@email.com"))
                    .willReturn(cliente);
            given(servicoService.buscarEntidadeServicoPorId(1L))
                    .willReturn(servico);
            given(barbeiroService.buscarEntidadeBarbeiroPorEmail("carlos@email.com"))
                    .willReturn(barbeiro);
            given(agendamentoRepository.existsByBarbeiro_BarbeiroIdAndHorarioAndStatus(
                    eq(1L), any(LocalDateTime.class), eq(StatusAgendamento.PENDENTE)))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> agendamentoService.criarAgendamento(agendamentoDto))
                    .isInstanceOf(HorarioIndisponivelException.class)
                    .hasMessageContaining("barbeiro");

            verify(agendamentoRepository, never()).save(any(Agendamento.class));
        }

        static Stream<Arguments> horariosInvalidosProvider() {
            return Stream.of(
                Arguments.of(LocalDateTime.of(2026, 12, 20,  6,  0), "expediente",  "antes da abertura (06:00)"),
                Arguments.of(LocalDateTime.of(2026, 12, 20, 22,  0), "expediente",  "após o fechamento (22:00)"),
                Arguments.of(LocalDateTime.of(2026, 12, 20, 10, 15), "30 minutos", "intervalo inválido (10:15)")
            );
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource("horariosInvalidosProvider")
        @DisplayName("Deve lançar AgendamentoInvalidoException para horário inválido")
        void deveLancarExcecaoParaHorarioInvalido(
                LocalDateTime horario,
                String mensagemEsperada,
                String descricaoCenario
        ) {
            // given
            agendamentoDto.setHorario(horario);
            given(clienteService.buscarEntidadeClientePorEmail("joao@email.com"))
                    .willReturn(cliente);
            given(servicoService.buscarEntidadeServicoPorId(1L))
                    .willReturn(servico);
            given(barbeiroService.buscarEntidadeBarbeiroPorEmail("carlos@email.com"))
                    .willReturn(barbeiro);

            // when & then
            assertThatThrownBy(() -> agendamentoService.criarAgendamento(agendamentoDto))
                    .isInstanceOf(AgendamentoInvalidoException.class)
                    .hasMessageContaining(mensagemEsperada);
        }
    }

    @Nested
    @DisplayName("buscarAgendamento()")
    class BuscarAgendamento {

        @Test
        @DisplayName("Deve retornar DTO do agendamento quando encontrado")
        void deveRetornarAgendamentoQuandoEncontrado() {
            // given
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));
            given(agendamentoMapper.toDto(agendamento))
                    .willReturn(agendamentoDto);

            // when
            AgendamentoDto resultado = agendamentoService.buscarAgendamento(1L);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando agendamento não encontrado")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            // given
            given(agendamentoRepository.findById(99L))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> agendamentoService.buscarAgendamento(99L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("buscarTodosAgendamentos()")
    class BuscarTodosAgendamentos {

        @Test
        @DisplayName("Deve retornar lista de agendamentos")
        void deveRetornarListaDeAgendamentos() {
            // given
            given(agendamentoRepository.findAll())
                    .willReturn(List.of(agendamento));
            given(agendamentoMapper.toDto(agendamento))
                    .willReturn(agendamentoDto);

            // when
            List<AgendamentoDto> resultado = agendamentoService.buscarTodosAgendamentos();

            // then
            assertThat(resultado).hasSize(1);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há agendamentos")
        void deveRetornarListaVaziaQuandoNaoHaAgendamentos() {
            // given
            given(agendamentoRepository.findAll())
                    .willReturn(Collections.emptyList());

            // when
            List<AgendamentoDto> resultado = agendamentoService.buscarTodosAgendamentos();

            // then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("buscarAgendamentosPorCliente()")
    class BuscarAgendamentosPorCliente {

        @Test
        @DisplayName("Deve retornar agendamentos do cliente")
        void deveRetornarAgendamentosDoCliente() {
            // given
            given(clienteService.buscarEntidadeClientePorEmail("joao@email.com"))
                    .willReturn(cliente);
            given(agendamentoRepository.findByCliente_ClienteId(1L))
                    .willReturn(List.of(agendamento));
            given(agendamentoMapper.toDto(agendamento))
                    .willReturn(agendamentoDto);

            // when
            List<AgendamentoDto> resultado = agendamentoService.buscarAgendamentosPorCliente("joao@email.com");

            // then
            assertThat(resultado).hasSize(1);
        }
    }

    @Nested
    @DisplayName("buscarAgendamentosPorIntervaloDeDatas()")
    class BuscarAgendamentosPorIntervaloDeDatas {

        @Test
        @DisplayName("Deve retornar agendamentos no intervalo de datas")
        void deveRetornarAgendamentosNoIntervalo() {
            // given
            LocalDate inicio = LocalDate.of(2026, 12, 1);
            LocalDate fim = LocalDate.of(2026, 12, 31);

            given(agendamentoRepository.findByHorarioBetween(
                    inicio.atStartOfDay(), fim.atTime(23, 59, 59)))
                    .willReturn(List.of(agendamento));
            given(agendamentoMapper.toDto(agendamento))
                    .willReturn(agendamentoDto);

            // when
            List<AgendamentoDto> resultado = agendamentoService.buscarAgendamentosPorIntervaloDeDatas(inicio, fim);

            // then
            assertThat(resultado).hasSize(1);
        }
    }

    @Nested
    @DisplayName("cancelarAgendamento()")
    class CancelarAgendamento {

        @Test
        @DisplayName("Deve cancelar agendamento pendente com sucesso")
        void deveCancelarAgendamentoPendenteComSucesso() {
            // given
            agendamento.setStatus(StatusAgendamento.PENDENTE);
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));

            // when
            boolean resultado = agendamentoService.cancelarAgendamento(1L);

            // then
            assertThat(resultado).isTrue();
            assertThat(agendamento.getStatus()).isEqualTo(StatusAgendamento.CANCELADO);
            verify(agendamentoRepository, times(1)).save(agendamento);
        }

        @Test
        @DisplayName("Deve lançar exceção ao cancelar agendamento já concluído")
        void deveLancarExcecaoAoCancelarConcluido() {
            // given
            agendamento.setStatus(StatusAgendamento.CONCLUIDO);
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));

            // when & then
            assertThatThrownBy(() -> agendamentoService.cancelarAgendamento(1L))
                    .isInstanceOf(CancelamentoInvalidoException.class)
                    .hasMessageContaining("concluído");
        }

        @Test
        @DisplayName("Deve lançar exceção ao cancelar agendamento já cancelado")
        void deveLancarExcecaoAoCancelarJaCancelado() {
            // given
            agendamento.setStatus(StatusAgendamento.CANCELADO);
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));

            // when & then
            assertThatThrownBy(() -> agendamentoService.cancelarAgendamento(1L))
                    .isInstanceOf(CancelamentoInvalidoException.class)
                    .hasMessageContaining("cancelado");
        }
    }

    @Nested
    @DisplayName("concluirAgendamento()")
    class ConcluirAgendamento {

        @Test
        @DisplayName("Deve concluir agendamento pendente com sucesso")
        void deveConcluirAgendamentoPendenteComSucesso() {
            // given
            agendamento.setStatus(StatusAgendamento.PENDENTE);
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));

            // when
            agendamentoService.concluirAgendamento(1L, "PIX");

            // then
            assertThat(agendamento.getStatus()).isEqualTo(StatusAgendamento.CONCLUIDO);
            verify(agendamentoRepository, times(1)).save(agendamento);
            verify(pagamentoService, times(1)).salvarPagamento(any(Pagamento.class));
            verify(historicoAtendimentoService, times(1)).registrar(eq(agendamento), any(Pagamento.class));
        }

        @Test
        @DisplayName("Deve lançar exceção ao concluir agendamento cancelado")
        void deveLancarExcecaoAoConcluirCancelado() {
            // given
            agendamento.setStatus(StatusAgendamento.CANCELADO);
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));

            // when & then
            assertThatThrownBy(() -> agendamentoService.concluirAgendamento(1L, "PIX"))
                    .isInstanceOf(ConclusaoInvalidaException.class)
                    .hasMessageContaining("cancelado");
        }

        @Test
        @DisplayName("Deve lançar exceção ao concluir agendamento já concluído")
        void deveLancarExcecaoAoConcluirJaConcluido() {
            // given
            agendamento.setStatus(StatusAgendamento.CONCLUIDO);
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));

            // when & then
            assertThatThrownBy(() -> agendamentoService.concluirAgendamento(1L, "PIX"))
                    .isInstanceOf(ConclusaoInvalidaException.class)
                    .hasMessageContaining("concluído");
        }
    }

    @Nested
    @DisplayName("atualizarAgendamento()")
    class AtualizarAgendamento {

        @Test
        @DisplayName("Deve atualizar agendamento existente com sucesso")
        void deveAtualizarAgendamentoComSucesso() {
            // given
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.of(agendamento));
            given(clienteService.buscarEntidadeClientePorEmail("joao@email.com"))
                    .willReturn(cliente);
            given(servicoService.buscarEntidadeServicoPorId(1L))
                    .willReturn(servico);
            given(barbeiroService.buscarEntidadeBarbeiroPorEmail("carlos@email.com"))
                    .willReturn(barbeiro);

            // when
            boolean resultado = agendamentoService.atualizarAgendamento(agendamentoDto);

            // then
            assertThat(resultado).isTrue();
            verify(agendamentoMapper, times(1))
                    .updateEntity(agendamentoDto, agendamento, cliente, servico, barbeiro);
            verify(agendamentoRepository, times(1)).save(agendamento);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar agendamento inexistente")
        void deveLancarExcecaoAoAtualizarInexistente() {
            // given
            given(agendamentoRepository.findById(1L))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> agendamentoService.atualizarAgendamento(agendamentoDto))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
