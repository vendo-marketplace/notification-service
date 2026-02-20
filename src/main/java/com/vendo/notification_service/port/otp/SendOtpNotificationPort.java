package com.vendo.notification_service.port.otp;

import com.vendo.event_lib.EmailOtpEvent;

public interface SendOtpNotificationPort {
    void sendOtpNotification(EmailOtpEvent event);
}
