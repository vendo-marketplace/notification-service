package com.vendo.notification_service.service.otp.common.config;

import com.vendo.integration.kafka.event.EmailOtpEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "otp")
public class OtpMailProperties{

    private Map<EmailOtpEvent.OtpEventType, String> subjects;

    private Map<EmailOtpEvent.OtpEventType, String> templates;

}
