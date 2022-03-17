package com.odeyalo.analog.auth.service.register.mail;

import com.odeyalo.analog.auth.dto.MailMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Delegate mail sending to microservice
 */
@Component
public class SimpleMicroserviceDelegateMailSender implements MailSender {
    private final RestTemplate restTemplate;
    private final static String EMAIL_SENDER_MICROSERVICE_ENTRYPOINT = "http://localhost:8081/api/v1/send";
    private final Logger logger = LoggerFactory.getLogger(SimpleMicroserviceDelegateMailSender.class);

    public SimpleMicroserviceDelegateMailSender(RestTemplate restTemplate) {
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
