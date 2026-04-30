package com.vendo.notification_service.port.otp;

import com.vendo.event_lib.otp.OtpEventType;

public interface OtpTemplatePort {
    String getSubject(OtpEventType eventType);
    String getTemplate(OtpEventType eventType);
}
