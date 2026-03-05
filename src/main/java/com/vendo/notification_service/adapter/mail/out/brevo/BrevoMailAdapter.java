package com.vendo.notification_service.adapter.mail.out.brevo;

import brevo.ApiException;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmail;
import brevoModel.SendSmtpEmailSender;
import brevoModel.SendSmtpEmailTo;
import com.vendo.notification_service.port.mail.MailProviderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrevoMailAdapter implements MailProviderPort {

    private final TransactionalEmailsApi transactionalEmailsApi;

    private final SendSmtpEmailSender sender;

    @Override
    public void sendMail(String subject, String to, String text) {
        SendSmtpEmail email = new SendSmtpEmail()
                .sender(sender)
                .to(Collections.singletonList(new SendSmtpEmailTo().email(to)))
                .subject(subject)
                .textContent(text);

        try {
            transactionalEmailsApi.sendTransacEmail(email);
        } catch (ApiException e) {
            log.error("Brevo error sending email: {} - {}", e.getMessage(), e.getResponseBody());
        }
    }
}
