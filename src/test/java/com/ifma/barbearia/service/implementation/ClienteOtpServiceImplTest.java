package com.ifma.barbearia.service.implementation;

import com.ifma.barbearia.dto.OtpRequestDto;
import com.ifma.barbearia.dto.OtpResponseDto;
import com.ifma.barbearia.dto.OtpValidateDto;
import com.ifma.barbearia.entity.Cliente;
import com.ifma.barbearia.entity.ClienteOtp;
import com.ifma.barbearia.repository.ClienteOtpRepository;
import com.ifma.barbearia.repository.ClienteRepository;
import com.ifma.barbearia.security.JwtUtil;
import com.ifma.barbearia.service.EmailService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteOtpServiceImpl - Testes Unitários")
class ClienteOtpServiceImplTest {

    @Mock
    private ClienteOtpRepository otpRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private ClienteOtpServiceImpl clienteOtpService;

    private Cliente cliente;
    private ClienteOtp clienteOtp;
    private OtpRequestDto otpRequestDto;
    private OtpValidateDto otpValidateDto;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setEmail("joao@email.com");

        clienteOtp = new ClienteOtp();
        clienteOtp.setOtpId(1L);
        clienteOtp.setEmail("joao@email.com");
        clienteOtp.setOtp("123456");
        clienteOtp.setExpiration(LocalDateTime.now().plusMinutes(10));
        clienteOtp.setUsed(false);

        otpRequestDto = new OtpRequestDto();
        otpRequestDto.setEmail("joao@email.com");

        otpValidateDto = new OtpValidateDto();
        otpValidateDto.setEmail("joao@email.com");
        otpValidateDto.setOtp("123456");
    }

    @Nested
    @DisplayName("enviarCodigoPorEmail()")
    class EnviarCodigoPorEmail {

        @Test
        @DisplayName("Deve gerar e enviar código OTP com sucesso quando cliente existe")
        void deveEnviarCodigoOtpComSucessoQuandoClienteExiste() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));

            // when
            clienteOtpService.enviarCodigoPorEmail(otpRequestDto);

            // then
            verify(otpRepository, times(1)).save(any(ClienteOtp.class));
            verify(emailService, times(1)).enviarEmail(
                    eq("joao@email.com"),
                    anyString(),
                    anyString()
            );
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando cliente não existe")
        void deveLancarExcecaoQuandoClienteNaoExiste() {
            // given
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clienteOtpService.enviarCodigoPorEmail(otpRequestDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Cliente não encontrado");

            verify(otpRepository, never()).save(any(ClienteOtp.class));
            verify(emailService, never()).enviarEmail(anyString(), anyString(), anyString());
        }
    }

    @Nested
    @DisplayName("validarCodigo()")
    class ValidarCodigo {

        @Test
        @DisplayName("Deve validar código OTP com sucesso e retornar token JWT")
        void deveValidarCodigoComSucessoERetornarToken() {
            // given
            given(otpRepository.findFirstByEmailAndOtpAndUsedFalseOrderByExpirationDesc(
                    "joao@email.com", "123456"))
                    .willReturn(Optional.of(clienteOtp));
            given(clienteRepository.findByEmail("joao@email.com"))
                    .willReturn(Optional.of(cliente));
            given(jwtUtil.generateToken("joao@email.com", "CLIENTE"))
                    .willReturn("jwt-token-otp");

            // when
            OtpResponseDto resultado = clienteOtpService.validarCodigo(otpValidateDto);

            // then
            assertThat(resultado).isNotNull();
            assertThat(resultado.getToken()).isEqualTo("jwt-token-otp");
            assertThat(clienteOtp.getUsed()).isTrue();
            verify(otpRepository, times(1)).save(clienteOtp);
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando código OTP é inválido ou inexistente")
        void deveLancarExcecaoQuandoCodigoInvalido() {
            // given
            given(otpRepository.findFirstByEmailAndOtpAndUsedFalseOrderByExpirationDesc(
                    "joao@email.com", "123456"))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> clienteOtpService.validarCodigo(otpValidateDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Código inválido ou expirado");

            verify(jwtUtil, never()).generateToken(anyString(), anyString());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando código OTP está expirado")
        void deveLancarExcecaoQuandoCodigoExpirado() {
            // given
            clienteOtp.setExpiration(LocalDateTime.now().minusMinutes(5)); // já expirado
            given(otpRepository.findFirstByEmailAndOtpAndUsedFalseOrderByExpirationDesc(
                    "joao@email.com", "123456"))
                    .willReturn(Optional.of(clienteOtp));

            // when & then
            assertThatThrownBy(() -> clienteOtpService.validarCodigo(otpValidateDto))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Código expirado");

            verify(jwtUtil, never()).generateToken(anyString(), anyString());
        }
    }
}
