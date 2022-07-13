package com.odeyalo.analog.auth.service.events;

import org.springframework.beans.factory.annotation.Autowired;

public interface EventHandler {

    void handleEvent(Event event);

    EventType getEventType();

    @Autowired
    default void registerEventHandler(EventHandlerManager manager) {
        manager.subscribeToEvent(getEventType(), this);
    }
}
