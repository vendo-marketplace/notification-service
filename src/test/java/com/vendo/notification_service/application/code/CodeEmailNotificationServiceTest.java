package com.vendo.notification_service.application.code;

import com.vendo.event_lib.auto_search.AutoSearchEmailEvent;
import com.vendo.event_lib.code.CodeEmailEvent;
import com.vendo.event_lib.code.CodeEventType;
import com.vendo.notification_service.domain.code.dto.EmailCodeEventDataBuilder;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CodeEmailNotificationServiceTest {

    @Mock
    private MailProviderPort mailProviderPort;

    @Mock
    private MailProperties mailProperties;

    @InjectMocks
    private CodeEmailNotificationService codeEmailNotificationService;

    private static final String EMAIL_VERIFICATION_SUBJECT = "Email Verification";
    private static final String EMAIL_VERIFICATION_TEMPLATE = "Use this code to verify your email: %s";
    private static final String PASSWORD_RECOVERY_SUBJECT = "Password Recovery";
    private static final String PASSWORD_RECOVERY_TEMPLATE = "Use this link for password recovery: http://localhost:8030/reset-password?code=%s";

    private final MailProperties.CodeTemplate codeTemplate = new MailProperties.CodeTemplate(
            Map.of(CodeEventType.EMAIL_VERIFICATION, EMAIL_VERIFICATION_SUBJECT, CodeEventType.PASSWORD_RECOVERY, PASSWORD_RECOVERY_SUBJECT),
            Map.of(CodeEventType.EMAIL_VERIFICATION, EMAIL_VERIFICATION_TEMPLATE, CodeEventType.PASSWORD_RECOVERY, PASSWORD_RECOVERY_TEMPLATE)
    );

    private void initCodeTemplate() {
        when(mailProperties.getCode()).thenReturn(codeTemplate);
    }

    @Test
    void sendCodeEmailEvent_shouldSendEmailNotification_whenEmailVerificationEvent() {
        CodeEmailEvent event = EmailCodeEventDataBuilder.withRequiredFields()
                .type(CodeEventType.EMAIL_VERIFICATION)
                .build();
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        initCodeTemplate();

        codeEmailNotificationService.send(event);

        verify(mailProviderPort).sendMail(
                subjectCaptor.capture(),
                eq(event.email()),
                contentCaptor.capture()
        );

        String subject = subjectCaptor.getValue();
        String content = contentCaptor.getValue();

        assertThat(subject).isEqualTo(EMAIL_VERIFICATION_SUBJECT);
        assertThat(content).isEqualTo(EMAIL_VERIFICATION_TEMPLATE.formatted(event.code()));
    }

    @Test
    void sendCodeEmailEvent_shouldSendEmailNotification_whenPasswordRecoveryEvent() {
        CodeEmailEvent event = EmailCodeEventDataBuilder.withRequiredFields()
                .type(CodeEventType.PASSWORD_RECOVERY)
                .build();
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> contentCaptor = ArgumentCaptor.forClass(String.class);

        initCodeTemplate();

        codeEmailNotificationService.send(event);

        verify(mailProviderPort).sendMail(
                subjectCaptor.capture(),
                eq(event.email()),
                contentCaptor.capture()
        );

        String subject = subjectCaptor.getValue();
        String content = contentCaptor.getValue();

        assertThat(subject).isEqualTo(PASSWORD_RECOVERY_SUBJECT);
        assertThat(content).isEqualTo(PASSWORD_RECOVERY_TEMPLATE.formatted(event.code()));
    }

    @ParameterizedTest
    @MethodSource("invalidEvents")
    void sendCodeEmailEvent_shouldNotSentNotification_whenInvalidEvent(CodeEmailEvent event) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> codeEmailNotificationService.send(event)
        );

        assertEquals("Invalid code email event.", exception.getMessage());

        verifyNoInteractions(mailProviderPort);
    }

    private static Stream<CodeEmailEvent> invalidEvents() {
        return Stream.of(
                null,
                new CodeEmailEvent("code", null, CodeEventType.EMAIL_VERIFICATION),
                new CodeEmailEvent(null, "email", CodeEventType.EMAIL_VERIFICATION),
                new CodeEmailEvent("code", "email", null)
        );
    }
}
