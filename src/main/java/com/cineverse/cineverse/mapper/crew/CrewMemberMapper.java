package com.cineverse.cineverse.mapper.crew;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.ContentCast;
import com.cineverse.cineverse.domain.entity.CrewMember;
import com.cineverse.cineverse.domain.entity.CrewMemberSocial;
import com.cineverse.cineverse.dto.content.ContentCastDto;
import com.cineverse.cineverse.dto.crew.CrewMemberDto;
import com.cineverse.cineverse.dto.crew.CrewMemberSocialDto;
import com.cineverse.cineverse.dto.crew.DirectorDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CrewMemberMapper {
    private final TMDBApiConfiguration tmdbApiConfiguration;
    private final String facebookUrl = "https://www.facebook.com/";
    private final String instagramUrl = "https://www.instagram.com/";
    private final String twitterUrl = "https://twitter.com/";
    private final String tiktokUrl = "https://www.tiktok.com/@";
    private final String youtubeUrl = "https://www.youtube.com/";

    public CrewMemberDto toCrewMemberDto(CrewMember crewMember) {
        if (crewMember == null) return null;
        return new CrewMemberDto(
                crewMember.getId(),
                crewMember.getName(),
                fullPath(crewMember.getProfilePath()),
                crewMember.getBiography(),
                crewMember.getBirthday(),
                crewMember.getDeathday(),
                crewMember.getPlaceOfBirth(),
                crewMember.getKnownForDepartment(),
                crewMember.getAlsoKnownAs()
        );
    }
    public DirectorDto toDirectorDto(CrewMember crewMember) {
        if (crewMember == null) return null;
        return new DirectorDto(crewMember.getId(), crewMember.getName(),
                fullPath(crewMember.getProfilePath()));
    }

    public List<ContentCastDto> toContentCastDto(List<ContentCast> contentCast) {
        if (contentCast == null) return List.of();
        return contentCast.stream()
                .map(c -> new ContentCastDto(c.getCast().getId(), c.getCharacterName(), c.getCast().getName(),
                        fullPath(c.getCast().getProfilePath())))
                .toList();
    }
    public CrewMemberSocialDto toCrewMemberSocialDto(CrewMemberSocial crewMemberSocial) {
        if (crewMemberSocial == null) return null;

        return new CrewMemberSocialDto(
                crewMemberSocial.getFacebookId() != null ? facebookUrl + crewMemberSocial.getFacebookId() : null,
                crewMemberSocial.getInstagramId() != null ? instagramUrl + crewMemberSocial.getInstagramId() : null,
                crewMemberSocial.getTwitterId() != null ? twitterUrl + crewMemberSocial.getTwitterId() : null,
                crewMemberSocial.getTiktokId() != null ? tiktokUrl + crewMemberSocial.getTiktokId() : null,
                crewMemberSocial.getYoutubeId() != null ? youtubeUrl + crewMemberSocial.getYoutubeId() : null
        );
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }
}
