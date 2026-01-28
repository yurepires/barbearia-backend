package com.ifma.barbearia.integration;

import com.ifma.barbearia.entity.*;
import com.ifma.barbearia.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de integração para relatórios.
 * Testa queries complexas com dados reais no banco H2.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RelatorioIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private HistoricoAtendimentoRepository historicoAtendimentoRepository;

    private Cliente cliente1;
    private Cliente cliente2;
    private Barbeiro barbeiro;
    private Servico servicoCorte;
    private Servico servicoBarba;
    private LocalDate inicio;
    private LocalDate fim;

    @BeforeEach
    void setUp() {
        // Limpar dados
        historicoAtendimentoRepository.deleteAll();
        pagamentoRepository.deleteAll();
        agendamentoRepository.deleteAll();

        // Datas do período de teste
        inicio = LocalDate.now().minusDays(30);
        fim = LocalDate.now();

        // Criar clientes
        cliente1 = clienteRepository.findByEmail("cliente1_relatorio@teste.com")
                .orElseGet(() -> {
                    Cliente c = new Cliente();
                    c.setNome("Cliente Frequente");
                    c.setEmail("cliente1_relatorio@teste.com");
                    c.setTelefone("11999999999");
                    return clienteRepository.save(c);
                });

        cliente2 = clienteRepository.findByEmail("cliente2_relatorio@teste.com")
                .orElseGet(() -> {
                    Cliente c = new Cliente();
                    c.setNome("Cliente Ocasional");
                    c.setEmail("cliente2_relatorio@teste.com");
                    c.setTelefone("11888888888");
                    return clienteRepository.save(c);
                });

        // Criar barbeiro
        barbeiro = barbeiroRepository.findByEmail("barbeiro_relatorio@teste.com")
                .orElseGet(() -> {
                    Barbeiro b = new Barbeiro();
                    b.setNome("Barbeiro Relatório");
                    b.setEmail("barbeiro_relatorio@teste.com");
                    b.setTelefone("11777777777");
                    return barbeiroRepository.save(b);
                });

        // Criar serviços
        servicoCorte = servicoRepository.findByNome("Corte Relatório")
                .orElseGet(() -> {
                    Servico s = new Servico();
                    s.setNome("Corte Relatório");
                    s.setDescricao("Corte para teste de relatório");
                    s.setPreco(50.0);
                    return servicoRepository.save(s);
                });

        servicoBarba = servicoRepository.findByNome("Barba Relatório")
                .orElseGet(() -> {
                    Servico s = new Servico();
                    s.setNome("Barba Relatório");
                    s.setDescricao("Barba para teste de relatório");
                    s.setPreco(30.0);
                    return servicoRepository.save(s);
                });

        // Criar histórico de atendimentos para testes de relatório
        criarAtendimentoCompleto(cliente1, servicoCorte, LocalDateTime.now().minusDays(5), 50.0);
        criarAtendimentoCompleto(cliente1, servicoCorte, LocalDateTime.now().minusDays(10), 50.0);
        criarAtendimentoCompleto(cliente1, servicoBarba, LocalDateTime.now().minusDays(15), 30.0);
        criarAtendimentoCompleto(cliente2, servicoCorte, LocalDateTime.now().minusDays(7), 50.0);
    }

    private void criarAtendimentoCompleto(Cliente cliente, Servico servico, LocalDateTime data, Double valor) {
        // Criar agendamento
        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setHorario(data);
        agendamento = agendamentoRepository.save(agendamento);

        // Criar pagamento
        Pagamento pagamento = new Pagamento();
        pagamento.setAgendamento(agendamento);
        pagamento.setValor(valor);
        pagamento.setFormaPagamento("PIX");
        pagamento.setDataPagamento(data);
        pagamento = pagamentoRepository.save(pagamento);

        // Criar histórico de atendimento
        HistoricoAtendimento historico = new HistoricoAtendimento();
        historico.setCliente(cliente);
        historico.setBarbeiro(barbeiro);
        historico.setServico(servico);
        historico.setPagamento(pagamento);
        historico.setData(data);
        historicoAtendimentoRepository.save(historico);
    }

    @Test
    @DisplayName("Deve gerar relatório consolidado por período")
    @WithMockUser(roles = "ADM")
    void deveGerarRelatorioConsolidadoPorPeriodo() throws Exception {
        mockMvc.perform(get("/api/relatorios/relatorioPorIntervaloDeData")
                .param("inicio", inicio.toString())
                .param("fim", fim.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotalPagamentos").exists())
                .andExpect(jsonPath("$.historicosAtendimentos").isArray());
    }

    @Test
    @DisplayName("Deve listar serviços mais vendidos ordenados por quantidade")
    @WithMockUser(roles = "ADM")
    void deveListarServicosMaisVendidosOrdenados() throws Exception {
        // Corte foi vendido 3x, Barba foi vendida 1x
        mockMvc.perform(get("/api/relatorios/servicosMaisVendidos")
                .param("inicio", inicio.toString())
                .param("fim", fim.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[0].nomeServico").value("Corte Relatório"))
                .andExpect(jsonPath("$[0].quantidadeVendas").value(3));
    }

    @Test
    @DisplayName("Deve listar clientes mais frequentes ordenados por atendimentos")
    @WithMockUser(roles = "ADM")
    void deveListarClientesMaisFrequentesOrdenados() throws Exception {
        // Cliente1 tem 3 atendimentos, Cliente2 tem 1
        mockMvc.perform(get("/api/relatorios/clientesMaisFrequentes")
                .param("inicio", inicio.toString())
                .param("fim", fim.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$[0].nomeCliente").value("Cliente Frequente"))
                .andExpect(jsonPath("$[0].quantidadeAtendimentos").value(3));
    }

    @Test
    @DisplayName("Deve rejeitar quando data fim é anterior à data início")
    @WithMockUser(roles = "ADM")
    void deveRejeitarQuandoDataFimAnteriorAInicio() throws Exception {
        mockMvc.perform(get("/api/relatorios/relatorioPorIntervaloDeData")
                .param("inicio", fim.toString())
                .param("fim", inicio.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há dados no período")
    @WithMockUser(roles = "ADM")
    void deveRetornarListaVaziaQuandoNaoHaDados() throws Exception {
        LocalDate inicioFuturo = LocalDate.now().plusYears(1);
        LocalDate fimFuturo = LocalDate.now().plusYears(1).plusMonths(1);

        mockMvc.perform(get("/api/relatorios/servicosMaisVendidos")
                .param("inicio", inicioFuturo.toString())
                .param("fim", fimFuturo.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
