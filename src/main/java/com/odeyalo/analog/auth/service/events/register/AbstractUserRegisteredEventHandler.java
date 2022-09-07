package com.odeyalo.analog.auth.service.events.register;

import com.odeyalo.analog.auth.service.events.Event;
import com.odeyalo.analog.auth.service.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserRegisteredEventHandler implements EventHandler {
    public static final String USER_REGISTERED_EVENT_VALUE = "USER_REGISTERED";
    protected final Logger logger = LoggerFactory.getLogger(AbstractUserRegisteredEventHandler.class);


    @Override
    public boolean checkIncomingEvent(Event event) {
        if (!(event.getEventType().equals(USER_REGISTERED_EVENT_VALUE))) {
            this.logger.error("Wrong event was received. Expected event: {}, event type that was received: {}", USER_REGISTERED_EVENT_VALUE, event.getEventType());
            return false;
        }
        if (!(event instanceof UserRegisteredEvent)) {
            this.logger.error("Wrong event class was received. Excepted class: {}, received class: {}", UserRegisteredEvent.class.getName(), event.getClass().getName());
            return false;
        }
        return true;
    }

    @Override
    public String getEventType() {
        return USER_REGISTERED_EVENT_VALUE;
    }
}
