package com.cineverse.cineverse.service.mapper;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.dto.ProviderDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProviderMapper {

    private final TMDBApiConfiguration tmdbApiConfiguration;

    public List<ProviderDto> map(List<ProviderDto> providerDtos) {
        if (providerDtos == null) return List.of();
        providerDtos.forEach(c -> c.setLogo(fullPath(c.getLogo())));
        return providerDtos;
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }
}