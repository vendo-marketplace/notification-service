package com.vendo.notification_service.common;

public interface MailSender {

    void sendMail(String subject, String to, String text);

}
