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

    /**
     * Generates a unique slug from the given title.
     * <p>
     * Converts the title into a slug using {@link SlugUtil#toSlug(String)} and
     * checks if the slug already exists in either the movie or series repository.
     * If a conflict is found, appends an incremental number until a unique slug is obtained.
     * </p>
     *
     * @param title The title from which to generate the slug.
     * @return A unique, URL-safe slug.
     */
    public String generateUniqueSlug(String title) {
        String baseSlug = SlugUtil.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (isSlugExists(slug)) {
            slug = baseSlug + "-" + counter++;
        }

        return slug;
    }

    /**
     * Checks whether a given slug already exists in either the movie or series repository.
     *
     * @param slug The slug to check for existence.
     * @return {@code true} if the slug exists in movies or series; {@code false} otherwise.
     */
    private boolean isSlugExists(String slug) {
        return movieRepository.existsBySlug(slug) || seriesRepository.existsBySlug(slug);
    }
}