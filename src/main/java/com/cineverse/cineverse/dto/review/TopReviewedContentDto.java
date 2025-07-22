package com.cineverse.cineverse.dto.review;

import com.cineverse.cineverse.domain.enums.ContentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TopReviewedContentDto {
    private Long contentId;
    private String title;
    private ContentType contentType;
    private Double averageRate;
    private Integer reviewCount;

    public TopReviewedContentDto(Long contentId, String title, ContentType contentType, Double averageRate, Integer reviewCount) {
        this.contentId = contentId;
        this.title = title;
        this.contentType = contentType;
        this.averageRate = averageRate;
        this.reviewCount = reviewCount;
    }


}
