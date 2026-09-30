package com.vendo.notification_service.application.auto_search;

import com.vendo.event_lib.auto_search.AutoSearchEventType;
import com.vendo.event_lib.auto_search.AutoSearchReadyEvent;
import com.vendo.notification_service.domain.code.dto.AutoSearchReadyEventDataBuilder;
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

import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AutoSearchReadyServiceTest {

    @Mock
    private MailProviderPort mailProviderPort;

    @Mock
    private MailProperties mailProperties;

    @InjectMocks
    private AutoSearchReadyService autoSearchReadyService;

    private static final String AUTO_SEARCH_SUBJECT = "Found new products matching your search";
    private static final String AUTO_SEARCH_TEMPLATE = "Hello! We found new products that match your search. Go to: ${client.prod.url}/auto-search/%s/products to view the list.";

    private final MailProperties.AutoSearchTemplate autoSearchTemplate = new MailProperties.AutoSearchTemplate(
            Map.of(AutoSearchEventType.AUTO_SEARCH_REQUEST_FOUND, AUTO_SEARCH_SUBJECT),
            Map.of(AutoSearchEventType.AUTO_SEARCH_REQUEST_FOUND, AUTO_SEARCH_TEMPLATE)
    );

    private void initAutoSearchTemplate() {
        when(mailProperties.getAutoSearch()).thenReturn(autoSearchTemplate);
    }

    @Test
    void sendAutoSearchEmailEvent_shouldSendAutoSearchEmailNotification() {
        AutoSearchReadyEvent event = AutoSearchReadyEventDataBuilder.withAllFields();
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        initAutoSearchTemplate();

        autoSearchReadyService.send(event);

        verify(mailProviderPort).sendMail(
                subjectCaptor.capture(),
                eq(event.email()),
                contentCaptor.capture()
        );

        String subject = subjectCaptor.getValue();
        String content = contentCaptor.getValue();

        assertThat(subject).isEqualTo(AUTO_SEARCH_SUBJECT);
        assertThat(content.contains(event.id())).isTrue();
    }

    @ParameterizedTest
    @MethodSource("invalidEvents")
    void sendAutoSearchEmailEvent_shouldNotSendNotification_whenEventIsInvalid(
            AutoSearchReadyEvent event
    ) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> autoSearchReadyService.send(event)
        );

        assertEquals("Invalid auto search event.", exception.getMessage());

        verifyNoInteractions(mailProviderPort);
    }

    private static Stream<AutoSearchReadyEvent> invalidEvents() {
        return Stream.of(
                null,
                new AutoSearchReadyEvent("eventId", null)
        );
    }
}
