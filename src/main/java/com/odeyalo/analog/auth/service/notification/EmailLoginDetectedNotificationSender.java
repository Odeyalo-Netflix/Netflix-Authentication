package com.odeyalo.analog.auth.service.notification;

import com.odeyalo.analog.auth.entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.odeyalo.analog.auth.service.sender.mail.MailSender;

@Service
public class EmailLoginDetectedNotificationSender implements LoginDetectedNotificationSender {
    private final MailSender mailSender;

    public EmailLoginDetectedNotificationSender(@Qualifier("kafkaBrokerMicroserviceDelegateMailSender") MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void notifyUser(User user) {
    }
}
