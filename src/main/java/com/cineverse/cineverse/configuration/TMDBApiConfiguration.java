package com.cineverse.cineverse.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TMDBApiConfiguration {
    @Value("${tmdb.api.key}")
    private String apiKey;
    @Value("${base.image.url}")
    private String baseImageUrl;


    public String getApiKey() {
        return apiKey;
    }

    public String getBaseImageUrl() {
        return baseImageUrl;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
