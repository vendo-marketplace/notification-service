package com.vendo.notification_service.service;

import com.vendo.notification_service.common.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleMailSender implements MailSender {

    @Value("${spring.mail.username}")
    private String MESSAGE_FROM;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(String subject, String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(subject);
        message.setText(text);
        message.setTo(to);
        message.setFrom(MESSAGE_FROM);

        javaMailSender.send(message);
    }

}
