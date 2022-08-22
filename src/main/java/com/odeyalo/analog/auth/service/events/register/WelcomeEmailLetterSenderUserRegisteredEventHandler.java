package com.odeyalo.analog.auth.service.events.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.Event;
import com.odeyalo.analog.auth.service.events.login.UserLoggedInEvent;
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
        if (!(event.getEventType().equals(USER_REGISTERED_EVENT_VALUE))) {
            this.logger.error("Wrong event was received. Expected event: {}, event type that was received: {}", USER_REGISTERED_EVENT_VALUE, event.getEventType());
            return;
        }
        if (!(event instanceof UserLoggedInEvent)) {
            this.logger.error("Wrong event class was received. Excepted class: {}, received class: {}", UserRegisteredEvent.class.getName(), event.getClass().getName());
            return;
        }
        User user = ((UserLoggedInEvent) event).getUser();
        this.mailSender.send("Hello, " + user.getNickname() + " We are so glad to see you on Netflix!", "Welcome to Netflix", user.getEmail());
    }
}
