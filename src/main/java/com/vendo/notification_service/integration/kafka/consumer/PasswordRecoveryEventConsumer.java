package com.vendo.notification_service.integration.kafka.consumer;

import com.vendo.notification_service.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordRecoveryEventConsumer {

    private final EmailNotificationService emailNotificationService;

    @KafkaListener(
            topics = "${kafka.events.password-recovery-event.topic}",
            groupId = "${kafka.events.password-recovery-event.groupId}",
            properties = {"auto.offset.reset: ${kafka.events.password-recovery-event.properties.auto-offset-reset}"}
    )
    public void listenPasswordRecoveryEvent(@Payload String email) {
        log.info("Received event for password recovery: {}", email);
        emailNotificationService.sendPasswordRecoveryOtp(email);
    }
}
