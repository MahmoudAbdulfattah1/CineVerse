package com.cineverse.cineverse.mapper.content;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.Season;
import com.cineverse.cineverse.dto.content.SeasonDto;
import com.cineverse.cineverse.service.ContentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SeasonMapper {
    private final TMDBApiConfiguration tmdbApiConfiguration;
    private ContentService contentService;

    public List<SeasonDto> toDto(List<Season> seasons) {
        if (seasons == null) return null;
        return seasons.stream().map(season -> new SeasonDto(
                season.getId(),
                season.getTitle(),
                season.getOverview(),
                season.getPosterPath() == null || season.getPosterPath().isBlank() ? null :
                        tmdbApiConfiguration.getBaseImageUrl() + season.getPosterPath(),
                season.getSeasonNumber(),
                season.getImdbRate(),
                season.getReleaseDate(),
                contentService.getEpisodeCountBySeasonId(season.getId())
        )).toList();
    }

    public SeasonDto toDto(Season season) {
        if (season == null) return null;
        return new SeasonDto(
                season.getId(),
                season.getTitle(),
                season.getOverview(),
                season.getPosterPath() == null || season.getPosterPath().isBlank() ? null :
                        tmdbApiConfiguration.getBaseImageUrl() + season.getPosterPath(),
                season.getSeasonNumber(),
                season.getImdbRate(),
                season.getReleaseDate(),
                contentService.getEpisodeCountBySeasonId(season.getId())
        );
    }
}
