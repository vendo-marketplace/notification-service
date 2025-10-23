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
public class EmailVerificationEventConsumer {

    private final EmailNotificationService emailNotificationService;

    @KafkaListener(
            topics = "${kafka.events.email-verification-event.topic}",
            groupId = "${kafka.events.email-verification-event.groupId}",
            properties = {"auto.offset.reset: ${kafka.events.email-verification-event.properties.auto-offset-reset}"}
    )
    public void listenEmailVerificationEvent(@Payload String email) {
        log.info("Received event for email verification: {}", email);
        emailNotificationService.sendEmailVerificationOtp(email);
    }
}
