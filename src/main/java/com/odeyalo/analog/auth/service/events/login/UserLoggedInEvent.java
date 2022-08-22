package com.odeyalo.analog.auth.service.events.login;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.service.events.Event;

import java.time.LocalDateTime;

public class UserLoggedInEvent extends Event {
    private LocalDateTime time; // When user logged in
    private User user;

    public UserLoggedInEvent(User user, LocalDateTime time) {
        super("USER_LOGGED_IN");
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
}
