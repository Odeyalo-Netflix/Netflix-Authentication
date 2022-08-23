package com.odeyalo.analog.auth.service.events.login;

import com.odeyalo.analog.auth.service.events.Event;
import com.odeyalo.analog.auth.service.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserLoggedInEventHandler implements EventHandler {
    public static final String USER_LOGGED_IN_EVENT_VALUE = "USER_LOGGED_IN";
    protected final Logger logger = LoggerFactory.getLogger(AbstractUserLoggedInEventHandler.class);

    @Override
    public boolean checkIncomingEvent(Event event) {
        if (!(event.getEventType().equals(USER_LOGGED_IN_EVENT_VALUE))) {
            this.logger.error("Wrong event was received. Expected event: {}, event type that was received: {}", USER_LOGGED_IN_EVENT_VALUE, event.getEventType());
            return false;
        }
        if (!(event instanceof UserLoggedInEvent)) {
            this.logger.error("Wrong event class was received. Excepted class: {}, received class: {}", UserLoggedInEvent.class.getName(), event.getClass().getName());
            return false;
        }
        return true;
    }

    @Override
    public String getEventType() {
        return USER_LOGGED_IN_EVENT_VALUE;
    }
}
