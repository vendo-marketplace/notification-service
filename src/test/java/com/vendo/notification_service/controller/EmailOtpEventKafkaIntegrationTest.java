package com.vendo.notification_service.controller;

import com.vendo.event_lib.EmailOtpEvent;
import com.vendo.notification_service.common.MailSender;
import com.vendo.notification_service.common.builder.EmailOtpEventDataBuilder;
import com.vendo.notification_service.integration.kafka.producer.TestProducer;
import com.vendo.notification_service.service.otp.common.config.OtpMailProperties;
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
public class EmailOtpEventKafkaIntegrationTest {

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private OtpMailProperties otpMailProperties;

    @MockitoBean
    private MailSender mailSender;

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenEmailVerificationEvent() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(EmailOtpEvent.OtpEventType.EMAIL_VERIFICATION)
                .build();
        String otpTemplate = otpMailProperties.getTemplates().get(event.getOtpEventType());
        String otpSubject = otpMailProperties.getSubjects().get(event.getOtpEventType());

        testProducer.sendEmailOtpNotificationEvent(event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> verify(mailSender).sendMail(
                otpSubject,
                event.getEmail(),
                otpTemplate.formatted(event.getOtp())
        ));
    }

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenPasswordRecoveryEvent() {
        EmailOtpEvent event = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(EmailOtpEvent.OtpEventType.PASSWORD_RECOVERY)
                .build();
        String otpTemplate = otpMailProperties.getTemplates().get(event.getOtpEventType());
        String otpSubject = otpMailProperties.getSubjects().get(event.getOtpEventType());

        testProducer.sendEmailOtpNotificationEvent(event);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> verify(mailSender).sendMail(
                otpSubject,
                event.getEmail(),
                otpTemplate.formatted(event.getOtp())
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
                .untilAsserted(() -> verify(mailSender, never()).sendMail(
                        subject,
                        event.getEmail(),
                        text));
    }
}
