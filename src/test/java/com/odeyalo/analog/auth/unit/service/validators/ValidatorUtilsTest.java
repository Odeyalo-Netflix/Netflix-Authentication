package com.odeyalo.analog.auth.unit.service.validators;

import com.odeyalo.analog.auth.service.validators.ValidatorUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorUtilsTest {
    public static final String TEST_STRING_VALUE = "HELLO";
    public static final Integer REQUIRED_LENGTH = 5;
    public static final Integer WRONG_REQUIRED_LENGTH = 10;
    public static final Integer MIN_LENGTH = 3;
    public static final Integer MAX_LENGTH = 7;
    public static final Integer WRONG_MIN_LENGTH = 10;
    public static final Integer WRONG_MAX_LENGTH = 20;


    @Test
    @DisplayName("Is length equal. Expect true")
    void isLengthEqual() {
        boolean result = ValidatorUtils.isLengthEqual(TEST_STRING_VALUE, REQUIRED_LENGTH);
        assertTrue(result);
    }
    @Test
    @DisplayName("Is length not equal. Expect false")
    void isLengthNotEqual() {
        boolean result = ValidatorUtils.isLengthEqual(TEST_STRING_VALUE, WRONG_REQUIRED_LENGTH);
        assertFalse(result);
    }

    @Test
    @DisplayName("Is length between 3 and 7, expect true")
    void isLengthBetweenCorrectLength() {
        boolean result = ValidatorUtils.isLengthBetween(TEST_STRING_VALUE, MIN_LENGTH, MAX_LENGTH);
        assertTrue(result);
    }
    @Test
    @DisplayName("Is length not between 10 and 20. expect false")
    void isLengthNotBetween() {
        boolean result = ValidatorUtils.isLengthBetween(TEST_STRING_VALUE, WRONG_MIN_LENGTH, WRONG_MAX_LENGTH);
        assertFalse(result);
    }

}
