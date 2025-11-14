package com.vendo.notification_service.common.builder;

import com.vendo.integration.kafka.event.EmailOtpEvent;

public class EmailOtpEventDataBuilder {

    public static EmailOtpEvent.EmailOtpEventBuilder buildEmailOtpEventWithRequiredFields() {
        return EmailOtpEvent.builder()
                .email("test@gmail.com")
                .otp("123456");
    }

}
