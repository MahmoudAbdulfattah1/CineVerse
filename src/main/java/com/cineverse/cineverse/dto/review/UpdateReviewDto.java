package com.cineverse.cineverse.dto.review;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Builder
public class UpdateReviewDto {
    @Range(min = 1, max = 10, message = "Rate must be between 1 and 10")
    private int rate;
    @Size(max = 200, message = "Title must be at most 200 characters long")
    private String title;
    @Size(max = 1000, message = "Description must be at most 1000 characters long")
    private String description;
    private boolean spoiler;

}
