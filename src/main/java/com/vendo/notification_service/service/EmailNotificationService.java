package com.vendo.notification_service.service;

import com.vendo.integration.redis.common.exception.RedisValueExpiredException;
import com.vendo.notification_service.common.MailSender;
import com.vendo.notification_service.integration.redis.common.config.RedisProperties;
import com.vendo.notification_service.integration.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final MailSender mailSender;

    private final RedisService redisService;

    private final RedisProperties redisProperties;

    public void sendRecoveryPasswordOtp(String email) {
        Optional<String> otp = redisService.getValue(redisProperties.getPasswordRecovery().getEmail().buildPrefix(email));
        if (otp.isEmpty()) {
            throw new RedisValueExpiredException("Otp has expired");
        }

        mailSender.sendMail("Recovery password", email, "Use this OTP for password recovery: %s".formatted(otp.get()));
    }
}