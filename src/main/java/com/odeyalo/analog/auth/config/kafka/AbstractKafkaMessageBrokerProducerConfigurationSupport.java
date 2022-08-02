package com.odeyalo.analog.auth.config.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.HashMap;

public abstract class AbstractKafkaMessageBrokerProducerConfigurationSupport {
    @Value("${kafka.connection.url}")
    protected String APACHE_KAFKA_MESSAGE_BROKER_CONNECTION_URL = "localhost:9092";

    protected Logger logger =  LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    void init() {
        this.logger.info("Running kafka on this port: {}", APACHE_KAFKA_MESSAGE_BROKER_CONNECTION_URL);
    }

    protected abstract HashMap<String, Object> kafkaProducerConfig();

}
