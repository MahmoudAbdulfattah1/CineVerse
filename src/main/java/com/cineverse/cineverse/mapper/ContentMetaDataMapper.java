package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.Movie;
import com.cineverse.cineverse.domain.entity.Series;
import com.cineverse.cineverse.dto.ContentMetaDataDto;
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
                    content.getGenres().stream().map(genre -> genre.getGenre().getName())
                            .collect(Collectors.toSet())
            );
        }).collect(Collectors.toList());

        return new PageImpl<>(dtoList, contentPage.getPageable(), contentPage.getTotalElements());
    }
}
