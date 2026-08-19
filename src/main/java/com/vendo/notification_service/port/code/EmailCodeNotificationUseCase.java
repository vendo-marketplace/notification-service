package com.vendo.notification_service.port.code;

import com.vendo.event_lib.code.CodeEmailEvent;

public interface EmailCodeNotificationUseCase {

    void send(CodeEmailEvent event);

}
