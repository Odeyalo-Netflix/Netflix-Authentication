package com.odeyalo.analog.auth.service.events.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.Event;

import java.time.LocalDateTime;

public class UserRegistrationConfirmedEvent extends Event {
    private final User user;
    private final LocalDateTime time;

    public UserRegistrationConfirmedEvent(User user, LocalDateTime time) {
        super("USER_REGISTRATION_CONFIRMED_EVENT");
        this.user = user;
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
