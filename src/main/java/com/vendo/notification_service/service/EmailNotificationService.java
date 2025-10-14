package com.vendo.notification_service.service;

import com.vendo.notification_service.integration.redis.common.config.RedisProperties;
import com.vendo.notification_service.integration.redis.common.exception.RedisValueExpiredException;
import com.vendo.notification_service.integration.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    @Value("${server.host}")
    private String SERVER_HOST;

    @Value("${server.port}")
    private int SERVER_PORT;

    private final SimpleMailSender simpleMailSender;

    private final RedisService redisService;

    private final RedisProperties redisProperties;

    @Transactional
    public void sendRecoveryPasswordEmail(String token) {
        Optional<String> email = redisService.getValue(redisProperties.getResetPassword().getPrefixes().getTokenPrefix() + token);
        if (email.isEmpty()) {
            throw new RedisValueExpiredException("Password recovery token has expired");
        }

        String passwordRecoveryLink = buildPasswordRecoveryLink(token);
        simpleMailSender.sendMail("Recovery password", email.get(), "Link for password recovery: " + passwordRecoveryLink);
    }

    private String buildPasswordRecoveryLink(String token) {
        return "http://%s:%d/auth/reset-password?token=%s".formatted(SERVER_HOST, SERVER_PORT, token);
    }
}