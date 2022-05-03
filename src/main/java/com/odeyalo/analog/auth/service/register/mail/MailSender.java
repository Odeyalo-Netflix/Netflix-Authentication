package com.odeyalo.analog.auth.service.register.mail;

import com.odeyalo.analog.auth.dto.MailMessageDTO;

public interface MailSender {

    void send(String body, String subject, String to);

    void send(MailMessageDTO dto);
}
