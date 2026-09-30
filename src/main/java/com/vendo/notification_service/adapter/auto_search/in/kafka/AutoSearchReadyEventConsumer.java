package com.vendo.notification_service.adapter.auto_search.in.kafka;

import com.vendo.event_lib.auto_search.AutoSearchReadyEvent;
import com.vendo.notification_service.port.AutoSearchReadyUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoSearchReadyEventConsumer {

    private final AutoSearchReadyUseCase autoSearchReadyUseCase;

    @KafkaListener(
            topics = "${kafka.events.auto-search.ready-event.topic}",
            groupId = "${kafka.events.auto-search.ready-event.groupId}",
            properties = {"auto.offset.reset: ${kafka.events.auto-search.ready-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.auto-search.ready-event.container-factory}"
    )
    public void listenAutoSearchReadyEvent(AutoSearchReadyEvent event) {
        log.info("Received event for auto search ready: {}", event);
        autoSearchReadyUseCase.send(event);
    }

}
