package com.vendo.notification_service.domain.code.dto;

import com.vendo.event_lib.auto_search.AutoSearchReadyEvent;

public class AutoSearchReadyEventDataBuilder {

    public static AutoSearchReadyEvent withAllFields() {
        return new AutoSearchReadyEvent("requestId", "email");
    }
}
