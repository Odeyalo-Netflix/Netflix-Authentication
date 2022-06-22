package com.odeyalo.analog.auth.service.validators;

import org.springframework.stereotype.Component;

@Component
public class UserPasswordValidator implements Validator {

    @Override
    public boolean validate(String password) {
        return ValidatorUtils.isLengthBetween(password, 8, 50);
    }
}
