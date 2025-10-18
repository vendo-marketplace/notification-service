package com.vendo.notification_service.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class JavaMailSenderConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setProtocol(mailProperties.getProperties().getMail().getTransport().getProtocol());

        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", mailProperties.getProperties().getMail().getSmtp().getAuth());
        properties.put("mail.smtp.starttls.enable", mailProperties.getProperties().getMail().getSmtp().getStarttls().getEnable());
        properties.put("mail.smtp.starttls.required", true);
        mailSender.setJavaMailProperties(properties);

        return mailSender;
    }
}
