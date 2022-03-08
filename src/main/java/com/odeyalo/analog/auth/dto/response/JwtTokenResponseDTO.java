package com.odeyalo.analog.auth.dto.response;

public class JwtTokenResponseDTO {
    private boolean isSuccess;
    private String jwtToken;
    private String refreshToken;

    public JwtTokenResponseDTO(boolean isSuccess, String jwtToken, String refreshToken) {
        this.isSuccess = isSuccess;
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
