package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.Series;
import com.cineverse.cineverse.dto.SeriesDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SeriesMapper {
    private final TMDBApiConfiguration tmdbApiConfiguration;

    public SeriesDto toDto(Series series) {
        if (series == null) return null;
        return new SeriesDto(
                series.getId(),
                series.getTitle(),
                series.getOverview(),
                tmdbApiConfiguration.getBaseImageUrl() + series.getPosterPath(),
                tmdbApiConfiguration.getBaseImageUrl() + series.getBackdropPath(),
                series.getReleaseDate(),
                series.getLanguage(),
                series.getProductionCountry(),
                series.getImdbRate(),
                series.getNumberOfSeasons(),
                series.getNumberOfEpisodes(),
                series.getStatus(),
                series.getGenres().stream().map(genre -> genre.getGenre().getName()).toList()
        );
    }
}
