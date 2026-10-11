package com.vendo.notification_service.port.code;

import com.vendo.event_lib.code.CodeEmailEvent;

public interface CodeEmailNotificationUseCase {

    void send(CodeEmailEvent event);

}
