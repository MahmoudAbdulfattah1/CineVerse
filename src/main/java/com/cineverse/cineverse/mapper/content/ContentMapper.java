package com.cineverse.cineverse.mapper.content;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.dto.content.*;
import com.cineverse.cineverse.service.ContentService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ContentMapper {
    private ContentService contentService;
    private final TMDBApiConfiguration tmdbApiConfiguration;

    public Page<ContentMetaDataDto> toContentMetaDataDto(Page<Content> contentPage) {
        List<ContentMetaDataDto> dtoList = contentPage.getContent().stream().map(content -> {

            String slug = null;
            if (content instanceof Movie movie) slug = movie.getSlug();
            else if (content instanceof Series series) slug = series.getSlug();

            return new ContentMetaDataDto(
                    content.getId(),
                    content.getTitle(),
                    slug,
                    tmdbApiConfiguration.getBaseImageUrl() + content.getPosterPath(),
                    content.getReleaseDate(),
                    content.getImdbRate(),
                    content.getOverview(),
                    content.getContentType().toString(),
                    content.getGenres().stream().map(genre -> genre.getGenre().getName())
                            .collect(Collectors.toSet())
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, contentPage.getPageable(), contentPage.getTotalElements());
    }

    public ContentSummaryDto toContentSummary(Content content) {
        ContentSummaryDto dto = new ContentSummaryDto();
        dto.setContentType(content.getContentType());

        switch (content.getContentType()) {
            case MOVIE -> dto.setSlug(((Movie) content).getSlug());

            case SERIES -> dto.setSlug(((Series) content).getSlug());

            case SEASON -> {
                Season season = (Season) content;
                dto.setSeasonNumber(season.getSeasonNumber());
                dto.setSlug(season.getSeries().getSlug()); // Series slug
            }

            case EPISODE -> {
                Episode episode = (Episode) content;
                dto.setSeasonNumber(episode.getSeason().getSeasonNumber());
                dto.setEpisodeNumber(episode.getEpisodeNumber());
                dto.setSlug(episode.getSeason().getSeries().getSlug()); // Series slug
            }
        }

        return dto;
    }

    public EpisodeDto toEpisodeDto(Episode episode) {
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

    public SeriesDto toSeriesDto(Series series) {
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

    public MovieDto toMovieDto(Movie movie) {
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

    public SeasonDto toSeasonDto(Season season) {
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

    public List<ProviderDto> toProviderDto(List<Provider> providers) {
        if (providers == null) return List.of();
        return providers.stream()
                .map(provider -> new ProviderDto(provider.getName(), fullPath(provider.getLogo())))
                .toList();
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }


}
