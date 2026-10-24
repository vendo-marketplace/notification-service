package com.vendo.notification_service.application.auto_search;

import com.vendo.core_lib.utils.ObjectUtils;
import com.vendo.core_lib.utils.StringUtils;
import com.vendo.event_lib.auto_search.AutoSearchEventType;
import com.vendo.notification_service.domain.auto_search.AutoSearch;
import com.vendo.notification_service.infrastructure.shared.MailProperties;
import com.vendo.notification_service.port.AutoSearchEventUseCase;
import com.vendo.notification_service.port.mail.MailProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutoSearchEventService implements AutoSearchEventUseCase {

    private final MailProviderPort mailSender;
    private final MailProperties mailProperties;

    @Override
    public void send(AutoSearchEventType type, AutoSearch autoSearch) {
        validateEvent(autoSearch);
        MailProperties.AutoSearchTemplate aSTemplate = mailProperties.getAutoSearch();

        String subject = aSTemplate.subjects().get(type);
        String template = aSTemplate.templates().get(type);

        mailSender.sendMail(subject, autoSearch.email(), template.formatted(autoSearch.id()));
    }

    private void validateEvent(AutoSearch autoSearch) {
        if (ObjectUtils.isNull(autoSearch) || !StringUtils.isNotEmptyAll(autoSearch.id(), autoSearch.email())) {
            throw new IllegalArgumentException("Invalid auto search event.");
        }
    }
}
