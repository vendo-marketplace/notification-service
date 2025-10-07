package com.vendo.notification_service.integration.kafka.consumer;

import com.vendo.notification_service.integration.kafka.common.topics.InputTopics;
import com.vendo.notification_service.service.EmailNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumer {

    private final EmailNotificationService emailNotificationService;

    // TODO move to configurations
    @KafkaListener(
            topics = InputTopics.PASSWORD_RECOVERY_EMAIL_NOTIFICATION_TOPIC,
            groupId = "password_recovery_email_notification_group",
            properties = {"auto.offset.reset=latest"}
    )
    public void listenRecoveryPasswordNotificationEvent(String token) {
        log.info("[PASSWORD_RECOVERY_EMAIL_NOTIFICATION_EVENT_CONSUMER]: Received token for password recovery: {}", token);
        emailNotificationService.sendRecoveryPasswordEmail(token);
    }

}
