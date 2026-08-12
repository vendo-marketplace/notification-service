package com.vendo.notification_service.domain.code.dto;

import com.vendo.event_lib.code.EmailCodeEvent;

public class EmailCodeEventDataBuilder {

    public static EmailCodeEvent.Builder buildEmailCodeEventWithRequiredFields() {
        return EmailCodeEvent.builder()
                .email("test@gmail.com")
                .code("123456");
    }

}
