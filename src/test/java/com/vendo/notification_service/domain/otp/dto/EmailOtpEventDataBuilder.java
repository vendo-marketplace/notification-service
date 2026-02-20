package com.vendo.notification_service.domain.otp.dto;

import com.vendo.event_lib.EmailOtpEvent;

public class EmailOtpEventDataBuilder {

    public static EmailOtpEvent.EmailOtpEventBuilder buildEmailOtpEventWithRequiredFields() {
        return EmailOtpEvent.builder()
                .email("test@gmail.com")
                .otp("123456");
    }

}
