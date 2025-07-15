package com.cineverse.cineverse.dto.content;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TrailerDto {
    private String trailer;

    public TrailerDto(String trailer) {
        this.trailer = trailer == null ? null : "https://www.youtube.com/watch?v=" + trailer;
    }
}
