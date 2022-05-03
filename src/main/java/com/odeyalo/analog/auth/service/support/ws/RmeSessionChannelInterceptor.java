package com.odeyalo.analog.auth.service.support.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;

public class RmeSessionChannelInterceptor implements ChannelInterceptor {
    private final Logger logger = LoggerFactory.getLogger(RmeSessionChannelInterceptor.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        this.logger.info("Received a new websocket message: " + message.getPayload());
        return message;
    }
}
