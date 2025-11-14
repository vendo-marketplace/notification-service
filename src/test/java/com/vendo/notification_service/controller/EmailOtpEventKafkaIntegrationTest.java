package com.vendo.notification_service.controller;

import com.vendo.integration.kafka.event.EmailOtpEvent;
import com.vendo.notification_service.common.MailSender;
import com.vendo.notification_service.common.builder.EmailOtpEventDataBuilder;
import com.vendo.notification_service.integration.kafka.producer.TestProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
@ExtendWith(MockitoExtension.class)
public class EmailOtpEventKafkaIntegrationTest {

    @Autowired
    private TestProducer testProducer;

    @Mock
    private MailSender mailSender;

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenEmailVerificationEvent() {
        EmailOtpEvent emailOtpEvent = EmailOtpEventDataBuilder.buildEmailOtpEventWithRequiredFields()
                .otpEventType(EmailOtpEvent.OtpEventType.EMAIL_VERIFICATION)
                .build();
        String subject = "test_subject";
        String to = "test_to";
        String text = "test_text";

        verify(mailSender).sendMail(subject, to, text);

        testProducer.sendEmailOtpNotificationEvent(emailOtpEvent);
    }

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenPasswordRecoveryEvent() {

    }
}
