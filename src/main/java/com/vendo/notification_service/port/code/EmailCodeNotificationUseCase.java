package com.vendo.notification_service.port.code;

import com.vendo.event_lib.code.EmailCodeEvent;

public interface EmailCodeNotificationUseCase {

    void send(EmailCodeEvent event);

}
