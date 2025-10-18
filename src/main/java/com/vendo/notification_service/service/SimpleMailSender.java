package com.vendo.notification_service.service;

import com.vendo.notification_service.common.MailSender;
import com.vendo.notification_service.common.config.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SimpleMailSender implements MailSender {

    private final MailProperties mailProperties;

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMail(String subject, String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setSubject(subject);
        message.setText(text);
        message.setTo(to);
        message.setFrom(mailProperties.getFrom());

        javaMailSender.send(message);
    }

}
