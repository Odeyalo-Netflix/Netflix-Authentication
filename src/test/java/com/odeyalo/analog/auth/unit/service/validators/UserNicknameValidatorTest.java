package com.odeyalo.analog.auth.unit.service.validators;

import com.odeyalo.analog.auth.service.validators.UserNicknameValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserNicknameValidatorTest {
    private final UserNicknameValidator validator = new UserNicknameValidator();
    private static final String CORRECT_NICKNAME = "CorrectNick";
    private static final String LESS_THAN_6_CHARS_NICKNAME = "aboba";
    private static final String MORE_THAN_12_CHARS_NICKNAME = "MORE_THAN_12_CHARACTERS";

    @Test
    @DisplayName("Correct nickname")
    void validateCorrectNickname() {
        boolean result = this.validator.validate(CORRECT_NICKNAME);
        assertTrue(result);
    }

    @Test
    @DisplayName("Wrong nickname that less than 6 chars")
    void validateWrongNicknameThatLessThan6Chars() {
        boolean result = this.validator.validate(LESS_THAN_6_CHARS_NICKNAME);
        assertFalse(result);
    }

    @Test
    @DisplayName("Wrong nickname that more than 12 chars")
    void validateWrongNicknameThatMoreThan12Chars() {
        boolean result = this.validator.validate(MORE_THAN_12_CHARS_NICKNAME);
        assertFalse(result);
    }
}