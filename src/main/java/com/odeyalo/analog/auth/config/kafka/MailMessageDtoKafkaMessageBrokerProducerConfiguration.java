package com.odeyalo.analog.auth.config.kafka;

import com.odeyalo.support.clients.notification.dto.EmailMessageDTO;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class MailMessageDtoKafkaMessageBrokerProducerConfiguration extends JsonSerializerKafkaMessageBrokerProducerConfigurationSupport {
    @Bean
    public KafkaTemplate<String, EmailMessageDTO> mailMessageDTOKafkaTemplate(ProducerFactory<String, EmailMessageDTO> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ProducerFactory<String, EmailMessageDTO> mailMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig(), new StringSerializer(), new JsonSerializer<>());
    }
}
