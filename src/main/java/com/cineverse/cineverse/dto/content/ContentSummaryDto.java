package com.cineverse.cineverse.dto.content;

import com.cineverse.cineverse.domain.enums.ContentType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContentSummaryDto {
    private ContentType contentType;
    private String slug;
    private Integer seasonNumber;
    private Integer episodeNumber;
}