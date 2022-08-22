package com.odeyalo.analog.auth.service.events.register;

import com.odeyalo.analog.auth.service.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserRegisteredEventHandler implements EventHandler {
    public static final String USER_REGISTERED_EVENT_VALUE = "USER_REGISTERED";
    protected final Logger logger = LoggerFactory.getLogger(AbstractUserRegisteredEventHandler.class);

    @Override
    public String getEventType() {
        return USER_REGISTERED_EVENT_VALUE;
    }
}
