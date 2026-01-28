package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.entity.*;
import com.ifma.barbearia.repository.HistoricoAtendimentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setNome("João");
        cliente.setEmail("joao@teste.com");

        barbeiro = new Barbeiro();
        barbeiro.setNome("Carlos");
        barbeiro.setEmail("carlos@teste.com");

        servico = new Servico();
        servico.setServicoId(1L);
        servico.setNome("Corte");

        agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);

        pagamento = new Pagamento();
        pagamento.setPagamentoId(1L);
        pagamento.setValor(50.0);
    }

    @Test
    @DisplayName("Deve registrar histórico de atendimento com dados do agendamento")
    void deveRegistrarHistoricoComDadosDoAgendamento() {
        // Arrange
        ArgumentCaptor<HistoricoAtendimento> captor = ArgumentCaptor.forClass(HistoricoAtendimento.class);
        when(historicoAtendimentoRepository.save(any(HistoricoAtendimento.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        historicoAtendimentoService.registrar(agendamento, pagamento);

        // Assert
        verify(historicoAtendimentoRepository).save(captor.capture());
        HistoricoAtendimento salvo = captor.getValue();

        assertThat(salvo.getCliente()).isEqualTo(cliente);
        assertThat(salvo.getBarbeiro()).isEqualTo(barbeiro);
        assertThat(salvo.getServico()).isEqualTo(servico);
        assertThat(salvo.getPagamento()).isEqualTo(pagamento);
        assertThat(salvo.getData()).isNotNull();
    }

    @Test
    @DisplayName("Deve listar todos os históricos de atendimento")
    void deveListarTodosHistoricos() {
        // Arrange
        HistoricoAtendimento h1 = new HistoricoAtendimento();
        HistoricoAtendimento h2 = new HistoricoAtendimento();

        when(historicoAtendimentoRepository.findAll()).thenReturn(List.of(h1, h2));

        // Act
        List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarTodos();

        // Assert
        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("Deve listar históricos por cliente")
    void deveListarHistoricosPorCliente() {
        // Arrange
        HistoricoAtendimento h1 = new HistoricoAtendimento();

        when(historicoAtendimentoRepository.findByCliente_Email("joao@teste.com"))
                .thenReturn(List.of(h1));

        // Act
        List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorCliente("joao@teste.com");

        // Assert
        assertThat(resultado).hasSize(1);
        verify(historicoAtendimentoRepository).findByCliente_Email("joao@teste.com");
    }

    @Test
    @DisplayName("Deve listar históricos por barbeiro")
    void deveListarHistoricosPorBarbeiro() {
        // Arrange
        HistoricoAtendimento h1 = new HistoricoAtendimento();

        when(historicoAtendimentoRepository.findByBarbeiro_Email("carlos@teste.com"))
                .thenReturn(List.of(h1));

        // Act
        List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorBarbeiro("carlos@teste.com");

        // Assert
        assertThat(resultado).hasSize(1);
        verify(historicoAtendimentoRepository).findByBarbeiro_Email("carlos@teste.com");
    }

    @Test
    @DisplayName("Deve listar históricos por serviço")
    void deveListarHistoricosPorServico() {
        // Arrange
        HistoricoAtendimento h1 = new HistoricoAtendimento();

        when(historicoAtendimentoRepository.findByServico_ServicoId(1L))
                .thenReturn(List.of(h1));

        // Act
        List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorServico(1L);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(historicoAtendimentoRepository).findByServico_ServicoId(1L);
    }

    @Test
    @DisplayName("Deve listar históricos por intervalo de datas")
    void deveListarHistoricosPorIntervaloDeDatas() {
        // Arrange
        LocalDate inicio = LocalDate.of(2026, 1, 1);
        LocalDate fim = LocalDate.of(2026, 1, 31);
        HistoricoAtendimento h1 = new HistoricoAtendimento();

        when(historicoAtendimentoRepository.findByDataBetween(any(), any()))
                .thenReturn(List.of(h1));

        // Act
        List<HistoricoAtendimento> resultado = historicoAtendimentoService.listarPorIntervaloDeDatas(inicio, fim);

        // Assert
        assertThat(resultado).hasSize(1);
        verify(historicoAtendimentoRepository).findByDataBetween(any(), any());
    }
}
