package com.vendo.notification_service.adapter.auto_search.in.mapper;

import com.vendo.event_lib.auto_search.AutoSearchNewProductEvent;
import com.vendo.event_lib.auto_search.AutoSearchReadyEvent;
import com.vendo.notification_service.domain.auto_search.AutoSearch;
import com.vendo.notification_service.infrastructure.config.mapper.MapStructConfig;
import org.mapstruct.Mapper;

@Mapper(config = MapStructConfig.class)
public interface AutoSearchEventMapper {

    AutoSearch toAutoSearch(AutoSearchNewProductEvent event);
    AutoSearch toAutoSearch(AutoSearchReadyEvent event);

}
