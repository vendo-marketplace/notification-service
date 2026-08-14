package com.vendo.notification_service.application.code;

import com.vendo.event_lib.code.EmailCodeEvent;
import com.vendo.notification_service.infrastructure.shared.MailProperties;
import com.vendo.notification_service.port.code.EmailCodeNotificationUseCase;
import com.vendo.notification_service.port.mail.MailProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EmailCodeNotificationService implements EmailCodeNotificationUseCase {

    private final MailProviderPort mailSender;

    private final MailProperties mailProperties;

    @Override
    public void send(EmailCodeEvent event) {
        if (event.type() == null) {
            throw new IllegalArgumentException("Type is required.");
        }

        String codeTemplate = mailProperties.getCode().templates().get(event.type());
        String codeSubject = mailProperties.getCode().subjects().get(event.type());

        mailSender.sendMail(codeSubject, event.email(), codeTemplate.formatted(event.code()));
    }
}