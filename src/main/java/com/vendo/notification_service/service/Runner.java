package com.vendo.notification_service.service;

import com.vendo.notification_service.common.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final MailSender mailSender;

    @Override
    public void run(String... args) throws Exception {
        mailSender.sendMail("Test 1", "melnuk2004y@gmail.com", "Hello world from Java");
    }
}
