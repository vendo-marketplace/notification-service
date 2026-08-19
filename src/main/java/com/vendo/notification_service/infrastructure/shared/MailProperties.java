package com.vendo.notification_service.infrastructure.shared;

import com.vendo.event_lib.auto_search.AutoSearchEventType;
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

    private CodeTemplate code;
    private AutoSearchTemplate autoSearch;

    public record CodeTemplate(
            Map<CodeEventType, String> subjects,
            Map<CodeEventType, String> templates
    ) {
    }

    public record AutoSearchTemplate(
            Map<AutoSearchEventType, String> subjects,
            Map<AutoSearchEventType, String> templates
    ) {
    }

}
