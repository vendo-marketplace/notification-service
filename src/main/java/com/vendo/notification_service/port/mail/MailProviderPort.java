package com.vendo.notification_service.port.mail;

public interface MailProviderPort {

    void sendMail(String subject, String to, String text);

}
