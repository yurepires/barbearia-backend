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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteServiceImpl - Testes Unitários")
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
        cliente.setNome("João Maria");
        cliente.setEmail("joao@email.com");
        cliente.setTelefone("(86) 9 9999-9999");
        cliente.setSenha("senhaEncriptada");

        clienteDto = new ClienteDto();
        clienteDto.setNome("João Maria");
        clienteDto.setEmail("joao@email.com");
        clienteDto.setTelefone("(86) 9 9999-9999");
        clienteDto.setSenha("minhasenha");
    }

    @Nested
    @DisplayName("criarCliente()")
    class CriarCliente {

        @Test
        @DisplayName("Deve criar cliente com sucesso e encriptar a senha")
        void deveCriarClienteComSucessoESenhaEncriptada() {
            // given
            given(clienteRepository.findByEmail(clienteDto.getEmail()))
                    .willReturn(Optional.empty());
            given(clienteMapper.toEntity(clienteDto))
                    .willReturn(cliente);
            given(passwordEncoder.encode(anyString()))
                    .willReturn("$2a$10$hashGerado");

            // when
            clienteService.criarCliente(clienteDto);

            // then
            verify(passwordEncoder, times(1)).encode(anyString());
            verify(clienteRepository, times(1)).save(cliente);
        }

        @Test
        @DisplayName("Deve criar cliente sem encriptar quando senha é nula")
        void deveCriarClienteSemEncriptarQuandoSenhaNula() {
            // given
            Cliente clienteSemSenha = new Cliente();
            clienteSemSenha.setNome("Maria");
            clienteSemSenha.setEmail("maria@email.com");
            clienteSemSenha.setSenha(null);

            given(clienteRepository.findByEmail(clienteDto.getEmail()))
                    .willReturn(Optional.empty());
            given(clienteMapper.toEntity(clienteDto))
                    .willReturn(clienteSemSenha);

            // when
            clienteService.criarCliente(clienteDto);

            // then
            verify(passwordEncoder, never()).encode(anyString());
            verify(clienteRepository, times(1)).save(clienteSemSenha);
        }

        @Test
        @DisplayName("Deve lançar exceção quando email já está cadastrado")
        void deveLancarExcecaoQuandoEmailJaExiste() {
            // given
            given(clienteRepository.findByEmail(clienteDto.getEmail()))
                    .willReturn(Optional.of(cliente));

            // when & then
            assertThatThrownBy(() -> clienteService.criarCliente(clienteDto))
                    .isInstanceOf(ClienteAlreadyExistsException.class)
                    .hasMessageContaining(clienteDto.getEmail());

            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Nested
    @DisplayName("buscarCliente()")
    class BuscarCliente {

        @Test
        @DisplayName("Deve retornar DTO do cliente quando encontrado por email")
        void deveRetornarClienteQuandoEncontrado() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));
            given(clienteMapper.toDto(cliente))
                    .willReturn(clienteDto);

            // when
            ClienteDto resultado = clienteService.buscarCliente("joao@email.com");

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getNome()).isEqualTo("João Maria");
            assertThat(resultado.getEmail()).isEqualTo("joao@email.com");
        }

        @Test
        @DisplayName("Deve lançar ResourceNotFoundException quando cliente não existe")
        void deveLancarExcecaoQuandoNaoEncontrado() {
            // given
            given(clienteRepository.findByEmail("inexistente@email.com"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clienteService.buscarCliente("inexistente@email.com"))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("inexistente@email.com");
        }
    }

    @Nested
    @DisplayName("buscarEntidadeClientePorEmail()")
    class BuscarEntidadeClientePorEmail {

        @Test
        @DisplayName("Deve retornar entidade Cliente quando encontrada")
        void deveRetornarEntidadeQuandoEncontrada() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));

            // when
            Cliente resultado = clienteService.buscarEntidadeClientePorEmail("joao@email.com");

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getClienteId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando entidade não encontrada")
        void deveLancarExcecaoQuandoEntidadeNaoEncontrada() {
            // given
            given(clienteRepository.findByEmail("inexistente@email.com"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clienteService.buscarEntidadeClientePorEmail("inexistente@email.com"))
                    .isInstanceOf(ResourceNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("buscarTodosClientes()")
    class BuscarTodosClientes {

        @Test
        @DisplayName("Deve retornar lista de clientes")
        void deveRetornarListaDeClientes() {
            // given
            given(clienteRepository.findAll())
                    .willReturn(List.of(cliente));
            given(clienteMapper.toDto(cliente))
                    .willReturn(clienteDto);

            // when
            List<ClienteDto> resultado = clienteService.buscarTodosClientes();

            // then
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNome()).isEqualTo("João Maria");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há clientes")
        void deveRetornarListaVaziaQuandoNaoHaClientes() {
            // given
            given(clienteRepository.findAll())
                    .willReturn(Collections.emptyList());

            // when
            List<ClienteDto> resultado = clienteService.buscarTodosClientes();

            // then
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("atualizarCliente()")
    class AtualizarCliente {

        @Test
        @DisplayName("Deve atualizar cliente existente com sucesso")
        void deveAtualizarClienteComSucesso() {
            // given
            given(clienteRepository.findByEmail(clienteDto.getEmail()))
                    .willReturn(Optional.of(cliente));

            // when
            boolean resultado = clienteService.atualizarCliente(clienteDto);

            // then
            assertThat(resultado).isTrue();
            verify(clienteMapper, times(1)).updateEntity(clienteDto, cliente);
            verify(clienteRepository, times(1)).save(cliente);
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar cliente inexistente")
        void deveLancarExcecaoAoAtualizarInexistente() {
            // given
            given(clienteRepository.findByEmail(clienteDto.getEmail()))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clienteService.atualizarCliente(clienteDto))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(clienteRepository, never()).save(any(Cliente.class));
        }
    }

    @Nested
    @DisplayName("deletarCliente()")
    class DeletarCliente {

        @Test
        @DisplayName("Deve deletar cliente existente com sucesso")
        void deveDeletarClienteComSucesso() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));

            // when
            boolean resultado = clienteService.deletarCliente("joao@email.com");

            // then
            assertThat(resultado).isTrue();
            verify(clienteRepository, times(1)).delete(cliente);
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar cliente inexistente")
        void deveLancarExcecaoAoDeletarInexistente() {
            // given
            given(clienteRepository.findByEmail("inexistente@email.com"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clienteService.deletarCliente("inexistente@email.com"))
                    .isInstanceOf(ResourceNotFoundException.class);

            verify(clienteRepository, never()).delete(any(Cliente.class));
        }
    }
}
