package com.odeyalo.analog.auth.service.events;


public abstract class Event {
    protected final String eventType;

    protected Event(String eventType) {
        this.eventType = eventType;
    }

    public String getEventType() {
        return eventType;
    }
}
