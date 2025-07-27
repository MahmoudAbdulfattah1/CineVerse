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
        return CrewMemberDto.builder()
                .id(crewMember.getId())
                .name(crewMember.getName())
                .imageUrl(fullPath(crewMember.getProfilePath()))
                .biography(crewMember.getBiography())
                .birthday(crewMember.getBirthday())
                .deathday(crewMember.getDeathday())
                .placeOfBirth(crewMember.getPlaceOfBirth())
                .knownForDepartment(crewMember.getKnownForDepartment())
                .alsoKnownAs(crewMember.getAlsoKnownAs())
                .build();
    }

    public DirectorDto toDirectorDto(CrewMember crewMember) {
        return crewMember == null ? null : DirectorDto.builder()
                .id(crewMember.getId())
                .name(crewMember.getName())
                .imageUrl(fullPath(crewMember.getProfilePath()))
                .build();
    }

    public ContentCastDto toContentCastDto(ContentCast contentCast) {
        return ContentCastDto.builder()
                .id(contentCast.getCast().getId())
                .characterName(contentCast.getCharacterName())
                .name(contentCast.getCast().getName())
                .imageUrl(fullPath(contentCast.getCast().getProfilePath()))
                .build();
    }

    public CrewMemberSocialDto toCrewMemberSocialDto(CrewMemberSocial crewMemberSocial) {
        if (crewMemberSocial == null) return null;

        return CrewMemberSocialDto.builder()
                .facebookUrl(crewMemberSocial.getFacebookId() != null ?
                        facebookUrl + crewMemberSocial.getFacebookId() : null)
                .instagramUrl(crewMemberSocial.getInstagramId() != null ?
                        instagramUrl + crewMemberSocial.getInstagramId() : null)
                .twitterUrl(crewMemberSocial.getTwitterId() != null ?
                        twitterUrl + crewMemberSocial.getTwitterId() : null)
                .tiktokUrl(crewMemberSocial.getTiktokId() != null ?
                        tiktokUrl + crewMemberSocial.getTiktokId() : null)
                .youtubeUrl(crewMemberSocial.getYoutubeId() != null ?
                        youtubeUrl + crewMemberSocial.getYoutubeId() : null)
                .build();
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }
}
