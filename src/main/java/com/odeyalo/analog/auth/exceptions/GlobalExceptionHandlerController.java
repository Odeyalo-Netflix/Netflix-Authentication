package com.odeyalo.analog.auth.exceptions;

import com.odeyalo.analog.auth.dto.response.ExceptionOccurredDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;


@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<?> emailExistException(EmailExistException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NicknameExistException.class)
    public ResponseEntity<?> nicknameExistException(NicknameExistException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserExistException.class)
    public ResponseEntity<?> userExistException(UserExistException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotExistException.class)
    public ResponseEntity<?> userNotExistException(UserNotExistException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<?> loginException(LoginException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<?> refreshTokenException(RefreshTokenException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> validationException(ValidationException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CodeVerificationException.class)
    public ResponseEntity<?> codeVerificationException(CodeVerificationException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeLoginException.class)
    public ResponseEntity<?> qrCodeLoginException(QrCodeLoginException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<?> qrCodeNotFoundException(QrCodeNotFoundException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> ioException(IOException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(FileNotPresentException.class)
    public ResponseEntity<?> ioException(FileNotPresentException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IncorrectResetPasswordCodeException.class)
    public ResponseEntity<?> IncorrectResetPasswordCodeException(IncorrectResetPasswordCodeException exception) {
        return this.extractedExceptionHandler(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> extractedExceptionHandler(String message, HttpStatus status) {
        return new ResponseEntity<>(new ExceptionOccurredDTO(message, status.toString()), status);
    }
}
