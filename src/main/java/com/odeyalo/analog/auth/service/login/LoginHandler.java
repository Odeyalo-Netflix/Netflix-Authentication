package com.odeyalo.analog.auth.service.login;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.LoginException;

public interface LoginHandler {

    User login(User user) throws LoginException;

}
