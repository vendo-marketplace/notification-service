package com.vendo.notification_service.application.auto_search;

import com.vendo.core_lib.utils.CollectionUtils;
import com.vendo.core_lib.utils.ObjectUtils;
import com.vendo.core_lib.utils.StringUtils;
import com.vendo.event_lib.auto_search.AutoSearchEmailEvent;
import com.vendo.event_lib.auto_search.AutoSearchEventType;
import com.vendo.notification_service.infrastructure.shared.MailProperties;
import com.vendo.notification_service.port.AutoSearchNotificationUseCase;
import com.vendo.notification_service.port.mail.MailProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AutoSearchEmailNotificationService implements AutoSearchNotificationUseCase {

    private final MailProviderPort mailSender;
    private final MailProperties mailProperties;

    private static final String PRODUCT_TEMPLATE = "\t%s - %s грн \n";

    @Override
    public void send(AutoSearchEmailEvent event) {
        validateEvent(event);
        MailProperties.AutoSearchTemplate autoSearch = mailProperties.getAutoSearch();

        String subject = autoSearch.subjects().get(AutoSearchEventType.AUTO_SEARCH_REQUEST_FOUND);
        String template = autoSearch.templates().get(AutoSearchEventType.AUTO_SEARCH_REQUEST_FOUND);

        mailSender.sendMail(subject, event.email(), buildContent(template, event));
    }

    private void validateEvent(AutoSearchEmailEvent event) {
        if (ObjectUtils.isNull(event)
                || CollectionUtils.isEmpty(event.products())
                || StringUtils.isEmpty(event.email())
        ) {
            throw new IllegalArgumentException("Invalid auto search event.");
        }
    }

    private String buildContent(String template, AutoSearchEmailEvent event) {
        return template.formatted(event.id(), event.products().size(), buildProductsContent(event));
    }

    private String buildProductsContent(AutoSearchEmailEvent event) {
        StringBuilder sb = new StringBuilder();

        for (AutoSearchEmailEvent.ResultProduct product : event.products()) {
            sb.append(PRODUCT_TEMPLATE.formatted(product.title(), product.price()));
        }

        return sb.toString();
    }
}
