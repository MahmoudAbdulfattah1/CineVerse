package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.CrewMember;
import com.cineverse.cineverse.dto.CrewMemberDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CrewMemberMapper {
    private final TMDBApiConfiguration tmdbApiConfiguration;

    public CrewMemberDto toDto(CrewMember crewMember) {
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

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }
}
