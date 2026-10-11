package com.vendo.notification_service.domain.code.dto;

import com.vendo.event_lib.code.CodeEmailEvent;

public class CodeEmailEventDataBuilder {

    public static CodeEmailEvent.CodeEmailEventBuilder withRequiredFields() {
        return CodeEmailEvent.builder()
                .email("test@gmail.com")
                .code("123456");
    }

}
