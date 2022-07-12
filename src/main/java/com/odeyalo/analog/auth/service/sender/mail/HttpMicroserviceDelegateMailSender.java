package com.odeyalo.analog.auth.service.sender.mail;

import com.odeyalo.support.clients.notification.NotificationSenderClient;
import com.odeyalo.support.clients.notification.dto.EmailMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Delegate mail sending to microservice
 */
@Component
public class HttpMicroserviceDelegateMailSender implements MailSender {
    private final NotificationSenderClient notificationSenderClient;
    private final Logger logger = LoggerFactory.getLogger(HttpMicroserviceDelegateMailSender.class);

    @Autowired
    public HttpMicroserviceDelegateMailSender(NotificationSenderClient notificationSenderClient) {
        this.notificationSenderClient = notificationSenderClient;
    }


    @Override
    public void send(String body, String subject, String to) {
        this.send(new GenericMailMessage(subject, body, to));
    }

    @Override
    public void send(GenericMailMessage dto) {
        this.logger.info("Sending message using Feign Client: {}", this.notificationSenderClient.getClass());
        ResponseEntity<?> response = this.notificationSenderClient.sendMail(new EmailMessageDTO());
        HttpStatus statusCode = response.getStatusCode();
        this.logger.info("Response status: {}", statusCode);
        if (statusCode.isError()) {
            this.logger.error("Sending message failed. Error code: {}, error message: {}", response.getStatusCode(), response.getBody());
        }
    }
}
