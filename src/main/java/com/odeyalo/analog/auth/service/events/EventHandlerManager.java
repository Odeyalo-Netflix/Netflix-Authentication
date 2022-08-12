package com.odeyalo.analog.auth.service.events;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventHandlerManager {
    private final Map<EventType, List<EventHandler>> events = new HashMap<>();

    public void subscribeToEvent(EventType eventType, EventHandler eventHandler) {
        List<EventHandler> eventHandlers = events.get(eventType);
        if(eventHandlers == null) {
            eventHandlers = new ArrayList<>();
            this.events.put(eventType, eventHandlers);
        }
        eventHandlers.add(eventHandler);
    }

    public void unsubscribeFromEvent(EventType eventType) {
        this.events.remove(eventType);
    }

    @Async
    public void notifySpecialEventHandlers(EventType eventType, Event event) {
        List<EventHandler> eventHandlers = this.events.get(eventType);
        for (EventHandler eventHandler : eventHandlers) {
            eventHandler.handleEvent(event);
        }
    }
}
