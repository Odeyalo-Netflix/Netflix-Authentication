package com.odeyalo.analog.auth.service.sender.sms;

public interface PhoneNumberMessageSender {

    void sendMessage(String to, String message);

}
