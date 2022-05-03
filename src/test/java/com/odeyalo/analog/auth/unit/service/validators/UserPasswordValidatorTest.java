package com.odeyalo.analog.auth.unit.service.validators;

import com.odeyalo.analog.auth.service.validators.UserPasswordValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserPasswordValidatorTest {
    private static final String CORRECT_PASSWORD = "password";
    private static final String LESS_THAN_8_CHARACTERS_PASSWORD = "123";
    private static final String MORE_THAN_50_CHARACTERS_PASSWORD = "THIS_PASSWORD_IS_LARGE_THAN_50_CHARACTERS_AND_THIS_PASSWORD_IS_INCORRECT";

    private final UserPasswordValidator validator = new UserPasswordValidator();

    @Test
    @DisplayName("Correct password")
    void validateCorrectPassword() {
        boolean validate = this.validator.validate(CORRECT_PASSWORD);
        assertTrue(validate);
    }
    @Test
    @DisplayName("Less than required length password")
    void validateWrongPasswordThatLessThan8Chars() {
        boolean result = this.validator.validate(LESS_THAN_8_CHARACTERS_PASSWORD);
        assertFalse(result);
    }
    @Test
    @DisplayName("More than required length password")
    void validateWrongPasswordThatMoreThan50Chars() {
        boolean result = this.validator.validate(MORE_THAN_50_CHARACTERS_PASSWORD);
        assertFalse(result);
    }
}
