package com.cineverse.cineverse.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewerDto {
    private int userId;
    private String username;
    private String name;
    private String imageUrl;

    public ReviewerDto(int userId, String username, String name, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
