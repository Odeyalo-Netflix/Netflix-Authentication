package com.odeyalo.analog.auth.service.events.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.Event;
import com.odeyalo.analog.auth.service.sender.mail.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WelcomeEmailLetterSenderUserRegisteredEventHandler extends AbstractUserRegisteredEventHandler {
    private final MailSender mailSender;

    @Autowired
    public WelcomeEmailLetterSenderUserRegisteredEventHandler(@Qualifier("kafkaBrokerMicroserviceDelegateMailSender") MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void handleEvent(Event event) {
        boolean isEventCorrect = checkIncomingEvent(event);
        if (!isEventCorrect) {
            return;
        }
        User user = ((UserRegisteredEvent) event).getUser();
        this.mailSender.send("Hello, " + user.getNickname() + " We are so glad to see you on Netflix!", "Welcome to Netflix", user.getEmail());
    }
}
