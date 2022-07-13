package com.odeyalo.analog.auth.service.events;

import com.odeyalo.analog.auth.entity.User;

import java.time.LocalDateTime;

public class UserLoggedInEvent implements Event {
    private User user;
    private LocalDateTime time; // When user logged in

    public UserLoggedInEvent(User user, LocalDateTime time) {
        this.user = user;
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public EventType getEventType() {
        return EventType.USER_LOGGED_IN;
    }
}
