package com.vendo.notification_service.domain.code.dto;

import com.vendo.event_lib.auto_search.AutoSearchEmailEvent;

public class AutoSearchEmailEventDataBuilder {

    public static AutoSearchEmailEvent withAllFields() {
        return new AutoSearchEmailEvent("requestId", "email");
    }
}
