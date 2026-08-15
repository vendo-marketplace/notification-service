package com.vendo.notification_service.application.code;

import com.vendo.core_lib.utils.ObjectUtils;
import com.vendo.core_lib.utils.StringUtils;
import com.vendo.event_lib.code.CodeEmailEvent;
import com.vendo.notification_service.infrastructure.shared.MailProperties;
import com.vendo.notification_service.port.code.EmailCodeNotificationUseCase;
import com.vendo.notification_service.port.mail.MailProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CodeEmailNotificationService implements EmailCodeNotificationUseCase {

    private final MailProviderPort mailSender;
    private final MailProperties mailProperties;

    @Override
    public void send(CodeEmailEvent event) {
        validateEvent(event);
        MailProperties.CodeTemplate code = mailProperties.getCode();

        String codeSubject = code.subjects().get(event.type());
        String codeTemplate = code.templates().get(event.type());

        mailSender.sendMail(codeSubject, event.email(), codeTemplate.formatted(event.code()));
    }

    private void validateEvent(CodeEmailEvent event) {
        if (!ObjectUtils.isAllNotNull(event, event.type()) || !StringUtils.isNotEmptyAll(event.email(), event.code())) {
            throw new IllegalArgumentException("Invalid code email event.");
        }
    }
}