package com.cineverse.cineverse.dto.content;

import lombok.*;

import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentMetaDataDto {
    private int id;
    private String title;
    private String slug;
    private String posterUrl;
    private LocalDate releaseDate;
    private Float imdbRate;
    private String overview;
    private String type;
    private Set<String> genres;
}
