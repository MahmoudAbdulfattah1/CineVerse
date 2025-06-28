package com.cineverse.cineverse.infrastructure.youtube;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Video {
    String videoId;
    String title;
    String description;
    String channelTitle;

    public Video(String videoId, String title, String description, String channelTitle) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
        this.channelTitle = channelTitle;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId='" + videoId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", channelTitle='" + channelTitle + '\'' +
                '}';
    }
}
