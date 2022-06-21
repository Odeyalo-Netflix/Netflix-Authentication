package com.odeyalo.analog.auth.service.validators;

public class ValidationResult {
    private String message;
    private boolean isSuccess;

    public ValidationResult(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public ValidationResult(String message, boolean isSuccess) {
        this.message = message;
        this.isSuccess = isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
