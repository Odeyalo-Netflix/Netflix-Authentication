package com.odeyalo.analog.auth.service.events.login;

import com.odeyalo.analog.auth.service.events.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUserLoggedInEventHandler implements EventHandler {
    public static final String USER_LOGGED_IN_EVENT_VALUE = "USER_LOGGED_IN";
    protected final Logger logger = LoggerFactory.getLogger(AbstractUserLoggedInEventHandler.class);

    @Override
    public String getEventType() {
        return USER_LOGGED_IN_EVENT_VALUE;
    }
}
