package com.vendo.notification_service.application.code;

import com.vendo.event_lib.code.CodeEventType;
import com.vendo.event_lib.code.EmailCodeEvent;
import com.vendo.notification_service.domain.code.dto.EmailCodeEventDataBuilder;
import com.vendo.notification_service.port.mail.MailProviderPort;
import com.vendo.notification_service.port.code.CodeTemplatePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailCodeNotificationServiceTest {

    @Mock
    private CodeTemplatePort codeTemplatePort;

    @Mock
    private MailProviderPort mailProviderPort;

    @InjectMocks
    private EmailCodeNotificationService emailCodeNotificationService;

    @Test
    void sendEmailCodeEvent_shouldSendEmailNotification_whenEmailVerificationEvent() {
        EmailCodeEvent event = EmailCodeEventDataBuilder.buildEmailCodeEventWithRequiredFields()
                .type(CodeEventType.EMAIL_VERIFICATION)
                .build();

        String template = "Verification code is %s";
        String subject = "Email Verification";
        when(codeTemplatePort.getTemplate(event.type())).thenReturn(template);
        when(codeTemplatePort.getSubject(event.type())).thenReturn(subject);

        emailCodeNotificationService.send(event);

        verify(codeTemplatePort).getTemplate(event.type());
        verify(codeTemplatePort).getSubject(event.type());
        verify(mailProviderPort).sendMail(
                subject,
                event.email(),
                template.formatted(event.code())
        );
    }

    @Test
    void sendEmailCodeEvent_shouldSendEmailNotification_whenPasswordRecoveryEvent() {
        EmailCodeEvent event = EmailCodeEventDataBuilder.buildEmailCodeEventWithRequiredFields()
                .type(CodeEventType.PASSWORD_RECOVERY)
                .build();

        String template = "Recovery url is http://localhost:3100/password-recovery?code=%s";
        String subject = "Password Recovery";
        when(codeTemplatePort.getTemplate(event.type())).thenReturn(template);
        when(codeTemplatePort.getSubject(event.type())).thenReturn(subject);

        emailCodeNotificationService.send(event);

        verify(codeTemplatePort).getTemplate(event.type());
        verify(codeTemplatePort).getSubject(event.type());
        verify(mailProviderPort).sendMail(
                subject,
                event.email(),
                template.formatted(event.code())
        );
    }

    @Test
    void sendEmailCodeEvent_shouldNotSentNotification_whenEventTypeIsNull() {
        EmailCodeEvent event = EmailCodeEventDataBuilder.buildEmailCodeEventWithRequiredFields()
                .type(null)
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> emailCodeNotificationService.send(event)
        );

        assertEquals("Type is required.", exception.getMessage());

        verifyNoInteractions(codeTemplatePort);
        verifyNoInteractions(mailProviderPort);
    }
}
