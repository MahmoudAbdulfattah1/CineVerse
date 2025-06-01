package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.dto.ContentMetaDataDto;

import java.util.List;

public interface ContentRepositoryCustom {
    List<ContentMetaDataDto> filterContent(List<String> genres, Integer year, Integer rate, ContentType contentType, String language, String sortBy);

    List<ContentMetaDataDto> searchContent(String keyword);
}
