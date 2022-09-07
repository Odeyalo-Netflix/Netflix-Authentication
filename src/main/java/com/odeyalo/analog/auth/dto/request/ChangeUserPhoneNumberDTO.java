package com.odeyalo.analog.auth.dto.request;

public class ChangeUserPhoneNumberDTO {
    private String newPhoneNumber;

    public ChangeUserPhoneNumberDTO() {
    }

    public ChangeUserPhoneNumberDTO(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }

    public String getNewPhoneNumber() {
        return newPhoneNumber;
    }

    public void setNewPhoneNumber(String newPhoneNumber) {
        this.newPhoneNumber = newPhoneNumber;
    }
}
