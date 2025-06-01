package com.cineverse.cineverse.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YoutubeApiConfiguration {
    @Value("${youtube.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

}
