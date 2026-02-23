package com.vendo.notification_service.adapter.otp.out.config;

import com.vendo.event_lib.EmailOtpEvent;
import com.vendo.notification_service.infrastructure.config.mail.OtpMailProperties;
import com.vendo.notification_service.port.otp.OtpTemplatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OtpTemplateAdapter implements OtpTemplatePort {

    private final OtpMailProperties otpMailProperties;

    @Override
    public String getSubject(EmailOtpEvent.OtpEventType eventType) {
        return otpMailProperties.getSubjects().get(eventType);
    }

    @Override
    public String getTemplate(EmailOtpEvent.OtpEventType eventType) {
        return otpMailProperties.getTemplates().get(eventType);
    }
}
