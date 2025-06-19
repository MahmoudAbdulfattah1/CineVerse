package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.Movie;
import com.cineverse.cineverse.dto.MovieDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MovieMapper {
    private final TMDBApiConfiguration tmdbApiConfiguration;

    public MovieDto toDto(Movie movie) {
        if (movie == null) return null;
        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                tmdbApiConfiguration.getBaseImageUrl() + movie.getPosterPath(),
                tmdbApiConfiguration.getBaseImageUrl() + movie.getBackdropPath(),
                movie.getReleaseDate(),
                movie.getRuntime(),
                movie.getLanguage(),
                movie.getProductionCountry(),
                movie.getImdbRate(),
                movie.getGenres().stream().map(genre -> genre.getGenre().getName()).toList()
        );

    }

}
