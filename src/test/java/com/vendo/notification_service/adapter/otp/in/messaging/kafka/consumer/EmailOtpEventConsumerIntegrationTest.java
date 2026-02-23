package com.vendo.notification_service.adapter.otp.in.messaging.kafka.consumer;

import com.vendo.event_lib.EmailOtpEvent;
import com.vendo.notification_service.domain.otp.dto.EmailOtpEventDataBuilder;
import com.vendo.notification_service.infrastructure.config.kafka.TestProducer;
import com.vendo.notification_service.port.mail.MailProviderPort;
import com.vendo.notification_service.port.otp.OtpTemplatePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka
@ActiveProfiles("test")
public class EmailOtpEventConsumerIntegrationTest {

    @Autowired
    private TestProducer testProducer;

    @MockitoBean
    private OtpTemplatePort otpTemplatePort;

    @MockitoBean
    private MailProviderPort mailProviderPort;

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenEmailVerificationEvent() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(EmailOtpEvent.OtpEventType.EMAIL_VERIFICATION)
                .build();

        String template = "Verification code is %s";
        String subject = "Email Verification";
        when(otpTemplatePort.getTemplate(event.getOtpEventType())).thenReturn(template);
        when(otpTemplatePort.getSubject(event.getOtpEventType())).thenReturn(subject);

        testProducer.sendEmailOtpNotificationEvent(event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> verify(mailProviderPort).sendMail(
                subject,
                event.getEmail(),
                template.formatted(event.getOtp())
        ));
    }

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenPasswordRecoveryEvent() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(EmailOtpEvent.OtpEventType.PASSWORD_RECOVERY)
                .build();

        String template = "Verification code is %s";
        String subject = "Email Verification";
        when(otpTemplatePort.getTemplate(event.getOtpEventType())).thenReturn(template);
        when(otpTemplatePort.getSubject(event.getOtpEventType())).thenReturn(subject);

        testProducer.sendEmailOtpNotificationEvent(event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> verify(mailProviderPort).sendMail(
                subject,
                event.getEmail(),
                template.formatted(event.getOtp())
        ));
    }

    @Test
    void listenEmailOtpEvent_shouldNotSentNotification_whenEventTypeIsNull() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(null)
                .build();
        String subject = "test_subject";
        String text = "test_text";

        testProducer.sendEmailOtpNotificationEvent(event);

        await().pollDelay(3, TimeUnit.SECONDS)
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(mailProviderPort, never()).sendMail(
                        subject,
                        event.getEmail(),
                        text));
    }
}
