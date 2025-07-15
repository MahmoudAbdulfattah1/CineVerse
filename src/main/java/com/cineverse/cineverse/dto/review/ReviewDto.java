package com.cineverse.cineverse.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewDto {
    private UserDto user;
    private int id;
    private int rate;
    private String title;
    private String content;
    private long likes;
    private long dislikes;
    private boolean spoiler;
    private LocalDate date;

    public ReviewDto(UserDto user, int id, int rate, String title, String content, long likes, long dislikes, boolean spoiler, LocalDate date) {
        this.user = user;
        this.id = id;
        this.rate = rate;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.dislikes = dislikes;
        this.spoiler = spoiler;
        this.date = date;
    }
}
