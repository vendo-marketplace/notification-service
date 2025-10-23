package com.vendo.notification_service.controller;

import com.vendo.notification_service.integration.mail_tm.MailTmService;
import com.vendo.notification_service.integration.mail_tm.dto.GetMessagesResponse;
import com.vendo.notification_service.integration.redis.common.config.RedisProperties;
import com.vendo.notification_service.integration.redis.service.RedisService;
import com.vendo.notification_service.kafka.producer.TestProducer;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.AfterTestClass;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmailVerificationEventConsumerKafkaIntegrationTest {

    @Autowired
    private TestProducer testProducer;

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private MailTmService mailTmService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushAll();
    }

    @AfterTestClass
    void tearDown() {
        redisTemplate.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushAll();
    }

    @Test
    void listenEmailVerificationEvent_shouldSendEmailNotification() {
        Integer otp = 123456;
        RedisProperties.EmailVerification emailVerification = redisProperties.getEmailVerification();
        String emailName = UUID.randomUUID().toString().substring(0, 6);
        String password = UUID.randomUUID().toString().substring(0, 6);

        String mailTmEmail = mailTmService.createAddressWithDomain(emailName, password);
        redisService.saveValue(emailVerification.getEmail().buildPrefix(mailTmEmail), String.valueOf(otp), emailVerification.getEmail().getTtl());
        testProducer.sendEmailVerificationEvent(mailTmEmail);

        await().atMost(25, TimeUnit.SECONDS).untilAsserted(() -> {
            Optional<String> redisOtp = redisService.getValue(emailVerification.getEmail().buildPrefix(mailTmEmail));
            assertThat(redisOtp).isPresent();

            List<GetMessagesResponse.Message> messages = mailTmService.retrieveTextFromMessage(mailTmEmail, password).getMessages();
            AssertionsForInterfaceTypes.assertThat(messages).isNotEmpty();
            assertThat(messages.get(0).getIntro()).isNotBlank();
            assertThat(messages.get(0).getIntro().contains(redisOtp.get())).isTrue();
        });
    }

    @Test
    void listenEmailVerificationEvent_shouldNotSendEmailNotification_whenOtpHasExpired() {
        String emailName = UUID.randomUUID().toString().substring(0, 6);
        String password = UUID.randomUUID().toString().substring(0, 6);

        String mailTmEmail = mailTmService.createAddressWithDomain(emailName, password);
        testProducer.sendEmailVerificationEvent(mailTmEmail);

        await().pollDelay(10, TimeUnit.SECONDS).atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            List<GetMessagesResponse.Message> messages = mailTmService.retrieveTextFromMessage(mailTmEmail, password).getMessages();
            AssertionsForInterfaceTypes.assertThat(messages).isEmpty();
        });
    }
}
