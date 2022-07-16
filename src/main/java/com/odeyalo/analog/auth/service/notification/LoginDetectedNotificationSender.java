package com.odeyalo.analog.auth.service.notification;

import com.odeyalo.analog.auth.entity.User;

public interface LoginDetectedNotificationSender {

    void notifyUser(User user);

}
