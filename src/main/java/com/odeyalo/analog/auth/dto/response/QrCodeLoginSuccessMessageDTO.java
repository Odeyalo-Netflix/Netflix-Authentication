package com.odeyalo.analog.auth.dto.response;

import com.odeyalo.analog.auth.dto.enums.QrCodeLoginMessageType;

public class QrCodeLoginSuccessMessageDTO {
    private final QrCodeLoginMessageType messageType = QrCodeLoginMessageType.QR_CODE_LOGIN_SUCCESS;
    private JwtTokenResponseDTO tokens;

    public QrCodeLoginSuccessMessageDTO(JwtTokenResponseDTO tokens) {
        this.tokens = tokens;
    }

    public QrCodeLoginMessageType getMessageType() {
        return messageType;
    }

    public JwtTokenResponseDTO getTokens() {
        return tokens;
    }

    public void setTokens(JwtTokenResponseDTO tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return "QrCodeLoginSuccessMessageDTO{" +
                "messageType=" + messageType +
                ", tokens=" + tokens +
                '}';
    }
}
