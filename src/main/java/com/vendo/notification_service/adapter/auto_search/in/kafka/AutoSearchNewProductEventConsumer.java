package com.vendo.notification_service.adapter.auto_search.in.kafka;

import com.vendo.event_lib.auto_search.AutoSearchEventType;
import com.vendo.event_lib.auto_search.AutoSearchNewProductEvent;
import com.vendo.notification_service.adapter.auto_search.in.mapper.AutoSearchEventMapper;
import com.vendo.notification_service.port.AutoSearchEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutoSearchNewProductEventConsumer {

    private final AutoSearchEventMapper mapper;
    private final AutoSearchEventUseCase autoSearchEventUseCase;

    @KafkaListener(
            topics = "${kafka.events.auto-search.new-product-event.topic}",
            groupId = "autoSearchReadyEventGroupId",
            properties = {"auto.offset.reset: ${kafka.events.auto-search.new-product-event.properties.auto-offset-reset}"},
            containerFactory = "${kafka.events.auto-search.new-product-event.container-factory}"
    )
    public void listenAutoSearchNewProductEvent(AutoSearchNewProductEvent event) {
        log.info("Received event for auto search new product: {}", event);
        autoSearchEventUseCase.send(AutoSearchEventType.AUTO_SEARCH_NEW_PRODUCT, mapper.toAutoSearch(event));
    }

}
