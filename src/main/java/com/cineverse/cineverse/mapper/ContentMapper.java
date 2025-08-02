package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.document.ContentDocument;
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
        List<ContentMetaDataDto> dtoList = contentPage.getContent().stream()
                .map(this::toContentMetaDataDto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, contentPage.getPageable(), contentPage.getTotalElements());
    }


    public ContentMetaDataDto toContentMetaDataDto(Content content) {
        String slug = null;
        if (content instanceof Movie movie) slug = movie.getSlug();
        else if (content instanceof Series series) slug = series.getSlug();
        return ContentMetaDataDto.builder()
                .id(content.getId())
                .title(content.getTitle())
                .slug(slug)
                .posterUrl(tmdbApiConfiguration.getBaseImageUrl() + content.getPosterPath())
                .releaseDate(content.getReleaseDate())
                .imdbRate(content.getImdbRate())
                .overview(content.getOverview())
                .type(content.getContentType().toString())
                .genres(content.getGenres().stream()
                        .map(genre -> genre.getGenre().getName())
                        .collect(Collectors.toSet()))
                .build();
    }

    public ContentSummaryDto toContentSummary(Content content) {
        ContentSummaryDto.ContentSummaryDtoBuilder builder = ContentSummaryDto.builder()
                .contentType(content.getContentType());

        switch (content.getContentType()) {
            case MOVIE -> builder.slug(((Movie) content).getSlug());

            case SERIES -> builder.slug(((Series) content).getSlug());

            case SEASON -> {
                Season season = (Season) content;
                builder.seasonNumber(season.getSeasonNumber())
                        .slug(season.getSeries().getSlug());
            }

            case EPISODE -> {
                Episode episode = (Episode) content;
                builder.seasonNumber(episode.getSeason().getSeasonNumber())
                        .episodeNumber(episode.getEpisodeNumber())
                        .slug(episode.getSeason().getSeries().getSlug());
            }
        }

        return builder.build();
    }

    public EpisodeDto toEpisodeDto(Episode episode) {
        if (episode == null) return null;
        return EpisodeDto.builder()
                .id(episode.getId())
                .title(episode.getTitle())
                .overview(episode.getOverview())
                .posterUrl(episode.getPosterPath() == null || episode.getPosterPath().isBlank() ? null :
                        tmdbApiConfiguration.getBaseImageUrl() + episode.getPosterPath())
                .episodeNumber(episode.getEpisodeNumber())
                .imdbRate(episode.getImdbRate())
                .runTime(episode.getRunTime())
                .releaseDate(episode.getReleaseDate())
                .build();
    }

    public SeriesDto toSeriesDto(Series series) {
        if (series == null) return null;
        return SeriesDto.builder()
                .id(series.getId())
                .title(series.getTitle())
                .overview(series.getOverview())
                .posterUrl(tmdbApiConfiguration.getBaseImageUrl() + series.getPosterPath())
                .backdropUrl(tmdbApiConfiguration.getBaseImageUrl() + series.getBackdropPath())
                .releaseDate(series.getReleaseDate())
                .language(series.getLanguage())
                .productionCountry(series.getProductionCountry())
                .imdbRate(series.getImdbRate())
                .numberOfSeasons(series.getNumberOfSeasons())
                .numberOfEpisodes(series.getNumberOfEpisodes())
                .status(series.getStatus())
                .genres(series.getGenres().stream()
                        .map(genre -> genre.getGenre().getName())
                        .collect(Collectors.toList()))
                .build();
    }

    public MovieDto toMovieDto(Movie movie) {
        if (movie == null) return null;
        return MovieDto.builder()
                .id(movie.getId())
                .title(movie.getTitle())
                .overview(movie.getOverview())
                .posterUrl(tmdbApiConfiguration.getBaseImageUrl() + movie.getPosterPath())
                .backdropUrl(tmdbApiConfiguration.getBaseImageUrl() + movie.getBackdropPath())
                .releaseDate(movie.getReleaseDate())
                .runtime(movie.getRuntime())
                .language(movie.getLanguage())
                .productionCountry(movie.getProductionCountry())
                .imdbRate(movie.getImdbRate())
                .genres(movie.getGenres().stream()
                        .map(genre -> genre.getGenre().getName())
                        .collect(Collectors.toList()))
                .build();
    }

    public SeasonDto toSeasonDto(Season season) {
        if (season == null) return null;
        return SeasonDto.builder()
                .id(season.getId())
                .title(season.getTitle())
                .overview(season.getOverview())
                .posterUrl(season.getPosterPath() == null || season.getPosterPath().isBlank() ? null :
                        tmdbApiConfiguration.getBaseImageUrl() + season.getPosterPath())
                .seasonNumber(season.getSeasonNumber())
                .imdbRate(season.getImdbRate())
                .releaseDate(season.getReleaseDate())
                .numberOfEpisodes(contentService.getEpisodeCountBySeasonId(season.getId()))
                .build();
    }

    public List<ProviderDto> toProviderDto(List<Provider> providers) {
        if (providers == null) return List.of();
        return providers.stream()
                .map(provider -> new ProviderDto(provider.getName(), fullPath(provider.getLogo())))
                .toList();
    }

    public ContentDocument toContentDocument(Content content) {
        String slug = null;
        if (content instanceof Movie movie) slug = movie.getSlug();
        else if (content instanceof Series series) slug = series.getSlug();

        return ContentDocument.builder()
                .id(String.valueOf(content.getId()))
                .title(content.getTitle())
                .slug(slug)
                .overview(content.getOverview())
                .posterPath(content.getPosterPath())
                .releaseDate(content.getReleaseDate())
                .imdbRate(content.getImdbRate())
                .contentType(content.getContentType().toString())
                .genres(content.getGenres().stream()
                        .map(g -> g.getGenre().getName())
                        .collect(Collectors.toSet()))
                .build();
    }

    public ContentMetaDataDto toContentMetaDataDto(ContentDocument contentDocument) {
        return ContentMetaDataDto.builder()
                .id(Integer.parseInt(contentDocument.getId()))
                .title(contentDocument.getTitle())
                .slug(contentDocument.getSlug())
                .posterUrl(fullPath(contentDocument.getPosterPath()))
                .releaseDate(contentDocument.getReleaseDate())
                .imdbRate(contentDocument.getImdbRate())
                .overview(contentDocument.getOverview())
                .type(contentDocument.getContentType())
                .genres(contentDocument.getGenres())
                .build();
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }


}
