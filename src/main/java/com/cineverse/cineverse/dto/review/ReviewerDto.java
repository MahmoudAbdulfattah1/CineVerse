package com.cineverse.cineverse.dto.review;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewerDto {
    private int id;
    private String username;
    private String name;
    private String imageUrl;
}
