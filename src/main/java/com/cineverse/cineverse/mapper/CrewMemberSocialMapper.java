package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.domain.entity.CrewMemberSocial;
import com.cineverse.cineverse.dto.CrewMemberSocialDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CrewMemberSocialMapper {
    private final String facebookUrl = "https://www.facebook.com/";
    private final String instagramUrl = "https://www.instagram.com/";
    private final String twitterUrl = "https://twitter.com/";
    private final String tiktokUrl = "https://www.tiktok.com/@";
    private final String youtubeUrl = "https://www.youtube.com/";

    public CrewMemberSocialDto toDto(CrewMemberSocial crewMemberSocial) {
        if (crewMemberSocial == null) return null;

        return new CrewMemberSocialDto(
                crewMemberSocial.getFacebookId() != null ? facebookUrl + crewMemberSocial.getFacebookId() : null,
                crewMemberSocial.getInstagramId() != null ? instagramUrl + crewMemberSocial.getInstagramId() : null,
                crewMemberSocial.getTwitterId() != null ? twitterUrl + crewMemberSocial.getTwitterId() : null,
                crewMemberSocial.getTiktokId() != null ? tiktokUrl + crewMemberSocial.getTiktokId() : null,
                crewMemberSocial.getYoutubeId() != null ? youtubeUrl + crewMemberSocial.getYoutubeId() : null
        );
    }

}
