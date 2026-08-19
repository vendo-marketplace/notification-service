package com.vendo.notification_service.port;

import com.vendo.event_lib.auto_search.AutoSearchEmailEvent;

public interface AutoSearchNotificationUseCase {

    void send(AutoSearchEmailEvent event);

}
