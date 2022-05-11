package com.odeyalo.analog.auth.service.support.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class StompProtocolWebSocketMessageChannelSender implements WebSocketMessageChannelSender {
    private final SimpMessagingTemplate template;
    private final Logger logger = LoggerFactory.getLogger(StompProtocolWebSocketMessageChannelSender.class);

    public StompProtocolWebSocketMessageChannelSender(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void sendMessage(String destination, Message<?> message) {
        Object payload = message.getPayload();
        this.logger.info("SEND MESSAGE WITH PAYLOAD: {}", payload);
        this.template.convertAndSend(destination, payload);
    }
}
