package com.odeyalo.analog.auth.service.support;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.UserNotExistException;

/**
 * Encode and update password for user
 */
public interface PasswordRecoverySaverSupport {

    void updatePassword(User user, String rawPassword) throws UserNotExistException;

    void updatePassword(String email, String rawPassword) throws UserNotExistException;

}
