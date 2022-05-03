package com.odeyalo.analog.auth.unit.service.validators;

import com.odeyalo.analog.auth.service.validators.UserEmailValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEmailValidatorTest {
    private static final String CORRECT_EMAIL = "username@domain.com";
    private static final String WRONG_EMAIL = "wrong12341";
    private final UserEmailValidator validator = new UserEmailValidator();

    @Test
    void validateCorrectEmail() {
        boolean validate = this.validator.validate(CORRECT_EMAIL);
        assertTrue(validate);
    }
    @Test
    void validateWrongEmail() {
        boolean validate = this.validator.validate(WRONG_EMAIL);
        assertFalse(validate);
    }
}