package com.vendo.notification_service.application.otp;

import com.vendo.event_lib.EmailOtpEvent;
import com.vendo.notification_service.port.mail.MailProviderPort;
import com.vendo.notification_service.port.otp.OtpTemplateProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailOtpNotificationService {

    private final MailProviderPort mailSender;

    private final OtpTemplateProviderPort otpEventProviderPort;

    public void sendOtpNotification(EmailOtpEvent event) {
        if (event.getOtpEventType() == null) {
            throw new IllegalArgumentException("OtpEventType is required but got null.");
        }

        String otpTemplate = otpEventProviderPort.getTemplate(event.getOtpEventType());
        String otpSubject = otpEventProviderPort.getSubject(event.getOtpEventType());

        mailSender.sendMail(otpSubject, event.getEmail(), otpTemplate.formatted(event.getOtp()));
    }
}