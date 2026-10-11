package com.vendo.notification_service.adapter.code.in.kafka;

import com.vendo.event_lib.code.CodeEmailEvent;
import com.vendo.notification_service.port.code.CodeEmailNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CodeEmailEventConsumer {

    private final CodeEmailNotificationUseCase codeEmailNotificationUseCase;

    @KafkaListener(
            topics = "${kafka.events.notification.code-email-event.topic}",
            groupId = "codeEmailEventGroupId",
            properties = {"auto.offset.reset: ${kafka.events.notification.code-email-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.notification.code-email-event.container-factory}"
    )
    private void listenCodeEmailEvent(CodeEmailEvent event) {
        log.info("Received event for code email notification: {}", event);
        codeEmailNotificationUseCase.send(event);
    }
}
