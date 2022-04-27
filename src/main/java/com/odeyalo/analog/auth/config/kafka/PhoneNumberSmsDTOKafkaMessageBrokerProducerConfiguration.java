package com.odeyalo.analog.auth.config.kafka;

import com.odeyalo.analog.auth.dto.PhoneNumberSmsDTO;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class PhoneNumberSmsDTOKafkaMessageBrokerProducerConfiguration extends JsonSerializerKafkaMessageBrokerProducerConfigurationSupport {

    @Bean
    public KafkaTemplate<String, PhoneNumberSmsDTO> phoneNumberSmsDTOKafkaTemplate(ProducerFactory<String, PhoneNumberSmsDTO> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ProducerFactory<String, PhoneNumberSmsDTO> phoneNumberSmsDTOProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig(), new StringSerializer(), new JsonSerializer<>());
    }
}
