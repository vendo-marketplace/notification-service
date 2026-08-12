package com.vendo.notification_service.application.code;

import com.vendo.event_lib.code.EmailCodeEvent;
import com.vendo.notification_service.port.code.EmailCodeNotificationUseCase;
import com.vendo.notification_service.port.mail.MailProviderPort;
import com.vendo.notification_service.port.code.CodeTemplatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class EmailCodeNotificationService implements EmailCodeNotificationUseCase {

    private final MailProviderPort mailSender;

    private final CodeTemplatePort codeTemplatePort;

    @Override
    public void send(EmailCodeEvent event) {
        if (event.type() == null) {
            throw new IllegalArgumentException("Type is required.");
        }

        String codeTemplate = codeTemplatePort.getTemplate(event.type());
        String codeSubject = codeTemplatePort.getSubject(event.type());

        mailSender.sendMail(codeSubject, event.email(), codeTemplate.formatted(event.code()));
    }
}