package com.vendo.notification_service.service.otp.service;

import com.vendo.integration.kafka.event.EmailOtpEvent;
import com.vendo.notification_service.common.MailSender;
import com.vendo.notification_service.service.otp.common.config.OtpMailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailOtpNotificationService {

    private final MailSender mailSender;

    private final OtpMailProperties otpMailProperties;

    public void sendOtpNotification(EmailOtpEvent event) {
        if (event.getOtpEventType() == null) {
            throw new IllegalArgumentException("OtpEventType is required but got null.");
        }

        String otpTemplate = otpMailProperties.getTemplates().get(event.getOtpEventType());
        String otpSubject = otpMailProperties.getSubjects().get(event.getOtpEventType());

        mailSender.sendMail(otpSubject, event.getEmail(), otpTemplate.formatted(event.getOtp()));
    }
}