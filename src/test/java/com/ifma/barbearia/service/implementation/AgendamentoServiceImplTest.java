package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.entity.Agendamento;
import com.ifma.barbearia.entity.Barbeiro;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.entity.Pagamento;
import com.ifma.barbearia.entity.Servico;
import com.ifma.barbearia.entity.enums.StatusAgendamento;
import com.ifma.barbearia.exceptions.*;
import com.ifma.barbearia.mapper.AgendamentoMapper;
import com.ifma.barbearia.repository.AgendamentoRepository;
import com.ifma.barbearia.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AgendamentoServiceImpl.
 * Foco nas regras de negócio críticas.
 */
@ExtendWith(MockitoExtension.class)
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
    private Barbeiro barbeiro;
    private Servico servico;
    private AgendamentoDto agendamentoDto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setEmail("cliente@teste.com");
        cliente.setNome("Cliente Teste");

        barbeiro = new Barbeiro();
        barbeiro.setBarbeiroId(1L);
        barbeiro.setEmail("barbeiro@teste.com");
        barbeiro.setNome("Barbeiro Teste");

        servico = new Servico();
        servico.setServicoId(1L);
        servico.setNome("Corte");
        servico.setPreco(35.0);

        agendamentoDto = new AgendamentoDto();
        agendamentoDto.setClienteEmail("cliente@teste.com");
        agendamentoDto.setBarbeiroEmail("barbeiro@teste.com");
        agendamentoDto.setServicoId(1L);
        agendamentoDto.setHorario(LocalDateTime.of(2026, 2, 1, 10, 0)); // 10:00, múltiplo de 30
    }

    @Nested
    @DisplayName("Testes de Criar Agendamento")
    class CriarAgendamentoTests {

        @Test
        @DisplayName("Deve criar agendamento com sucesso quando dados válidos")
        void deveCriarAgendamentoComSucesso() {
            // Arrange
            when(clienteService.buscarEntidadeClientePorEmail(agendamentoDto.getClienteEmail())).thenReturn(cliente);
            when(servicoService.buscarEntidadeServicoPorId(agendamentoDto.getServicoId())).thenReturn(servico);
            when(barbeiroService.buscarEntidadeBarbeiroPorEmail(agendamentoDto.getBarbeiroEmail()))
                    .thenReturn(barbeiro);
            when(agendamentoRepository.existsByBarbeiro_BarbeiroIdAndHorarioAndStatus(
                    eq(barbeiro.getBarbeiroId()), eq(agendamentoDto.getHorario()), eq(StatusAgendamento.PENDENTE)))
                    .thenReturn(false);
            when(agendamentoMapper.toEntity(any(), any(), any(), any())).thenReturn(new Agendamento());

            // Act
            agendamentoService.criarAgendamento(agendamentoDto);

            // Assert
            verify(agendamentoRepository).save(any(Agendamento.class));
        }

        @Test
        @DisplayName("Deve lançar exception quando barbeiro indisponível")
        void deveLancarExceptionQuandoBarbeiroIndisponivel() {
            // Arrange
            when(clienteService.buscarEntidadeClientePorEmail(any())).thenReturn(cliente);
            when(servicoService.buscarEntidadeServicoPorId(any())).thenReturn(servico);
            when(barbeiroService.buscarEntidadeBarbeiroPorEmail(any())).thenReturn(barbeiro);
            when(agendamentoRepository.existsByBarbeiro_BarbeiroIdAndHorarioAndStatus(
                    any(), any(), eq(StatusAgendamento.PENDENTE))).thenReturn(true); // Barbeiro ocupado

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.criarAgendamento(agendamentoDto))
                    .isInstanceOf(HorarioIndisponivelException.class)
                    .hasMessageContaining("Já existe um agendamento para este barbeiro");
        }

        @Test
        @DisplayName("Deve lançar exception quando horário fora do expediente (antes das 7h)")
        void deveLancarExceptionQuandoHorarioAntesDoExpediente() {
            // Arrange
            agendamentoDto.setHorario(LocalDateTime.of(2026, 2, 1, 6, 30)); // 6:30

            when(clienteService.buscarEntidadeClientePorEmail(any())).thenReturn(cliente);
            when(servicoService.buscarEntidadeServicoPorId(any())).thenReturn(servico);
            when(barbeiroService.buscarEntidadeBarbeiroPorEmail(any())).thenReturn(barbeiro);

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.criarAgendamento(agendamentoDto))
                    .isInstanceOf(AgendamentoInvalidoException.class)
                    .hasMessageContaining("Horário fora do expediente");
        }

        @Test
        @DisplayName("Deve lançar exception quando horário fora do expediente (depois das 21h)")
        void deveLancarExceptionQuandoHorarioDepoisDoExpediente() {
            // Arrange
            agendamentoDto.setHorario(LocalDateTime.of(2026, 2, 1, 21, 30)); // 21:30

            when(clienteService.buscarEntidadeClientePorEmail(any())).thenReturn(cliente);
            when(servicoService.buscarEntidadeServicoPorId(any())).thenReturn(servico);
            when(barbeiroService.buscarEntidadeBarbeiroPorEmail(any())).thenReturn(barbeiro);

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.criarAgendamento(agendamentoDto))
                    .isInstanceOf(AgendamentoInvalidoException.class)
                    .hasMessageContaining("Horário fora do expediente");
        }

        @Test
        @DisplayName("Deve lançar exception quando horário não é múltiplo de 30 minutos")
        void deveLancarExceptionQuandoHorarioNaoEhMultiploDe30() {
            // Arrange
            agendamentoDto.setHorario(LocalDateTime.of(2026, 2, 1, 10, 15)); // 10:15

            when(clienteService.buscarEntidadeClientePorEmail(any())).thenReturn(cliente);
            when(servicoService.buscarEntidadeServicoPorId(any())).thenReturn(servico);
            when(barbeiroService.buscarEntidadeBarbeiroPorEmail(any())).thenReturn(barbeiro);

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.criarAgendamento(agendamentoDto))
                    .isInstanceOf(AgendamentoInvalidoException.class)
                    .hasMessageContaining("intervalos de 30 minutos");
        }
    }

    @Nested
    @DisplayName("Testes de Cancelar Agendamento")
    class CancelarAgendamentoTests {

        @Test
        @DisplayName("Deve cancelar agendamento pendente com sucesso")
        void deveCancelarAgendamentoPendenteComSucesso() {
            // Arrange
            Agendamento agendamento = new Agendamento();
            agendamento.setAgendamentoId(1L);
            agendamento.setStatus(StatusAgendamento.PENDENTE);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

            // Act
            boolean resultado = agendamentoService.cancelarAgendamento(1L);

            // Assert
            assertThat(resultado).isTrue();
            assertThat(agendamento.getStatus()).isEqualTo(StatusAgendamento.CANCELADO);
            verify(agendamentoRepository).save(agendamento);
        }

        @Test
        @DisplayName("Não deve cancelar agendamento já concluído")
        void naoDeveCancelarAgendamentoConcluido() {
            // Arrange
            Agendamento agendamento = new Agendamento();
            agendamento.setAgendamentoId(1L);
            agendamento.setStatus(StatusAgendamento.CONCLUIDO);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.cancelarAgendamento(1L))
                    .isInstanceOf(CancelamentoInvalidoException.class);
        }

        @Test
        @DisplayName("Não deve cancelar agendamento já cancelado")
        void naoDeveCancelarAgendamentoJaCancelado() {
            // Arrange
            Agendamento agendamento = new Agendamento();
            agendamento.setAgendamentoId(1L);
            agendamento.setStatus(StatusAgendamento.CANCELADO);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.cancelarAgendamento(1L))
                    .isInstanceOf(CancelamentoInvalidoException.class);
        }
    }

    @Nested
    @DisplayName("Testes de Concluir Agendamento")
    class ConcluirAgendamentoTests {

        @Test
        @DisplayName("Deve concluir agendamento e criar pagamento")
        void deveConcluirAgendamentoECriarPagamento() {
            // Arrange
            Agendamento agendamento = new Agendamento();
            agendamento.setAgendamentoId(1L);
            agendamento.setStatus(StatusAgendamento.PENDENTE);
            agendamento.setServico(servico);
            agendamento.setCliente(cliente);
            agendamento.setBarbeiro(barbeiro);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

            // Act
            agendamentoService.concluirAgendamento(1L, "PIX");

            // Assert
            assertThat(agendamento.getStatus()).isEqualTo(StatusAgendamento.CONCLUIDO);
            verify(agendamentoRepository).save(agendamento);

            ArgumentCaptor<Pagamento> pagamentoCaptor = ArgumentCaptor.forClass(Pagamento.class);
            verify(pagamentoService).salvarPagamento(pagamentoCaptor.capture());

            Pagamento pagamentoSalvo = pagamentoCaptor.getValue();
            assertThat(pagamentoSalvo.getValor()).isEqualTo(35.0);
            assertThat(pagamentoSalvo.getFormaPagamento()).isEqualTo("PIX");

            verify(historicoAtendimentoService).registrar(eq(agendamento), any(Pagamento.class));
        }

        @Test
        @DisplayName("Não deve concluir agendamento cancelado")
        void naoDeveConcluirAgendamentoCancelado() {
            // Arrange
            Agendamento agendamento = new Agendamento();
            agendamento.setAgendamentoId(1L);
            agendamento.setStatus(StatusAgendamento.CANCELADO);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.concluirAgendamento(1L, "PIX"))
                    .isInstanceOf(ConclusaoInvalidaException.class);
        }

        @Test
        @DisplayName("Não deve concluir agendamento já concluído")
        void naoDeveConcluirAgendamentoJaConcluido() {
            // Arrange
            Agendamento agendamento = new Agendamento();
            agendamento.setAgendamentoId(1L);
            agendamento.setStatus(StatusAgendamento.CONCLUIDO);

            when(agendamentoRepository.findById(1L)).thenReturn(Optional.of(agendamento));

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.concluirAgendamento(1L, "PIX"))
                    .isInstanceOf(ConclusaoInvalidaException.class);
        }
    }

    @Nested
    @DisplayName("Testes de Buscar Agendamento")
    class BuscarAgendamentoTests {

        @Test
        @DisplayName("Deve lançar exception quando agendamento não encontrado")
        void deveLancarExceptionQuandoAgendamentoNaoEncontrado() {
            // Arrange
            when(agendamentoRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> agendamentoService.buscarAgendamento(999L))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
