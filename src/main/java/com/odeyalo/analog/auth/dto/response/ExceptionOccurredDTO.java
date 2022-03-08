package com.odeyalo.analog.auth.dto.response;

public class ExceptionOccurredDTO {
    private String cause;
    private String status;

    public ExceptionOccurredDTO(String cause, String status) {
        this.cause = cause;
        this.status = status;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
