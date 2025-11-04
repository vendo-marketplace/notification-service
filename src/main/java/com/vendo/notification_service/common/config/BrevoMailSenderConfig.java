package com.vendo.notification_service.common.config;

import brevo.ApiClient;
import brevoApi.TransactionalEmailsApi;
import brevoModel.SendSmtpEmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrevoMailSenderConfig {

    @Value("${brevo.api-key}")
    public String BREVO_API_KEY;

    @Value("${brevo.sender.name}")
    public String BREVO_SENDER_NAME;

    @Value("${brevo.sender.email}")
    public String BREVO_SENDER_EMAIL;

    @Bean
    public TransactionalEmailsApi transactionalEmailsApi() {
        TransactionalEmailsApi transactionalEmailsApi = new TransactionalEmailsApi();

        ApiClient apiClient = brevo.Configuration.getDefaultApiClient();
        apiClient.setApiKey(BREVO_API_KEY);

        return transactionalEmailsApi;
    }

    @Bean
    public SendSmtpEmailSender sendSmtpEmailSender() {
        return new SendSmtpEmailSender()
                .name(BREVO_SENDER_NAME)
                .email(BREVO_SENDER_EMAIL);
    }
}
