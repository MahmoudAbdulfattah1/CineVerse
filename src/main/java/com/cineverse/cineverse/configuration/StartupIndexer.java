package com.cineverse.cineverse.configuration;

import com.cineverse.cineverse.service.ContentIndexerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupIndexer implements CommandLineRunner {

    private final ContentIndexerService contentIndexerService;

    @Override
    public void run(String... args) {
        contentIndexerService.reindexAll();
    }
}