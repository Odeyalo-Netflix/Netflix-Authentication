package com.odeyalo.analog.auth.unit.service.validators;

import com.odeyalo.analog.auth.service.validators.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class DefaultRequestUserDTOValidatorUnitTest {
    Validator emailValidator = new UserEmailValidator();
    Validator passwordValidator = new UserPasswordValidator();
    Validator nicknameValidator = new UserNicknameValidator();
    DefaultRequestUserDTOValidator validator = new DefaultRequestUserDTOValidator(nicknameValidator, emailValidator, passwordValidator);
    private static final String USER_EMAIL = "email@gmail.com";
    private static final String USER_NICKNAME = "nickname";
    private static final String USER_PASSWORD = "password";
    private static final String WRONG_EMAIL = "123";
    private static final String WRONG_PASSWORD = "1";
    private static final String WRONG_NICKNAME = "nick";

    @Test
    @DisplayName("Validate correct values")
    void validate() {
        ValidationResult result = validator.validate(USER_EMAIL, USER_NICKNAME, USER_PASSWORD);
        assertTrue(result.isSuccess());
        assertNull(result.getMessage());
    }
    @Test
    @DisplayName("Validate wrong values")
    void validateWrongData() {
        ValidationResult result = validator.validate(WRONG_EMAIL, WRONG_NICKNAME, WRONG_PASSWORD);
        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
    }

    @Test
    @DisplayName("Validate wrong email")
    void validateWrongEmail() {
        ValidationResult result = validator.validate(WRONG_EMAIL, USER_NICKNAME, USER_PASSWORD);
        String expectedMessage = "Wrong email";
        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertEquals(expectedMessage, result.getMessage());
    }
    @Test
    @DisplayName("Validate wrong email")
    void validateWrongNickname() {
        ValidationResult result = validator.validate(USER_EMAIL, WRONG_NICKNAME, USER_PASSWORD);
        String expectedMessage = "Nickname must be more than 6 and less than 12 chars";
        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertEquals(expectedMessage, result.getMessage());
    }

    @Test
    @DisplayName("Validate wrong email")
    void validateWrongPassword() {
        ValidationResult result = validator.validate(USER_EMAIL, USER_NICKNAME, WRONG_PASSWORD);
        String expectedMessage = "Password must be at least 8 chars and less than 50";
        assertFalse(result.isSuccess());
        assertNotNull(result.getMessage());
        assertEquals(expectedMessage, result.getMessage());
    }
}
