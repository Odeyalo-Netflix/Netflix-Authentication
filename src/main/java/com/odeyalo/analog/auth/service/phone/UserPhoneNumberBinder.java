package com.odeyalo.analog.auth.service.phone;

import com.odeyalo.analog.auth.entity.User;
import com.odeyalo.analog.auth.exceptions.PhoneNumberBindException;

public interface UserPhoneNumberBinder {
    /**
     * Update or save user phone to db
     * @param user - user who want to change phone number
     * @param phoneNumber - new phone number
     */
    void bindUserPhone(User user, String phoneNumber) throws PhoneNumberBindException;

}
