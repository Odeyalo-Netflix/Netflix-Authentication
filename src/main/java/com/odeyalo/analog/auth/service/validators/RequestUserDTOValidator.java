package com.odeyalo.analog.auth.service.validators;

public interface RequestUserDTOValidator {
    ValidationResult validate(String email, String nickname, String password);
}
