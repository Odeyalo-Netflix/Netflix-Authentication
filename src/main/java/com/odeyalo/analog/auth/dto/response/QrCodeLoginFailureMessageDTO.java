package com.odeyalo.analog.auth.dto.response;

import com.odeyalo.analog.auth.dto.enums.QrCodeLoginMessageType;

public class QrCodeLoginFailureMessageDTO {
    private final QrCodeLoginMessageType messageType = QrCodeLoginMessageType.QR_CODE_LOGIN_FAILURE;
    private String message;

    public QrCodeLoginFailureMessageDTO() {}

    public QrCodeLoginFailureMessageDTO(String message) {
        this.message = message;
    }

    public QrCodeLoginMessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
