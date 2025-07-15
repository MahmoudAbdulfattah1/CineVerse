package com.cineverse.cineverse.mapper.content;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.Episode;
import com.cineverse.cineverse.dto.content.EpisodeDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class EpisodeMapper {
    private final TMDBApiConfiguration tmdbApiConfiguration;

    public List<EpisodeDto> toDto(List<Episode> episodes) {
        if (episodes == null) return null;
        return episodes.stream().map(episode -> new EpisodeDto(
                episode.getId(),
                episode.getTitle(),
                episode.getOverview(),
                episode.getPosterPath() == null || episode.getPosterPath().isBlank() ? null :
                        tmdbApiConfiguration.getBaseImageUrl() + episode.getPosterPath(),
                episode.getEpisodeNumber(),
                episode.getImdbRate(),
                episode.getRunTime(),
                episode.getReleaseDate()
        )).toList();
    }

    public EpisodeDto toDto(Episode episode) {
        if (episode == null) return null;
        return new EpisodeDto(
                episode.getId(),
                episode.getTitle(),
                episode.getOverview(),
                episode.getPosterPath() == null || episode.getPosterPath().isBlank() ? null :
                        tmdbApiConfiguration.getBaseImageUrl() + episode.getPosterPath(),
                episode.getEpisodeNumber(),
                episode.getImdbRate(),
                episode.getRunTime(),
                episode.getReleaseDate()
        );
    }
}
