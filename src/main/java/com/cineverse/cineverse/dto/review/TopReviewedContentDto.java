package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ContentType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopReviewedContentDto {
    private Long contentId;
    private String title;
    private ContentType contentType;
    private Double averageRate;
    private Integer reviewCount;
}
