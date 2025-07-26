package com.cineverse.cineverse.dto.review;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewerDto {
    private int id;
    private String username;
    private String name;
    private String imageUrl;

    public ReviewerDto(int id, String username, String name, String imageUrl) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
