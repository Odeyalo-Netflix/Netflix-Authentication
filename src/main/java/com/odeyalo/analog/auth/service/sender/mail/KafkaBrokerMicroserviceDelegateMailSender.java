package com.odeyalo.analog.auth.service.sender.mail;

import com.odeyalo.support.clients.notification.dto.EmailMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

@Service
public class KafkaBrokerMicroserviceDelegateMailSender implements MailSender {
    private final KafkaTemplate<String, EmailMessageDTO> mailSenderKafkaTemplate;
    private final static String KAFKA_BROKER_MAIL_SENDER_TOPIC_ENTRYPOINT = "MAIL_MESSAGE_SENDER_TOPIC";
    private final Logger logger = LoggerFactory.getLogger(KafkaBrokerMicroserviceDelegateMailSender.class);

    @Autowired
    public KafkaBrokerMicroserviceDelegateMailSender(@Qualifier("mailMessageDTOKafkaTemplate") KafkaTemplate<String, EmailMessageDTO> mailSenderKafkaTemplate) {
        this.mailSenderKafkaTemplate = mailSenderKafkaTemplate;
    }

    @Override
    public void send(String body, String subject, String to) {
        this.send(new GenericMailMessage(subject, body, to));
    }

    @Override
    public void send(GenericMailMessage message) {
        ListenableFuture<SendResult<String, EmailMessageDTO>> result = this.mailSenderKafkaTemplate.send(KAFKA_BROKER_MAIL_SENDER_TOPIC_ENTRYPOINT,
                new EmailMessageDTO(message.getTo(), message.getBody(), message.getBody()));
        result.addCallback(new SuccessCallback<SendResult<String, EmailMessageDTO>>() {
            private final Logger logger = LoggerFactory.getLogger(this.getClass());

            @Override
            public void onSuccess(SendResult<String, EmailMessageDTO> result) {
                this.logger.info("Success delivered message to topic {} with headers {}, value in message was: {}",
                        result.getProducerRecord().topic(),
                        result.getProducerRecord().headers(),
                        result.getProducerRecord().value());
            }
        }, new FailureCallback() {
            private final Logger logger = LoggerFactory.getLogger(this.getClass());

            @Override
            public void onFailure(Throwable ex) {
                logger.error("Message delivered failed, exception message: {}, stacktrace: {}", ex.getMessage(), ex.fillInStackTrace());
            }
        });
    }
}
