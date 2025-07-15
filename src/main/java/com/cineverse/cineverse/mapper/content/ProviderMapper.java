package com.cineverse.cineverse.mapper.content;

import com.cineverse.cineverse.configuration.TMDBApiConfiguration;
import com.cineverse.cineverse.domain.entity.Provider;
import com.cineverse.cineverse.dto.content.ProviderDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProviderMapper {

    private final TMDBApiConfiguration tmdbApiConfiguration;

    public List<ProviderDto> map(List<Provider> providers) {
        if (providers == null) return List.of();
        return providers.stream()
                .map(p -> new ProviderDto(p.getName(), fullPath(p.getLogo())))
                .toList();
    }

    private String fullPath(String path) {
        if (path == null || path.isBlank()) return null;
        return tmdbApiConfiguration.getBaseImageUrl() + path;
    }
}