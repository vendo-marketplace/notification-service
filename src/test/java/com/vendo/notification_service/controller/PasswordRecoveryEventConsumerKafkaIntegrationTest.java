package com.vendo.notification_service.controller;

import com.vendo.notification_service.integration.mail_tm.MailTmService;
import com.vendo.notification_service.integration.mail_tm.dto.GetMessagesResponse;
import com.vendo.notification_service.integration.redis.common.config.RedisProperties;
import com.vendo.notification_service.integration.redis.service.RedisService;
import com.vendo.notification_service.kafka.producer.TestProducer;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PasswordRecoveryEventConsumerKafkaIntegrationTest {

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
    void listenRecoveryPasswordEvent_shouldSendEmailNotification_andDeleteOldToken() {
        String recoveryPasswordToken = String.valueOf(UUID.randomUUID());
        String emailPrefix = recoveryPasswordToken.substring(0, 6);
        String password = recoveryPasswordToken.substring(0, 6);

        String mailTmEmail = mailTmService.createAddressWithDomain(emailPrefix, password);
        redisService.saveValue(redisProperties.getResetPassword().getPrefixes().getTokenPrefix() + recoveryPasswordToken, mailTmEmail, redisProperties.getResetPassword().getTtl());
        testProducer.sendRecoveryPasswordNotificationEvent(recoveryPasswordToken);

        await().atMost(25, TimeUnit.SECONDS).untilAsserted(() -> {
            Optional<String> email = redisService.getValue(redisProperties.getResetPassword().getPrefixes().getTokenPrefix() + recoveryPasswordToken);
            assertThat(email).isNotPresent();

            List<GetMessagesResponse.Message> messages = mailTmService.retrieveTextFromMessage(mailTmEmail, password).getMessages();
            assertThat(messages).isNotEmpty();
            assertThat(messages.get(0).getIntro()).isNotBlank();
            assertThat(messages.get(0).getIntro().contains(recoveryPasswordToken)).isTrue();
        });
    }

    @Test
    void listenRecoveryPasswordEvent_shouldNotSendNotification_whenTokenHasExpired() {
        String recoveryPasswordToken = String.valueOf(UUID.randomUUID());
        String emailPrefix = recoveryPasswordToken.substring(0, 6);
        String password = recoveryPasswordToken.substring(0, 6);

        String mailTmEmail = mailTmService.createAddressWithDomain(emailPrefix, password);
        redisService.saveValue(redisProperties.getResetPassword().getPrefixes().getTokenPrefix() + recoveryPasswordToken, mailTmEmail, 1);
        testProducer.sendRecoveryPasswordNotificationEvent(recoveryPasswordToken);

        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            Optional<String> email = redisService.getValue(redisProperties.getResetPassword().getPrefixes().getTokenPrefix() + recoveryPasswordToken);
            Optional<String> token = redisService.getValue(redisProperties.getResetPassword().getPrefixes().getEmailPrefix() + mailTmEmail);
            assertThat(email).isNotPresent();
            assertThat(token).isNotPresent();
        });

        await().pollDelay(10, TimeUnit.SECONDS).atMost(15, TimeUnit.SECONDS).untilAsserted(() -> {
            List<GetMessagesResponse.Message> messages = mailTmService.retrieveTextFromMessage(mailTmEmail, password).getMessages();
            assertThat(messages).isEmpty();
        });
    }
}
