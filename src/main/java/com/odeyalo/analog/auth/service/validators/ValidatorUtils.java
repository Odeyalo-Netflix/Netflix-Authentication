package com.odeyalo.analog.auth.service.validators;

public class ValidatorUtils {

    public static boolean lengthCheck(String value, Integer requiredLength) {
        return value.length() > requiredLength;
    }
    public static boolean lengthCheck(String value, Integer requiredLength, Integer maxLength) {
        return value.length() >= requiredLength && value.length() < maxLength;
    }
}
