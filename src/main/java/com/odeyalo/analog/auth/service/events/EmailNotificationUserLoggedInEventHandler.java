package com.odeyalo.analog.auth.service.events;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.sender.mail.GenericMailMessage;
import com.odeyalo.analog.auth.service.sender.mail.MailSender;
import com.odeyalo.analog.auth.service.support.HttpServletRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Send email notification when someone logged in user account
 */
@Component
public class EmailNotificationUserLoggedInEventHandler implements UserLoggedInEventHandler {
    private final Logger logger = LoggerFactory.getLogger(EmailNotificationUserLoggedInEventHandler.class);
    private final MailSender mailSender;

    public EmailNotificationUserLoggedInEventHandler(@Qualifier("kafkaBrokerMicroserviceDelegateMailSender") MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void handleEvent(Event event) {
        if (!(event.getEventType() == EventType.USER_LOGGED_IN)) {
            this.logger.error("Wrong event was received. Expected event: {}, event type that was received: {}", EventType.USER_LOGGED_IN, event.getEventType());
            return;
        }
        if (!(event instanceof UserLoggedInEvent)) {
            this.logger.error("Wrong event class was received. Excepted class: {}, received class: {}", UserLoggedInEvent.class.getName(), event.getClass().getName());
            return;
        }
        UserLoggedInEvent userLoggedInEvent = (UserLoggedInEvent) event;
        User user = userLoggedInEvent.getUser();
        LocalDateTime time = userLoggedInEvent.getTime();
        GenericMailMessage message = new GenericMailMessage("We detected a login to your account.",
                String.format("Hello, %s. We detected that someone logged in your account at %s, ip address is %s. Browser: %s, operating system: %s",
                        user.getNickname(), time, HttpServletRequestUtils.getIPAddress(), HttpServletRequestUtils.getBrowserName(), HttpServletRequestUtils.getOperatingSystemName()),
                user.getEmail());
        this.mailSender.send(message);
        this.logger.info("Successful delivered message to user: {}, with message: {}", user.getNickname(), message);
    }

    @Override
    public EventType getEventType() {
        return EventType.USER_LOGGED_IN;
    }
}
