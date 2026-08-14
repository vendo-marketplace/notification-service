package com.vendo.notification_service.infrastructure.shared;

import com.vendo.event_lib.code.CodeEventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "notification")
public class MailProperties {

    private MailTemplate code;
    private MailTemplate autoSearch;

    public record MailTemplate(
            Map<CodeEventType, String> subjects,
            Map<CodeEventType, String> templates
    ) {
    }

}
