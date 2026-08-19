package com.vendo.notification_service.domain.code.dto;

import com.vendo.event_lib.auto_search.AutoSearchEmailEvent;

import java.math.BigDecimal;
import java.util.List;

public class AutoSearchEmailEventDataBuilder {

    public static AutoSearchEmailEvent withAllFields() {
        return new AutoSearchEmailEvent("requestId", "email", List.of(
                new AutoSearchEmailEvent.ResultProduct("productId1", "title", BigDecimal.TEN),
                new AutoSearchEmailEvent.ResultProduct("productId2", "title", BigDecimal.ONE),
                new AutoSearchEmailEvent.ResultProduct("productId3", "title", BigDecimal.valueOf(1.5))
        ));
    }
}
