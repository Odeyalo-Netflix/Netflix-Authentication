package com.odeyalo.analog.auth.dto;

public class PhoneNumberSmsDTO {
    private String phoneNumber;
    private String body;

    public PhoneNumberSmsDTO() {}

    public PhoneNumberSmsDTO(String phoneNumber, String body) {
        this.phoneNumber = phoneNumber;
        this.body = body;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
