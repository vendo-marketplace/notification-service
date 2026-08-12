package com.vendo.notification_service.adapter.code.out.props;

import com.vendo.event_lib.code.CodeEventType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "code")
public class CodeMailProperties {

    private Map<CodeEventType, String> subjects;

    private Map<CodeEventType, String> templates;

}
