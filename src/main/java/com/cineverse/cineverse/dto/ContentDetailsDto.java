package com.cineverse.cineverse.dto;

import java.time.LocalDate;
import java.util.List;

public interface ContentDetailsDto {
    int getId();

    String getTitle();

    String getOverview();

    String getPosterPath();

    LocalDate getReleaseDate();

    String getLanguage();

    String getProductionCountry();

    float getImdbRate();

    float getPlatformRate();

    List<String> getGenres();
}
