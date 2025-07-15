package com.cineverse.cineverse.dto.crew;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrewMemberSocialDto {
    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String tiktokUrl;
    private String youtubeUrl;


    public CrewMemberSocialDto(String facebookUrl, String instagramUrl, String twitterUrl, String tiktokUrl,
                               String youtubeUrl) {
        this.facebookUrl = facebookUrl;
        this.instagramUrl = instagramUrl;
        this.twitterUrl = twitterUrl;
        this.tiktokUrl = tiktokUrl;
        this.youtubeUrl = youtubeUrl;
    }

}
