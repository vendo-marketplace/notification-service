package com.vendo.notification_service.port.otp;

import com.vendo.event_lib.EmailOtpEvent.OtpEventType;

public interface OtpTemplateProviderPort {
    String getSubject(OtpEventType eventType);

    String getTemplate(OtpEventType eventType);
}
