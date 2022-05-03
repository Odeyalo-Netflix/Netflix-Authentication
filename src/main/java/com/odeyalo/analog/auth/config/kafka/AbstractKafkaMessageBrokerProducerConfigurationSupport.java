package com.odeyalo.analog.auth.config.kafka;

import java.util.HashMap;

public abstract class AbstractKafkaMessageBrokerProducerConfigurationSupport {

    protected static final String APACHE_KAFKA_MESSAGE_BROKER_CONNECTION_URL = "localhost:9092";

    protected abstract HashMap<String, Object> kafkaProducerConfig();

}
