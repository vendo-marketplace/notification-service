package com.vendo.notification_service.controller;

import com.vendo.integration.kafka.event.EmailOtpEvent;
import com.vendo.notification_service.integration.kafka.producer.TestProducer;
import com.vendo.notification_service.integration.mail_tm.MailTmService;
import com.vendo.notification_service.integration.mail_tm.common.dto.GetMessagesResponse;
import com.vendo.notification_service.integration.mail_tm.common.exception.MailTmException;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.Assume.assumeNoException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmailOtpEventKafkaIntegrationTest {

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private MailTmService mailTmService;

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenEmailVerificationEvent() {
        String emailName = UUID.randomUUID().toString().substring(0, 8);
        String password = UUID.randomUUID().toString().substring(0, 8);
        String otp = "123456";

        try {
            String mailTmEmail = mailTmService.createAddressWithDomain(emailName, password);
            EmailOtpEvent event = EmailOtpEvent.builder()
                    .email(mailTmEmail)
                    .otp(otp)
                    .otpEventType(EmailOtpEvent.OtpEventType.EMAIL_VERIFICATION)
                    .build();
            testProducer.sendEmailOtpNotificationEvent(event);

            await().pollInterval(1, TimeUnit.SECONDS).atMost(25, TimeUnit.SECONDS).untilAsserted(() -> {
                List<GetMessagesResponse.Message> messages = mailTmService.retrieveTextFromMessage(mailTmEmail, password).getMessages();
                assertThat(messages).isNotNull();
                assertThat(messages.isEmpty()).isFalse();
                assertThat(messages.get(0).getIntro()).isNotBlank();
                assertThat(messages.get(0).getIntro().contains(otp)).isTrue();
            });
        } catch (MailTmException e) {
            assumeNoException(e);
        }
    }

    @Test
    void listenEmailOtpEvent_shouldSendEmailNotification_whenPasswordRecoveryEvent() {
        String emailName = UUID.randomUUID().toString().substring(0, 8);
        String password = UUID.randomUUID().toString().substring(0, 8);
        String otp = "123456";

        try {
            String mailTmEmail = mailTmService.createAddressWithDomain(emailName, password);
            EmailOtpEvent event = EmailOtpEvent.builder()
                    .email(mailTmEmail)
                    .otp(otp)
                    .otpEventType(EmailOtpEvent.OtpEventType.PASSWORD_RECOVERY)
                    .build();
            testProducer.sendEmailOtpNotificationEvent(event);
            await().atMost(25, TimeUnit.SECONDS).untilAsserted(() -> {
                List<GetMessagesResponse.Message> messages = mailTmService.retrieveTextFromMessage(mailTmEmail, password).getMessages();
                AssertionsForInterfaceTypes.assertThat(messages).isNotEmpty();
                assertThat(messages.get(0).getIntro()).isNotBlank();
                assertThat(messages.get(0).getIntro().contains(otp)).isTrue();
            });
        } catch (MailTmException e) {
            assumeNoException(e);
        }
    }
}
