package com.odeyalo.analog.auth.config.kafka;

import com.odeyalo.analog.auth.dto.MailMessageDTO;
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
    public KafkaTemplate<String, MailMessageDTO> mailMessageDTOKafkaTemplate(ProducerFactory<String, MailMessageDTO> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ProducerFactory<String, MailMessageDTO> mailMessageProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig(), new StringSerializer(), new JsonSerializer<>());
    }
}
