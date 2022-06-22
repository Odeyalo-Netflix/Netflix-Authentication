package com.odeyalo.analog.auth.service.validators;

import org.springframework.stereotype.Component;

@Component
public class UserNicknameValidator implements Validator {

    @Override
    public boolean validate(String nickname) {
        return ValidatorUtils.isLengthBetween(nickname, 6, 20);
    }
}
