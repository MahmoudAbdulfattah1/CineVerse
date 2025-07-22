package com.cineverse.cineverse.mapper.content;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.*;
import com.cineverse.cineverse.dto.content.ContentMetaDataDto;
import com.cineverse.cineverse.dto.content.ContentSummaryDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ContentMetaDataMapper {
    private final TMDBApiConfiguration tmdbApiConfiguration;

    public List<ContentMetaDataDto> toDto(List<Content> contents) {
        return contents.stream().map(content -> {

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
        }).toList();
    }
    public Page<ContentMetaDataDto> toDto(Page<Content> contentPage) {
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


}
