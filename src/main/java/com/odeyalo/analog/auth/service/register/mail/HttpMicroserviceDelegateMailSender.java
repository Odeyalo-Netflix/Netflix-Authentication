package com.odeyalo.analog.auth.service.register.mail;

import com.odeyalo.analog.auth.dto.MailMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Delegate mail sending to microservice
 */
@Component
public class HttpMicroserviceDelegateMailSender implements MailSender {
    private final RestTemplate restTemplate;
    @Value("send-email")
    private static String EMAIL_SENDER_MICROSERVICE_ENTRYPOINT;
    private final Logger logger = LoggerFactory.getLogger(HttpMicroserviceDelegateMailSender.class);

    public HttpMicroserviceDelegateMailSender(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void send(String body, String subject, String to) {
        this.send(new MailMessageDTO(subject, body, to));
    }

    @Override
    public void send(MailMessageDTO dto) {
        this.restTemplate.postForObject(EMAIL_SENDER_MICROSERVICE_ENTRYPOINT, dto, String.class);
    }
}
