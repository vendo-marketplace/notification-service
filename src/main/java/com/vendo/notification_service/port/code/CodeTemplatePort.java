package com.vendo.notification_service.port.code;

import com.vendo.event_lib.code.CodeEventType;

public interface CodeTemplatePort {
    String getSubject(CodeEventType type);
    String getTemplate(CodeEventType type);
}
