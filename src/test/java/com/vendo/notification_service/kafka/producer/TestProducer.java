package com.vendo.notification_service.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestProducer {

    @Value("${kafka.events.password-recovery-event.topic}")
    private String passwordRecoveryEventTopic;

    @Value("${kafka.events.email-verification-event.topic}")
    private String emailVerificationEventTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendPasswordRecoveryEvent(String email) {
        log.info("Sent event for password recovery: {}", email);
        kafkaTemplate.send(passwordRecoveryEventTopic, email);
    }

    public void sendEmailVerificationEvent(String email) {
        log.info("Sent event for email verification: {}", email);
        kafkaTemplate.send(emailVerificationEventTopic, email);
    }
}
