package com.cineverse.cineverse.service;

import com.cineverse.cineverse.repository.MovieRepository;
import com.cineverse.cineverse.repository.SeriesRepository;
import com.cineverse.cineverse.util.SlugUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SlugService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    private String generateUniqueSlug(String title) {
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (isSlugExists(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    private boolean isSlugExists(String slug) {
        return movieRepository.existsBySlug(slug) || seriesRepository.existsBySlug(slug);
    }
}