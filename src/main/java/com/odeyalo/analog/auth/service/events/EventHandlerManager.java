package com.odeyalo.analog.auth.service.events;

public interface EventHandlerManager {
    /**
     * Subscribe event handler to specific event type
     * @param eventType - event type
     * @param eventHandler - event handler
     */
    void subscribeToEvent(String eventType, EventHandler eventHandler);

    /**
     * Unsubscribe all event handlers by event type
     * @param eventType - unique event type
     */
    void unsubscribeAllFromEvent(String eventType);

    /**
     * Unsubscribe specific event handler from event type
     * @param eventType - event type
     * @param eventHandler - event handler to unsub
     */
    void unsubscribeFromEvent(String eventType, EventHandler eventHandler);

    /**
     * Notify all events that subscribed to specific event type
     * @param eventType - unique event type
     * @param event - event that need to event handler
     */
    void notifySpecialEventHandlers(String eventType, Event event);
}
