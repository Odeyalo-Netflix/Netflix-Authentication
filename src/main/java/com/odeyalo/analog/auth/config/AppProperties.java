package com.odeyalo.analog.auth.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "app")
public class AppProperties {
    @Value("${app.oauth2.authorizedRedirectUris}")
    private final List<String> authorizedRedirectUris = new ArrayList<>(5);


    public List<String> getAuthorizedRedirectUris() {
        return authorizedRedirectUris;
    }
}
