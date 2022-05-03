package com.odeyalo.analog.auth.service.validators;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;

@Component
public class UserEmailValidator implements Validator {

    @Override
    public boolean validate(String email) {
        return EmailValidator.getInstance().isValid(email);
    }
}
