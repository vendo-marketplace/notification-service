package com.vendo.notification_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    @Value("${server.host}")
    private String SERVER_HOST;

    @Value("${server.port}")
    private int SERVER_PORT;

    private final SimpleMailSender simpleMailSender;

    public void sendRecoveryPasswordEmail(String token) {
        String passwordRecoveryLink = buildPasswordRecoveryLink(token);
        simpleMailSender.sendMail("Recovery password", "melnuk2004y@gmail.com", "Link for password recovery: " + passwordRecoveryLink);
    }

    private String buildPasswordRecoveryLink(String token) {
        return "http://%s:%d/auth/reset-password?token=%s".formatted(SERVER_HOST, SERVER_PORT, token);
    }

}