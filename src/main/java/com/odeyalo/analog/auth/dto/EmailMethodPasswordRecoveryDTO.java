package com.odeyalo.analog.auth.dto;

public class EmailMethodPasswordRecoveryDTO {
    private String email;

    public EmailMethodPasswordRecoveryDTO() {}

    public EmailMethodPasswordRecoveryDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
