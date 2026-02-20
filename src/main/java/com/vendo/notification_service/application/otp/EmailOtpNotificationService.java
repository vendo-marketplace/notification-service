package com.vendo.notification_service.application.otp;

import com.vendo.event_lib.EmailOtpEvent;
import com.vendo.notification_service.infrastructure.config.mail.OtpMailProperties;
import com.vendo.notification_service.port.mail.MailProviderPort;
import com.vendo.notification_service.port.otp.SendOtpNotificationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailOtpNotificationService implements SendOtpNotificationPort {

    private final MailProviderPort mailSender;

    private final OtpMailProperties otpMailProperties;

    @Override
    public void sendOtpNotification(EmailOtpEvent event) {
        if (event.getOtpEventType() == null) {
            throw new IllegalArgumentException("OtpEventType is required but got null.");
        }

        String otpTemplate = otpMailProperties.getTemplates().get(event.getOtpEventType());
        String otpSubject = otpMailProperties.getSubjects().get(event.getOtpEventType());

        mailSender.sendMail(otpSubject, event.getEmail(), otpTemplate.formatted(event.getOtp()));
    }
}