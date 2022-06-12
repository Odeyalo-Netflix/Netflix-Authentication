package com.odeyalo.analog.auth.service.support.ws;

import org.springframework.messaging.Message;

public interface WebSocketMessageChannelSender {

    void sendMessage(String destination, Message<?> message);

}
