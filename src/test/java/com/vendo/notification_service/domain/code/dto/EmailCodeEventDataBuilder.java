package com.vendo.notification_service.domain.code.dto;

import com.vendo.event_lib.code.CodeEmailEvent;

public class EmailCodeEventDataBuilder {

    public static CodeEmailEvent.Builder withRequiredFields() {
        return CodeEmailEvent.builder()
                .email("test@gmail.com")
                .code("123456");
    }

}
