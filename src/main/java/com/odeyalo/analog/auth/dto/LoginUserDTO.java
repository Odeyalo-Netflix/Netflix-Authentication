package com.odeyalo.analog.auth.dto;

public class LoginUserDTO {
    private String nickname;
    private String password;

    public LoginUserDTO(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
