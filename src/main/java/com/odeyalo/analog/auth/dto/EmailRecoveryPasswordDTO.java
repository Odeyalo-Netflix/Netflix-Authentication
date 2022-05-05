package com.odeyalo.analog.auth.dto;

public class EmailRecoveryPasswordDTO {
    private String email;

    public EmailRecoveryPasswordDTO() {}

    public EmailRecoveryPasswordDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
