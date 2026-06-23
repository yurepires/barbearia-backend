package com.ifma.barbearia.service;

import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailService - Testes Unitários")
class EmailServiceTest {

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService();
        // Injeta a chave falsa de API via ReflectionTestUtils (substitui o @Value)
        ReflectionTestUtils.setField(emailService, "sendGridApiKey", "SG.fake-api-key-for-tests");
    }

    @Nested
    @DisplayName("enviarEmail()")
    class EnviarEmail {

        @Test
        @DisplayName("Deve enviar e-mail com sucesso sem lançar exceção")
        void deveEnviarEmailComSucesso() throws IOException {
            // Mocka a construção do SendGrid para evitar chamada de rede real
            try (MockedConstruction<SendGrid> mockedSendGrid =
                         mockConstruction(SendGrid.class, (mock, context) -> {
                             Response response = new Response();
                             response.setStatusCode(202);
                             when(mock.api(any())).thenReturn(response);
                         })) {

                // when & then: não deve lançar nenhuma exceção
                emailService.enviarEmail(
                        "destino@email.com",
                        "Assunto de teste",
                        "Corpo do e-mail de teste"
                );

                // Verifica que o SendGrid foi instanciado e chamado
                SendGrid sendGridMockado = mockedSendGrid.constructed().get(0);
                verify(sendGridMockado, times(1)).api(any());
            }
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando o SendGrid retornar IOException")
        void deveLancarRuntimeExceptionQuandoSendGridFalha(){
            // Mocka o SendGrid para lançar IOException
            try (MockedConstruction<SendGrid> mockedSendGrid =
                         mockConstruction(SendGrid.class, (mock, context) ->
                                 doThrow(new IOException("Falha de rede simulada"))
                                         .when(mock).api(any()))) {

                // when & then
                assertThatThrownBy(() -> emailService.enviarEmail(
                        "destino@email.com",
                        "Assunto de teste",
                        "Corpo do e-mail de teste"
                ))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Falha ao enviar e-mail pelo SendGrid")
                        .hasCauseInstanceOf(IOException.class);
            }
        }
    }
}
