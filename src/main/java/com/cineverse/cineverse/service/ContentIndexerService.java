package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.document.ContentDocument;
import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.mapper.ContentMapper;
import com.cineverse.cineverse.repository.ContentRepository;
import com.cineverse.cineverse.repository.ContentDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentIndexerService {

    private final ContentRepository contentRepository;
    private final ContentDocumentRepository documentRepository;
    private final ContentMapper contentMapper;

    @Transactional(readOnly = true)
    public void reindexAll() {
        List<Content> contents = contentRepository.findByContentTypeIn(List.of(ContentType.MOVIE, ContentType.SERIES));
        log.info("Reindexing {} contents to Elasticsearch", contents.size());
        List<ContentDocument> documents = contents.stream()
                .map(contentMapper::toContentDocument)
                .toList();
        documentRepository.saveAll(documents);
        log.info("Finished indexing {} documents", documents.size());
    }

    public void indexSingle(Content content) {
        ContentDocument document = contentMapper.toContentDocument(content);
        documentRepository.save(document);
    }

    public void deleteFromIndex(int contentId) {
        documentRepository.deleteById(String.valueOf(contentId));
    }
}

