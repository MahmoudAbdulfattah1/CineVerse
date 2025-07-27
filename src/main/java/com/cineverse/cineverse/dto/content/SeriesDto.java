package com.cineverse.cineverse.dto.content;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeriesDto implements ContentDetailsDto {
    private int id;
    private String title;
    private String overview;
    private String posterUrl;
    private String backdropUrl;
    private LocalDate releaseDate;
    private String language;
    private String productionCountry;
    private float imdbRate;
    private int numberOfSeasons;
    private int numberOfEpisodes;
    private String status;
    private List<String> genres;
}
