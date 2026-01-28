package com.ifma.barbearia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ifma.barbearia.dto.ClienteDto;
import com.ifma.barbearia.exceptions.ClienteAlreadyExistsException;
import com.ifma.barbearia.exceptions.GlobalExceptionHandler;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.service.IClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de controller para ClienteController.
 */
@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private ObjectMapper objectMapper;
    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();

        clienteDto = new ClienteDto();
        clienteDto.setEmail("cliente@teste.com");
        clienteDto.setNome("Cliente Teste");
        clienteDto.setTelefone("11999999999");
    }

    @Nested
    @DisplayName("POST /api/cliente/criarCliente")
    class CriarClienteTests {

        @Test
        @DisplayName("Deve criar cliente com sucesso")
        void deveCriarClienteComSucesso() throws Exception {
            doNothing().when(clienteService).criarCliente(any(ClienteDto.class));

            mockMvc.perform(post("/api/cliente/criarCliente")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clienteDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.statusCode").value("201"));
        }

        @Test
        @DisplayName("Deve retornar erro quando email já existe")
        void deveRetornarErroQuandoEmailJaExiste() throws Exception {
            doThrow(new ClienteAlreadyExistsException("Email já cadastrado"))
                    .when(clienteService).criarCliente(any(ClienteDto.class));

            mockMvc.perform(post("/api/cliente/criarCliente")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clienteDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("GET /api/cliente/buscarCliente")
    class BuscarClienteTests {

        @Test
        @DisplayName("Deve buscar cliente pelo email")
        void deveBuscarClientePeloEmail() throws Exception {
            when(clienteService.buscarCliente("cliente@teste.com")).thenReturn(clienteDto);

            mockMvc.perform(get("/api/cliente/buscarCliente")
                    .param("email", "cliente@teste.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nome").value("Cliente Teste"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando cliente não encontrado")
        void deveRetornar404QuandoClienteNaoEncontrado() throws Exception {
            when(clienteService.buscarCliente("inexistente@teste.com"))
                    .thenThrow(new ResourceNotFoundException("Cliente", "email", "inexistente@teste.com"));

            mockMvc.perform(get("/api/cliente/buscarCliente")
                    .param("email", "inexistente@teste.com"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/cliente/buscarTodosClientes")
    class BuscarTodosClientesTests {

        @Test
        @DisplayName("Deve buscar todos os clientes")
        void deveBuscarTodosClientes() throws Exception {
            ClienteDto dto2 = new ClienteDto();
            dto2.setEmail("outro@teste.com");
            dto2.setNome("Outro Cliente");

            when(clienteService.buscarTodosClientes()).thenReturn(List.of(clienteDto, dto2));

            mockMvc.perform(get("/api/cliente/buscarTodosClientes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2));
        }
    }

    @Nested
    @DisplayName("PUT /api/cliente/atualizarCliente")
    class AtualizarClienteTests {

        @Test
        @DisplayName("Deve atualizar cliente com sucesso")
        void deveAtualizarClienteComSucesso() throws Exception {
            when(clienteService.atualizarCliente(any(ClienteDto.class))).thenReturn(true);

            mockMvc.perform(put("/api/cliente/atualizarCliente")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(clienteDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }
    }

    @Nested
    @DisplayName("DELETE /api/cliente/deletarCliente")
    class DeletarClienteTests {

        @Test
        @DisplayName("Deve deletar cliente com sucesso")
        void deveDeletarClienteComSucesso() throws Exception {
            when(clienteService.deletarCliente("cliente@teste.com")).thenReturn(true);

            mockMvc.perform(delete("/api/cliente/deletarCliente")
                    .param("email", "cliente@teste.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value("200"));
        }
    }
}
