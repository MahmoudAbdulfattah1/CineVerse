package com.cineverse.cineverse.configuration;

import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.repository.*;
import com.cineverse.cineverse.service.SlugService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class DataTestRunnerConfiguration {
    @Bean
    public CommandLineRunner testEntityStructure(

    ) {
        return args -> {

        };
    }
}
