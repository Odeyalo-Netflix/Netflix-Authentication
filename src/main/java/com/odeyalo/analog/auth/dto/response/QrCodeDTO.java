package com.odeyalo.analog.auth.dto.response;

public class QrCodeDTO {
    private String clientId;
    private String data;
    private String time;

    public QrCodeDTO() {}

    public QrCodeDTO(String clientId, String data, String time) {
        this.clientId = clientId;
        this.data = data;
        this.time = time;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
