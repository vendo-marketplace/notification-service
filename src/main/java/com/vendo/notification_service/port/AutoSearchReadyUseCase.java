package com.vendo.notification_service.port;

import com.vendo.event_lib.auto_search.AutoSearchReadyEvent;

public interface AutoSearchReadyUseCase {

    void send(AutoSearchReadyEvent event);

}
