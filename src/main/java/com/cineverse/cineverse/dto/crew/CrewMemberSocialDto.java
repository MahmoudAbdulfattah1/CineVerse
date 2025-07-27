package com.cineverse.cineverse.dto.crew;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewMemberSocialDto {
    private String facebookUrl;
    private String instagramUrl;
    private String twitterUrl;
    private String tiktokUrl;
    private String youtubeUrl;
}
