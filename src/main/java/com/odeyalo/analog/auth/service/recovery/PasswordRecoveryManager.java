package com.odeyalo.analog.auth.service.recovery;


import org.springframework.beans.factory.annotation.Autowired;

public interface PasswordRecoveryManager {

    void sendResetPasswordCode(String target);

    void changePassword(String code, String newPassword);

    boolean checkResetPasswordCode(String code);

    PasswordRecoveryType getPasswordRecoveryType();

    @Autowired
    default void registerMe(PasswordRecoveryManagerFactory factory) {
        factory.registerPasswordRecoveryManager(getPasswordRecoveryType(), this);
    }
}
