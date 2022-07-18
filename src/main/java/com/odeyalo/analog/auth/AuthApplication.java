package com.odeyalo.analog.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableEurekaClient
@EnableFeignClients(basePackages = "com.odeyalo.support.clients")
@PropertySource("classpath:clients.default.properties")
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
