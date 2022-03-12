package com.odeyalo.analog.auth.service.validators;

import com.odeyalo.analog.auth.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.logging.FileHandler;

public interface RequestUserDTOValidator {
   void validate(String email, String nickname, String password) throws ValidationException;
}
