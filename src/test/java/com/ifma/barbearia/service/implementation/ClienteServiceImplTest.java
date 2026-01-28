package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.ClienteDto;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.exceptions.ClienteAlreadyExistsException;
import com.ifma.barbearia.exceptions.ResourceNotFoundException;
import com.ifma.barbearia.mapper.ClienteMapper;
import com.ifma.barbearia.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para ClienteServiceImpl.
 */
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteDto clienteDto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setEmail("cliente@teste.com");
        cliente.setNome("Cliente Teste");
        cliente.setTelefone("11999999999");

        clienteDto = new ClienteDto();
        clienteDto.setEmail("cliente@teste.com");
        clienteDto.setNome("Cliente Teste");
        clienteDto.setTelefone("11999999999");
    }

    @Nested
    @DisplayName("Testes de Criar Cliente")
    class CriarClienteTests {

        @Test
        @DisplayName("Deve criar cliente com sucesso")
        void deveCriarClienteComSucesso() {
            // Arrange
            when(clienteRepository.findByEmail(clienteDto.getEmail())).thenReturn(Optional.empty());
            when(clienteMapper.toEntity(clienteDto)).thenReturn(cliente);

            // Act
            clienteService.criarCliente(clienteDto);

            // Assert
            verify(clienteRepository).save(cliente);
        }

        @Test
        @DisplayName("Deve criptografar senha ao criar cliente")
        void deveCriptografarSenhaAoCriarCliente() {
            // Arrange
            clienteDto.setSenha("senha123");
            cliente.setSenha("senha123");

            when(clienteRepository.findByEmail(clienteDto.getEmail())).thenReturn(Optional.empty());
            when(clienteMapper.toEntity(clienteDto)).thenReturn(cliente);
            when(passwordEncoder.encode("senha123")).thenReturn("$2a$10$encoded");

            // Act
            clienteService.criarCliente(clienteDto);

            // Assert
            verify(passwordEncoder).encode("senha123");

            ArgumentCaptor<Cliente> clienteCaptor = ArgumentCaptor.forClass(Cliente.class);
            verify(clienteRepository).save(clienteCaptor.capture());
            assertThat(clienteCaptor.getValue().getSenha()).isEqualTo("$2a$10$encoded");
        }

        @Test
        @DisplayName("Deve lançar exception quando email já existe")
        void deveLancarExceptionQuandoEmailJaExiste() {
            // Arrange
            when(clienteRepository.findByEmail(clienteDto.getEmail())).thenReturn(Optional.of(cliente));

            // Act & Assert
            assertThatThrownBy(() -> clienteService.criarCliente(clienteDto))
                    .isInstanceOf(ClienteAlreadyExistsException.class);

            verify(clienteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Buscar Cliente")
    class BuscarClienteTests {

        @Test
        @DisplayName("Deve buscar cliente por email")
        void deveBuscarClientePorEmail() {
            // Arrange
            when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
            when(clienteMapper.toDto(cliente)).thenReturn(clienteDto);

            // Act
            ClienteDto resultado = clienteService.buscarCliente("cliente@teste.com");

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getEmail()).isEqualTo("cliente@teste.com");
        }

        @Test
        @DisplayName("Deve lançar exception quando cliente não encontrado")
        void deveLancarExceptionQuandoClienteNaoEncontrado() {
            // Arrange
            when(clienteRepository.findByEmail("inexistente@teste.com")).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> clienteService.buscarCliente("inexistente@teste.com"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        @DisplayName("Deve buscar todos os clientes")
        void deveBuscarTodosClientes() {
            // Arrange
            Cliente cliente2 = new Cliente();
            cliente2.setClienteId(2L);
            cliente2.setEmail("outro@teste.com");

            when(clienteRepository.findAll()).thenReturn(List.of(cliente, cliente2));
            when(clienteMapper.toDto(any(Cliente.class))).thenReturn(clienteDto);

            // Act
            List<ClienteDto> resultado = clienteService.buscarTodosClientes();

            // Assert
            assertThat(resultado).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Testes de Atualizar Cliente")
    class AtualizarClienteTests {

        @Test
        @DisplayName("Deve atualizar cliente com sucesso")
        void deveAtualizarClienteComSucesso() {
            // Arrange
            when(clienteRepository.findByEmail(clienteDto.getEmail())).thenReturn(Optional.of(cliente));

            // Act
            boolean resultado = clienteService.atualizarCliente(clienteDto);

            // Assert
            assertThat(resultado).isTrue();
            verify(clienteMapper).updateEntity(clienteDto, cliente);
            verify(clienteRepository).save(cliente);
        }

        @Test
        @DisplayName("Deve lançar exception ao atualizar cliente inexistente")
        void deveLancarExceptionAoAtualizarClienteInexistente() {
            // Arrange
            when(clienteRepository.findByEmail(clienteDto.getEmail())).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> clienteService.atualizarCliente(clienteDto))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Testes de Deletar Cliente")
    class DeletarClienteTests {

        @Test
        @DisplayName("Deve deletar cliente com sucesso")
        void deveDeletarClienteComSucesso() {
            // Arrange
            when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));

            // Act
            boolean resultado = clienteService.deletarCliente("cliente@teste.com");

            // Assert
            assertThat(resultado).isTrue();
            verify(clienteRepository).delete(cliente);
        }

        @Test
        @DisplayName("Deve lançar exception ao deletar cliente inexistente")
        void deveLancarExceptionAoDeletarClienteInexistente() {
            // Arrange
            when(clienteRepository.findByEmail("inexistente@teste.com")).thenReturn(Optional.empty());

            // Act & Assert
            assertThatThrownBy(() -> clienteService.deletarCliente("inexistente@teste.com"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }
}
