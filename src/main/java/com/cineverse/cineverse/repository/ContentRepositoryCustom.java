package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.enums.ContentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContentRepositoryCustom {

    Page<Content> filterContent(List<String> genres, Integer year, Integer rate, ContentType contentType,
                                String language, String sortBy, Pageable pageable);

    Page<Content> searchContent(String keyword, Pageable pageable);

}
