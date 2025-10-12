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
    private String passwordRecoveryEmailNotificationEventTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendRecoveryPasswordNotificationEvent(String token) {
        log.info("Sent token for password recovery: {}", token);
        kafkaTemplate.send(passwordRecoveryEmailNotificationEventTopic, token);
    }
}
