package com.odeyalo.analog.auth.service.phone;

import com.odeyalo.analog.auth.entity.User;

public interface PhoneNumberBinderManager {

    void sendVerificationCode(User user, String newPhoneNumber);

    void changeUserPhoneIfCodeCorrect(String code, String newPhoneNumber);

    boolean confirmCode(String code);
}
