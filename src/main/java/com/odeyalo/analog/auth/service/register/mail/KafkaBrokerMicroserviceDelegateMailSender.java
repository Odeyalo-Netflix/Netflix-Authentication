package com.odeyalo.analog.auth.service.register.mail;

import com.odeyalo.analog.auth.dto.MailMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class KafkaBrokerMicroserviceDelegateMailSender implements MailSender {
    private final KafkaTemplate<String, MailMessageDTO> mailSenderKafkaTemplate;
    private final static String KAFKA_BROKER_MAIL_SENDER_TOPIC_ENTRYPOINT = "MAIL_MESSAGE_SENDER_TOPIC";

    @Autowired
    public KafkaBrokerMicroserviceDelegateMailSender(@Qualifier("mailMessageDTOKafkaTemplate") KafkaTemplate<String, MailMessageDTO> mailSenderKafkaTemplate) {
        this.mailSenderKafkaTemplate = mailSenderKafkaTemplate;
    }

    @Override
    public void send(String body, String subject, String to) {
        this.send(new MailMessageDTO(subject, body, to));
    }

    @Override
    public void send(MailMessageDTO dto) {
        ListenableFuture<SendResult<String, MailMessageDTO>> result = this.mailSenderKafkaTemplate.send(KAFKA_BROKER_MAIL_SENDER_TOPIC_ENTRYPOINT, dto);
        result.addCallback(System.out::println, System.out::println);
    }
}
