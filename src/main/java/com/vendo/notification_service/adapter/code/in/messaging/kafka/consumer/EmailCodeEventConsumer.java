package com.vendo.notification_service.adapter.code.in.messaging.kafka.consumer;

import com.vendo.event_lib.code.EmailCodeEvent;
import com.vendo.notification_service.port.code.EmailCodeNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailCodeEventConsumer {

    private final EmailCodeNotificationUseCase emailCodeNotificationUseCase;

    @KafkaListener(
            topics = "${kafka.events.notification.code-email-event.topic}",
            groupId = "${kafka.events.notification.code-email-event.groupId}",
            properties = {"auto.offset.reset: ${kafka.events.notification.code-email-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.notification.code-email-event.container-factory}"
    )
    private void listenEmailCodeEvent(EmailCodeEvent event) {
        log.info("Received event for code email notification: {}", event);
        emailCodeNotificationUseCase.send(event);
    }
}
