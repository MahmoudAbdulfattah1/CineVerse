package com.cineverse.cineverse.dto.content;

import java.time.LocalDate;
import java.util.List;

public interface ContentDetailsDto {
    int getId();

    String getTitle();

    String getOverview();


    LocalDate getReleaseDate();

    String getLanguage();

    String getProductionCountry();

    float getImdbRate();


    List<String> getGenres();
}
