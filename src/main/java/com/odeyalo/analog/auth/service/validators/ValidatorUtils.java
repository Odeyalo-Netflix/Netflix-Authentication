package com.odeyalo.analog.auth.service.validators;

public class ValidatorUtils {

    public static boolean isLengthEqual(String value, Integer requiredLength) {
        return value.length() == requiredLength;
    }


    public static boolean isLengthBetween(String value, Integer minLength, Integer maxLength) {
        return value.length() >= minLength && value.length() < maxLength;
    }
}
