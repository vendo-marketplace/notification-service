package com.vendo.notification_service.port;

import com.vendo.event_lib.auto_search.AutoSearchEventType;
import com.vendo.notification_service.domain.auto_search.AutoSearch;

public interface AutoSearchEventUseCase {

    void send(AutoSearchEventType type, AutoSearch autoSearch);

}
