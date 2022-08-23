package com.odeyalo.analog.auth.config.kafka;


import com.odeyalo.support.clients.common.UserRegisteredDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class UserRegistryKafkaMessageBrokerProducerConfiguration extends JsonSerializerKafkaMessageBrokerProducerConfigurationSupport {

    @Bean
    public KafkaTemplate<String, UserRegisteredDTO> stringUserRegisteredDTOKafkaTemplate() {
        return new KafkaTemplate<>(stringUserRegisteredDTOProducerFactory());
    }

    @Bean
    public ProducerFactory<String, UserRegisteredDTO> stringUserRegisteredDTOProducerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerConfig());
    }
}
