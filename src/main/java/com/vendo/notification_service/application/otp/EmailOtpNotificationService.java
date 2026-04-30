package com.vendo.notification_service.application.otp;

import com.vendo.event_lib.otp.EmailOtpEvent;
import com.vendo.notification_service.port.mail.MailProviderPort;
import com.vendo.notification_service.port.otp.OtpTemplatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailOtpNotificationService {

    private final MailProviderPort mailSender;

    private final OtpTemplatePort otpTemplatePort;

    public void sendOtpNotification(EmailOtpEvent event) {
        if (event.otpEventType() == null) {
            throw new IllegalArgumentException("OtpEventType is required but got null.");
        }

        String otpTemplate = otpTemplatePort.getTemplate(event.otpEventType());
        String otpSubject = otpTemplatePort.getSubject(event.otpEventType());

        mailSender.sendMail(otpSubject, event.email(), otpTemplate.formatted(event.otp()));
    }
}