package com.vendo.notification_service.adapter.mail.out.brevo;

import brevo.ApiException;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BrevoMailAdapterTest {

    @Mock
    private TransactionalEmailsApi transactionalEmailsApi;

    @Mock
    private SendSmtpEmailSender sendSmtpEmailSender;

    @InjectMocks
    private BrevoMailAdapter brevoMailAdapter;

    @Captor
    private ArgumentCaptor<SendSmtpEmail> captorEmailOtpEvent;

    @Test
    void sendMail_shouldCorrectlyMapDataAndCallBrevoApi() throws ApiException {
        String subject = "Test Subject";
        String to = "test@example.com";
        String text = "Test Content";

        brevoMailAdapter.sendMail(subject, to, text);

        verify(transactionalEmailsApi).sendTransacEmail(captorEmailOtpEvent.capture());

        SendSmtpEmail capturedEmail = captorEmailOtpEvent.getValue();

        assertEquals(subject, capturedEmail.getSubject());
        assertEquals(text, capturedEmail.getTextContent());
        assertEquals(to, capturedEmail.getTo().get(0).getEmail());
        assertEquals(sendSmtpEmailSender, capturedEmail.getSender());
    }
}
