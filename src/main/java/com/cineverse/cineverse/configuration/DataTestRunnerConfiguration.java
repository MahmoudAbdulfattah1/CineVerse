package com.cineverse.cineverse.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataTestRunnerConfiguration {
    @Bean
    public CommandLineRunner testEntityStructure(

    ) {
        return args -> {

        };
    }
}
