package com.odeyalo.analog.auth.config.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;

public abstract class JsonSerializerKafkaMessageBrokerProducerConfigurationSupport extends AbstractKafkaMessageBrokerProducerConfigurationSupport {

    @Override
    protected HashMap<String, Object> kafkaProducerConfig() {
        HashMap<String, Object> config = new HashMap<>(5);
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, APACHE_KAFKA_MESSAGE_BROKER_CONNECTION_URL);
        config.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return config;
    }
}
