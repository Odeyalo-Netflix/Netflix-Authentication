package com.odeyalo.analog.auth.service.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Async implementation of EventHandlerManager. Notify all subscribers using async mode
 * @see com.odeyalo.analog.auth.service.events.EventHandlerManager
 */
@Component
public class AsyncEventHandlerManager implements EventHandlerManager {
    private final Map<String, List<EventHandler>> events = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(AsyncEventHandlerManager.class);

    @Override
    public void subscribeToEvent(String eventType, EventHandler eventHandler) {
        List<EventHandler> eventHandlers = events.get(eventType);
        if (eventHandlers == null) {
            eventHandlers = new ArrayList<>();
            this.events.put(eventType, eventHandlers);
        }
        eventHandlers.add(eventHandler);
        this.logger.info("Added to event handlers: {} with event type: {}", eventHandler, eventType);
    }

    @Override
    public void unsubscribeAllFromEvent(String eventType) {
        this.events.remove(eventType);
    }

    @Override
    public void unsubscribeFromEvent(String eventType, EventHandler eventHandler) {
        List<EventHandler> eventHandlers = this.events.get(eventType);
        if (eventHandlers == null) {
            this.logger.info("Event handler list is empty. Cannot to unsubscribe from event type: {}", eventType);
            return;
        }
        eventHandlers.remove(eventHandler);
    }

    @Async
    @Override
    public void notifySpecialEventHandlers(String eventType, Event event) {
        List<EventHandler> eventHandlers = this.events.get(eventType);
        for (EventHandler eventHandler : eventHandlers) {
            eventHandler.handleEvent(event);
        }
    }
}
