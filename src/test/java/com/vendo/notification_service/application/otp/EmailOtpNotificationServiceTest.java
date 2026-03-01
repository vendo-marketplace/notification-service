package com.vendo.notification_service.application.otp;

import com.vendo.event_lib.EmailOtpEvent;
import com.vendo.notification_service.domain.otp.dto.EmailOtpEventDataBuilder;
import com.vendo.notification_service.port.mail.MailProviderPort;
import com.vendo.notification_service.port.otp.OtpTemplatePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailOtpNotificationServiceTest {

    @Mock
    private OtpTemplatePort otpTemplatePort;

    @Mock
    private MailProviderPort mailProviderPort;

    @InjectMocks
    private EmailOtpNotificationService emailOtpNotificationService;

    @Test
    void sendEmailOtpEvent_shouldSendEmailNotification_whenEmailVerificationEvent() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(EmailOtpEvent.OtpEventType.EMAIL_VERIFICATION)
                .build();

        String template = "Verification code is %s";
        String subject = "Email Verification";
        when(otpTemplatePort.getTemplate(event.getOtpEventType())).thenReturn(template);
        when(otpTemplatePort.getSubject(event.getOtpEventType())).thenReturn(subject);

        emailOtpNotificationService.sendOtpNotification(event);

        verify(mailProviderPort).sendMail(
                subject,
                event.getEmail(),
                template.formatted(event.getOtp())
        );
    }

    @Test
    void sendEmailOtpEvent_shouldSendEmailNotification_whenPasswordRecoveryEvent() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(EmailOtpEvent.OtpEventType.PASSWORD_RECOVERY)
                .build();

        String template = "Recovery code is %s";
        String subject = "Password Recovery";
        when(otpTemplatePort.getTemplate(event.getOtpEventType())).thenReturn(template);
        when(otpTemplatePort.getSubject(event.getOtpEventType())).thenReturn(subject);

        emailOtpNotificationService.sendOtpNotification(event);

        verify(mailProviderPort).sendMail(
                subject,
                event.getEmail(),
                template.formatted(event.getOtp())
        );
    }

    @Test
    void sendEmailOtpEvent_shouldNotSentNotification_whenEventTypeIsNull() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(null)
                .build();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> emailOtpNotificationService.sendOtpNotification(event)
        );

        assertEquals("OtpEventType is required but got null.", exception.getMessage());

        verifyNoInteractions(mailProviderPort);
    }
}
