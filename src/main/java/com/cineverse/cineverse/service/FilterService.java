package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.Genre;
import com.cineverse.cineverse.domain.enums.GenreType;
import com.cineverse.cineverse.dto.content.FilterOption;
import com.cineverse.cineverse.dto.content.FilterSection;
import com.cineverse.cineverse.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class FilterService {
    private GenreRepository genreRepository;

    /**
     * Builds a list of filter sections to be displayed in the UI, including movie genres,
     * series genres, content types, languages, sorting options, order, and release status.
     *
     * @return a list of {@link FilterSection} objects, each containing a category of filters
     */
    public List<FilterSection> getFilterOptions() {
        List<FilterSection> sections = new ArrayList<>();
        Set<String> excludedGenres = Set.of("Music", "Romance", "Soap");
        List<Genre> genres = genreRepository.findAll();
        List<FilterOption> movieGenres = genres.stream()
                .filter(genre ->
                        (genre.getType() == GenreType.MOVIE || genre.getType() == GenreType.ALL) &&
                                !excludedGenres.contains(genre.getName())
                )
                .map(genre -> new FilterOption(genre.getName(), genre.getName()))
                .toList();
        sections.add(new FilterSection("Movie Genres", "genres", movieGenres, true));

        List<FilterOption> seriesGenres = genres.stream()
                .filter(genre ->
                        (genre.getType() == GenreType.SERIES || genre.getType() == GenreType.ALL) &&
                                !excludedGenres.contains(genre.getName())
                )
                .map(genre -> new FilterOption(genre.getName(), genre.getName())).toList();
        sections.add(new FilterSection("Series Genres", "genres", seriesGenres, true));

        List<FilterOption> typeOptions = List.of(new FilterOption("Movie", "MOVIE"), new FilterOption("Series",
                "SERIES"));

        sections.add(new FilterSection("Content", "type", typeOptions, false));
        List<FilterOption> languageOptions = List.of(
                new FilterOption("English", "en"),
                new FilterOption("Arabic", "ar"),
                new FilterOption("Spanish", "es"),
                new FilterOption("French", "fr"),
                new FilterOption("German", "de"),
                new FilterOption("Japanese", "ja"),
                new FilterOption("Korean", "ko"),
                new FilterOption("Chinese", "cn"),
                new FilterOption("Hindi", "hi"),
                new FilterOption("Italian", "it")
        );
        sections.add(new FilterSection("Language", "lang", languageOptions, false));
        List<FilterOption> sortOptions = List.of(
                new FilterOption("Top Rated", "topRated"),
                new FilterOption("Most Recent", "mostRecent"),
                new FilterOption("Title", "title")
        );
        sections.add(new FilterSection("Sort", "sortBy", sortOptions, false));


        List<FilterOption> orderOptions = List.of(
                new FilterOption("Asc", "asc"),
                new FilterOption("Desc", "desc")
        );
        sections.add(new FilterSection("Order", "order", orderOptions, false));

        List<FilterOption> contentStatus = List.of(
                new FilterOption("Released", "RELEASED"),
                new FilterOption("Upcoming", "UPCOMING")
        );
        sections.add(new FilterSection("Release Status", "status", contentStatus, false));
        return sections;
    }
}
