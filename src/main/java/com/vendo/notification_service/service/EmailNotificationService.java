package com.vendo.notification_service.service;

import com.vendo.integration.redis.common.exception.RedisValueExpiredException;
import com.vendo.notification_service.integration.redis.common.config.RedisProperties;
import com.vendo.notification_service.integration.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmailNotificationService {

    private final SimpleMailSender simpleMailSender;

    private final RedisService redisService;

    private final RedisProperties redisProperties;

    @Transactional
    public void sendRecoveryPasswordEmail(String email) {
        Optional<String> otp = redisService.getValue(redisProperties.getResetPassword().getPrefixes().getEmailPrefix() + email);
        if (otp.isEmpty()) {
            throw new RedisValueExpiredException("Otp has expired");
        }

        simpleMailSender.sendMail("Recovery password", email, "Use this OTP for password recovery: [%s]".formatted(otp.get()));
    }
}