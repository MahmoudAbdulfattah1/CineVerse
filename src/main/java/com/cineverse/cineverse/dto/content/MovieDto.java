package com.cineverse.cineverse.dto.content;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto implements ContentDetailsDto {
    private int id;
    private String title;
    private String overview;
    private String posterUrl;
    private String backdropUrl;
    private LocalDate releaseDate;
    private int runtime;
    private String language;
    private String productionCountry;
    private float imdbRate;
    private List<String> genres;
}
