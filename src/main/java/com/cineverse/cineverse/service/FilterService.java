package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.entity.Genre;
import com.cineverse.cineverse.dto.FilterOption;
import com.cineverse.cineverse.dto.FilterSection;
import com.cineverse.cineverse.repository.GenreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FilterService {
    private GenreRepository genreRepository;

    public List<FilterSection> getFilterOptions() {
        List<FilterSection> sections = new ArrayList<>();
        List<Genre> genres = genreRepository.findAll();
        List<FilterOption> genreOptions = genres.stream().map(genre -> new FilterOption(genre.getName(), genre.getName())).toList();
        sections.add(new FilterSection("Genres", "genres", genreOptions, true));
        List<FilterOption> typeOptions = List.of(new FilterOption("Movie", "MOVIE"), new FilterOption("Series", "SERIES"));
        sections.add(new FilterSection("Content", "type", typeOptions, false));
        List<FilterOption> languageOptions = List.of(
                new FilterOption("English", "en"),
                new FilterOption("Arabic", "ar"),
                new FilterOption("Spain", "es")
        );
        sections.add(new FilterSection("Language", "lang", languageOptions, false));
        List<FilterOption> sortOptions = List.of(
                new FilterOption("Top Rated", "topRated"),
                new FilterOption("Most Recent", "mostRecent")
        );
        sections.add(new FilterSection("Sort", "sortBy", sortOptions, false));
        return sections;
    }
}
