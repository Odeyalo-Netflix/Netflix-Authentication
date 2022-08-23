package com.odeyalo.analog.auth.service.events;

import org.springframework.beans.factory.annotation.Autowired;

public interface EventHandler {

    void handleEvent(Event event);

    String getEventType();

    /**
     * Checks event
     * @param event - event to check
     * @return - true if event is correct, otherwise - false
     */
    boolean checkIncomingEvent(Event event);

    @Autowired
    default void registerEventHandler(EventHandlerManager manager) {
        manager.subscribeToEvent(getEventType(), this);
    }
}
