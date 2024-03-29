package com.odeyalo.analog.auth.service.events.login;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.Event;
import com.odeyalo.analog.auth.service.sender.mail.GenericMailMessage;
import com.odeyalo.analog.auth.service.sender.mail.MailSender;
import com.odeyalo.analog.auth.service.support.HttpServletRequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Send email notification when someone logged in user account
 */
@Component
public class EmailNotificationUserLoggedInEventHandler extends AbstractUserLoggedInEventHandler {
    private final MailSender mailSender;

    @Autowired
    public EmailNotificationUserLoggedInEventHandler(@Qualifier("kafkaBrokerMicroserviceDelegateMailSender") MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void handleEvent(Event event) {
        boolean isEventCorrect = checkIncomingEvent(event);
        if (!isEventCorrect) {
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
}
