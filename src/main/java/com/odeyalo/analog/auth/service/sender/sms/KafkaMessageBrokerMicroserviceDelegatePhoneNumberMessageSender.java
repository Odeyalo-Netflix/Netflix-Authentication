package com.odeyalo.analog.auth.service.sender.sms;

import com.odeyalo.analog.auth.dto.PhoneNumberSmsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Send message to another microservice using kafka message broker
 */
@Component
public class KafkaMessageBrokerMicroserviceDelegatePhoneNumberMessageSender implements MicroserviceDelegatePhoneNumberMessageSender {
    private final KafkaTemplate<String, PhoneNumberSmsDTO> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(KafkaMessageBrokerMicroserviceDelegatePhoneNumberMessageSender.class);
    public KafkaMessageBrokerMicroserviceDelegatePhoneNumberMessageSender(KafkaTemplate<String, PhoneNumberSmsDTO> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendMessage(String to, String message) {
        this.logger.info("Send message: {}, body: {}", to, message);
        this.kafkaTemplate.send("PHONE_NUMBER_SMS_SENDER", new PhoneNumberSmsDTO(to, message));
    }
}
