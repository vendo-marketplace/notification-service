package com.vendo.notification_service.adapter.otp.in.messaging.kafka.consumer;

import com.vendo.event_lib.otp.EmailOtpEvent;
import com.vendo.notification_service.application.otp.EmailOtpNotificationService;
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
            topics = "${kafka.events.notification.email-otp-event.topic}",
            groupId = "${kafka.events.notification.email-otp-event.groupId}",
            properties = {"auto.offset.reset: ${kafka.events.notification.email-otp-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.notification.email-otp-event.container-factory}"
    )
    private void listenEmailOtpEvent(EmailOtpEvent event) {
        log.info("Received event for email otp notification: {}", event);
        emailOtpNotificationService.sendOtpNotification(event);
    }
}
