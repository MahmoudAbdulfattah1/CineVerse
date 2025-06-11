package com.cineverse.cineverse.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.ContentCast;
import com.cineverse.cineverse.domain.entity.CrewMember;
import com.cineverse.cineverse.dto.ContentCastDto;
import com.cineverse.cineverse.dto.DirectorDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CreditMapper {

    private final TMDBApiConfiguration tmdbApiConfiguration;

    public DirectorDto mapDirector(CrewMember crewMember) {
        if (crewMember == null) return null;
        return new DirectorDto(crewMember.getId(), crewMember.getName(),
                fullPath(crewMember.getProfilePath()));
    }

    public List<ContentCastDto> mapContentCast(List<ContentCast> contentCast) {
        if (contentCast == null) return List.of();
        return contentCast.stream()
                .map(c -> new ContentCastDto(c.getCast().getId(), c.getCharacterName(), c.getCast().getName(),
                        fullPath(c.getCast().getProfilePath())))
                .toList();
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }
}