package com.cineverse.cineverse.dto.content;

import com.cineverse.cineverse.domain.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentSummaryDto {
    private ContentType contentType;
    private String slug;
    private Integer seasonNumber;
    private Integer episodeNumber;
}