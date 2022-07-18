package com.odeyalo.analog.auth.service.sender.mail;

public interface MailSender {

    void send(String body, String subject, String to);

    void send(GenericMailMessage message);
}
