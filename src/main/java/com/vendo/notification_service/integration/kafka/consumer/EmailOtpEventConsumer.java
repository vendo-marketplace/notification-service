package com.vendo.notification_service.integration.kafka.consumer;

import com.vendo.event_lib.EmailOtpEvent;
import com.vendo.notification_service.service.otp.service.EmailOtpNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailOtpEventConsumer {

    private final EmailOtpNotificationService emailOtpNotificationService;

    @KafkaListener(
            topics = "${kafka.events.email-otp-notification-event.topic}",
            groupId = "${kafka.events.email-otp-notification-event.groupId}",
            properties = {"auto.offset.reset: ${kafka.events.email-otp-notification-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.email-otp-notification-event.container-factory}"
    )
    private void listenEmailOtpEvent(EmailOtpEvent event) {
        log.info("Received event for email otp notification: {}", event);
        emailOtpNotificationService.sendOtpNotification(event);
    }
}
