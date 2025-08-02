package com.cineverse.cineverse.domain.document;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDate;
import java.util.Set;

@Document(indexName = "contents")
@Setting(settingPath = "/elasticsearch/settings.json")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentDocument {
    @Id
    private String id;
    @Field(type = FieldType.Text, analyzer = "arabic_english_autocomplete", searchAnalyzer =
            "arabic_english_autocomplete")
    private String title;
    @Field(type = FieldType.Text, analyzer = "arabic_english_autocomplete", searchAnalyzer =
            "arabic_english_autocomplete")
    private String overview;
    private String slug;
    private String posterPath;
    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    private float imdbRate;
    private String contentType;
    private Set<String> genres;
}
