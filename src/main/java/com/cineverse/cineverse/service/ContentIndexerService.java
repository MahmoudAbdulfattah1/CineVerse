package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.document.ContentDocument;
import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.mapper.ContentMapper;
import com.cineverse.cineverse.repository.ContentRepository;
import com.cineverse.cineverse.repository.ContentDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentIndexerService {

    private final ContentRepository contentRepository;
    private final ContentDocumentRepository documentRepository;
    private final ContentMapper contentMapper;

    /**
     * Reindexes all content of type {@link ContentType#MOVIE} and {@link ContentType#SERIES} for Elasticsearch.
     * <p>
     * This method loads the matching entities from the relational database,
     * maps them to {@link ContentDocument} instances, and saves them in bulk to Elasticsearch.
     * </p>
     * The operation is read-only on the relational database.
     */
    @Transactional(readOnly = true)
    public void reindexAll() {
        List<Content> contents = contentRepository.findByContentTypeIn(List.of(ContentType.MOVIE, ContentType.SERIES));
        List<ContentDocument> documents = contents.stream()
                .map(contentMapper::toContentDocument)
                .toList();
        documentRepository.saveAll(documents);
    }

}

