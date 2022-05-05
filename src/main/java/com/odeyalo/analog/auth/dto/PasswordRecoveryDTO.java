package com.odeyalo.analog.auth.dto;

public class PasswordRecoveryDTO {
    private String password;

    public PasswordRecoveryDTO() {}

    public PasswordRecoveryDTO(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
