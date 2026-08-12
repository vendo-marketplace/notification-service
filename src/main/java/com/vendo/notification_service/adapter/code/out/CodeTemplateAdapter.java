package com.vendo.notification_service.adapter.code.out;

import com.vendo.event_lib.code.CodeEventType;
import com.vendo.notification_service.adapter.code.out.props.CodeMailProperties;
import com.vendo.notification_service.port.code.CodeTemplatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodeTemplateAdapter implements CodeTemplatePort {

    private final CodeMailProperties codeMailProperties;

    @Override
    public String getSubject(CodeEventType type) {
        return codeMailProperties.getSubjects().get(type);
    }

    @Override
    public String getTemplate(CodeEventType type) {
        return codeMailProperties.getTemplates().get(type);
    }
}
