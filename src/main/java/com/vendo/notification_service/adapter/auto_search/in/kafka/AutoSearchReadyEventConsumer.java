package com.vendo.notification_service.adapter.auto_search.in.kafka;

import com.vendo.event_lib.auto_search.AutoSearchEventType;
import com.vendo.event_lib.auto_search.AutoSearchReadyEvent;
import com.vendo.notification_service.adapter.auto_search.in.mapper.AutoSearchEventMapper;
import com.vendo.notification_service.port.AutoSearchEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoSearchReadyEventConsumer {

    private final AutoSearchEventMapper mapper;
    private final AutoSearchEventUseCase autoSearchEventUseCase;

    @KafkaListener(
            topics = "${kafka.events.auto-search.ready-event.topic}",
            groupId = "autoSearchReadyEventGroupId",
            properties = {"auto.offset.reset: ${kafka.events.auto-search.ready-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.auto-search.ready-event.container-factory}"
    )
    public void listenAutoSearchReadyEvent(AutoSearchReadyEvent event) {
        log.info("Received event for auto search ready: {}", event);
        autoSearchEventUseCase.send(AutoSearchEventType.AUTO_SEARCH_REQUEST_FOUND, mapper.toAutoSearch(event));
    }

}
