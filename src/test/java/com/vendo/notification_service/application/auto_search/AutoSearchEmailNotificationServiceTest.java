package com.vendo.notification_service.application.auto_search;

import com.vendo.event_lib.auto_search.AutoSearchEmailEvent;
import com.vendo.event_lib.auto_search.AutoSearchEventType;
import com.vendo.notification_service.domain.code.dto.AutoSearchEmailEventDataBuilder;
import com.vendo.notification_service.infrastructure.shared.MailProperties;
import com.vendo.notification_service.port.mail.MailProviderPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoSearchEmailNotificationServiceTest {

    @Mock
    private MailProviderPort mailProviderPort;

    @Mock
    private MailProperties mailProperties;

    @InjectMocks
    private AutoSearchEmailNotificationService autoSearchEmailNotificationService;

    private static final String AUTO_SEARCH_SUBJECT = "Found new products matching your search";
    private static final String AUTO_SEARCH_TEMPLATE = "Hello! We found new products that match your search %s.\n\n%d products found:\n%s\nYou're receiving this email because you subscribed to product search notifications for this query.\n";

    private final MailProperties.AutoSearchTemplate autoSearchTemplate = new MailProperties.AutoSearchTemplate(
            Map.of(AutoSearchEventType.AUTO_SEARCH_REQUEST_FOUND, AUTO_SEARCH_SUBJECT),
            Map.of(AutoSearchEventType.AUTO_SEARCH_REQUEST_FOUND, AUTO_SEARCH_TEMPLATE)
    );

    private void initAutoSearchTemplate() {
        when(mailProperties.getAutoSearch()).thenReturn(autoSearchTemplate);
    }

    @Test
    void sendAutoSearchEmailEvent_shouldSendAutoSearchEmailNotification() {
        AutoSearchEmailEvent event = AutoSearchEmailEventDataBuilder.withAllFields();
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        initAutoSearchTemplate();

        autoSearchEmailNotificationService.send(event);

        verify(mailProviderPort).sendMail(
                subjectCaptor.capture(),
                eq(event.email()),
                contentCaptor.capture()
        );

        String subject = subjectCaptor.getValue();
        String content = contentCaptor.getValue();

        assertThat(subject).isEqualTo(AUTO_SEARCH_SUBJECT);
        assertThat(content.contains(event.id())).isTrue();
        assertThat(content.contains(event.email())).isTrue();
        assertThat(content.contains(event.products().get(0).title())).isTrue();
        assertThat(content.contains(event.products().get(1).title())).isTrue();
        assertThat(content.contains(event.products().get(2).title())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidEvents")
    void sendAutoSearchEmailEvent_shouldNotSendNotification_whenEventIsInvalid(
            AutoSearchEmailEvent event
    ) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autoSearchEmailNotificationService.send(event)
        );

        assertEquals("Invalid auto search event.", exception.getMessage());

        verifyNoInteractions(mailProviderPort);
    }

    private static Stream<AutoSearchEmailEvent> invalidEvents() {
        return Stream.of(
                null,
                new AutoSearchEmailEvent("eventId", null, List.of(new AutoSearchEmailEvent.ResultProduct("productId1", "title", BigDecimal.TEN))),
                new AutoSearchEmailEvent("eventId", "requestId", List.of())
        );
    }
}
