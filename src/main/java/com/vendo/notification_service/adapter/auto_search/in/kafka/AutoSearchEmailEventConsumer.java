package com.vendo.notification_service.adapter.auto_search.in.kafka;

import com.vendo.event_lib.auto_search.AutoSearchEmailEvent;
import com.vendo.notification_service.port.AutoSearchNotificationUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoSearchEmailEventConsumer {

    private final AutoSearchNotificationUseCase autoSearchNotificationUseCase;

    @KafkaListener(
            topics = "${kafka.events.notification.auto-search-email-event.topic}",
            groupId = "${kafka.events.notification.auto-search-email-event.groupId}",
            properties = {"auto.offset.reset: ${kafka.events.notification.auto-search-email-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.notification.auto-search-email-event.container-factory}"
    )
    private void listenAutoSearchEmailEvent(AutoSearchEmailEvent event) {
        log.info("Received event for auto search email notification: {}", event);
        autoSearchNotificationUseCase.send(event);
    }

}
