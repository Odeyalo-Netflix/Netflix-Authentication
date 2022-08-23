package com.odeyalo.analog.auth.dto.request;

public class NewPasswordDTO {
    private String password;

    public NewPasswordDTO() {}

    public NewPasswordDTO(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
