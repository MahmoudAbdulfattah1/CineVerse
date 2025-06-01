package com.cineverse.cineverse.service.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.dto.ContentCastDto;
import com.cineverse.cineverse.dto.DirectorDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class CreditMapper {

    private final TMDBApiConfiguration tmdbApiConfiguration;

    public DirectorDto map(DirectorDto dto) {
        if (dto == null) return null;
        dto.setPath(fullPath(dto.getPath()));
        return dto;
    }

    public List<ContentCastDto> map(List<ContentCastDto> castDtos) {
        if (castDtos == null) return List.of();
        castDtos.forEach(c -> c.setPath(fullPath(c.getPath())));
        return castDtos;
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }
}