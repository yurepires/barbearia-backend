package com.ifma.barbearia.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ifma.barbearia.dto.AgendamentoDto;
import com.ifma.barbearia.entity.Barbeiro;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.entity.Servico;
import com.ifma.barbearia.entity.enums.StatusAgendamento;
import com.ifma.barbearia.repository.AgendamentoRepository;
import com.ifma.barbearia.repository.BarbeiroRepository;
import com.ifma.barbearia.repository.ClienteRepository;
import com.ifma.barbearia.repository.ServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste de integração para o fluxo de agendamento.
 * Testa criar, buscar, cancelar e concluir agendamentos.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AgendamentoIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private AgendamentoRepository agendamentoRepository;

        @Autowired
        private ClienteRepository clienteRepository;

        @Autowired
        private BarbeiroRepository barbeiroRepository;

        @Autowired
        private ServicoRepository servicoRepository;

        private Cliente cliente;
        private Barbeiro barbeiro;
        private Servico servico;
        private AgendamentoDto agendamentoDto;

        @BeforeEach
        void setUp() {
                objectMapper.registerModule(new JavaTimeModule());

                // Limpar dados
                agendamentoRepository.deleteAll();

                // Criar cliente de teste
                cliente = clienteRepository.findByEmail("cliente_integracao@teste.com")
                                .orElseGet(() -> {
                                        Cliente c = new Cliente();
                                        c.setNome("Cliente Integração");
                                        c.setEmail("cliente_integracao@teste.com");
                                        c.setTelefone("11999999999");
                                        return clienteRepository.save(c);
                                });

                // Criar barbeiro de teste
                barbeiro = barbeiroRepository.findByEmail("barbeiro_integracao@teste.com")
                                .orElseGet(() -> {
                                        Barbeiro b = new Barbeiro();
                                        b.setNome("Barbeiro Integração");
                                        b.setEmail("barbeiro_integracao@teste.com");
                                        b.setTelefone("11888888888");
                                        return barbeiroRepository.save(b);
                                });

                // Criar serviço de teste
                servico = servicoRepository.findByNome("Corte Integração")
                                .orElseGet(() -> {
                                        Servico s = new Servico();
                                        s.setNome("Corte Integração");
                                        s.setDescricao("Corte para teste de integração");
                                        s.setPreco(50.0);
                                        return servicoRepository.save(s);
                                });

                // Preparar DTO de agendamento
                agendamentoDto = new AgendamentoDto();
                agendamentoDto.setClienteEmail(cliente.getEmail());
                agendamentoDto.setBarbeiroEmail(barbeiro.getEmail());
                agendamentoDto.setServicoId(servico.getServicoId());
                // Horário válido: próximo dia útil às 10:00
                agendamentoDto.setHorario(
                                LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0));
        }

        @Test
        @DisplayName("Deve criar agendamento com sucesso")
        @WithMockUser(roles = "ADM")
        void deveCriarAgendamentoComSucesso() throws Exception {
                mockMvc.perform(post("/api/agendamento/criarAgendamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agendamentoDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.statusCode").value("201"));

                // Verificar se foi persistido
                assertThat(agendamentoRepository.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("Deve buscar agendamento criado")
        @WithMockUser(roles = "ADM")
        void deveBuscarAgendamentoCriado() throws Exception {
                // Criar agendamento primeiro
                MvcResult result = mockMvc.perform(post("/api/agendamento/criarAgendamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agendamentoDto)))
                                .andExpect(status().isCreated())
                                .andReturn();

                // Buscar o ID do agendamento criado
                Long agendamentoId = agendamentoRepository.findAll().get(0).getAgendamentoId();

                // Buscar agendamento
                mockMvc.perform(get("/api/agendamento/buscarAgendamento")
                                .param("agendamentoId", agendamentoId.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.clienteEmail").value(cliente.getEmail()))
                                .andExpect(jsonPath("$.barbeiroEmail").value(barbeiro.getEmail()));
        }

        @Test
        @DisplayName("Deve cancelar agendamento pendente")
        @WithMockUser(roles = "ADM")
        void deveCancelarAgendamentoPendente() throws Exception {
                // Criar agendamento
                mockMvc.perform(post("/api/agendamento/criarAgendamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agendamentoDto)))
                                .andExpect(status().isCreated());

                Long agendamentoId = agendamentoRepository.findAll().get(0).getAgendamentoId();

                // Cancelar agendamento
                mockMvc.perform(patch("/api/agendamento/cancelarAgendamento")
                                .param("agendamentoId", agendamentoId.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusCode").value("200"));

                // Verificar status
                assertThat(agendamentoRepository.findById(agendamentoId).get().getStatus())
                                .isEqualTo(StatusAgendamento.CANCELADO);
        }

        @Test
        @DisplayName("Deve concluir agendamento e registrar pagamento")
        @WithMockUser(roles = "ADM")
        void deveConcluirAgendamentoERegistrarPagamento() throws Exception {
                // Criar agendamento
                mockMvc.perform(post("/api/agendamento/criarAgendamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agendamentoDto)))
                                .andExpect(status().isCreated());

                Long agendamentoId = agendamentoRepository.findAll().get(0).getAgendamentoId();

                // Concluir agendamento
                mockMvc.perform(patch("/api/agendamento/concluirAgendamento")
                                .param("agendamentoId", agendamentoId.toString())
                                .param("formaPagamento", "PIX"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.statusCode").value("200"));

                // Verificar status
                assertThat(agendamentoRepository.findById(agendamentoId).get().getStatus())
                                .isEqualTo(StatusAgendamento.CONCLUIDO);
        }

        @Test
        @DisplayName("Deve rejeitar agendamento fora do horário de funcionamento")
        @WithMockUser(roles = "ADM")
        void deveRejeitarAgendamentoForaDoHorario() throws Exception {
                // Tentar agendar às 6:00 (antes da abertura às 7:00)
                agendamentoDto.setHorario(LocalDateTime.now().plusDays(1).withHour(6).withMinute(0));

                mockMvc.perform(post("/api/agendamento/criarAgendamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agendamentoDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve rejeitar agendamento quando barbeiro já está ocupado")
        @WithMockUser(roles = "ADM")
        void deveRejeitarQuandoBarbeiroOcupado() throws Exception {
                // Criar primeiro agendamento
                mockMvc.perform(post("/api/agendamento/criarAgendamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agendamentoDto)))
                                .andExpect(status().isCreated());

                // Tentar criar segundo agendamento no mesmo horário
                mockMvc.perform(post("/api/agendamento/criarAgendamento")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agendamentoDto)))
                                .andExpect(status().isBadRequest());
        }
}
