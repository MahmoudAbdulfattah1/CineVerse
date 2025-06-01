package com.cineverse.cineverse.service;

import com.cineverse.cineverse.repository.MovieRepository;
import com.cineverse.cineverse.repository.SeriesRepository;
import com.cineverse.cineverse.util.SlugUtil;
import org.springframework.stereotype.Service;

@Service
public class SlugService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;

    public SlugService(MovieRepository movieRepository, SeriesRepository seriesRepository) {
        this.movieRepository = movieRepository;
        this.seriesRepository = seriesRepository;
    }

    public String generateUniqueSlugForMovie(String title) {
        return generateUniqueSlug(title, true);
    }

    public String generateUniqueSlugForSeries(String title) {
        return generateUniqueSlug(title, false);
    }

    private String generateUniqueSlug(String title, boolean isMovie) {
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (isSlugExists(slug, isMovie)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    private boolean isSlugExists(String slug, boolean isMovie) {
        return isMovie
                ? movieRepository.existsBySlug(slug)
                : seriesRepository.existsBySlug(slug);
    }
}