package com.odeyalo.analog.auth.service.events.register;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.Event;

public class UserRegisteredEvent extends Event {
    private final User user;

    public UserRegisteredEvent(User user) {
        super("USER_REGISTERED");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
