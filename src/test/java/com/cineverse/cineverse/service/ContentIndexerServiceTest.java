package com.cineverse.cineverse.service;

import com.cineverse.cineverse.domain.document.ContentDocument;
import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.enums.ContentType;
import com.cineverse.cineverse.mapper.ContentMapper;
import com.cineverse.cineverse.repository.ContentDocumentRepository;
import com.cineverse.cineverse.repository.ContentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentIndexerServiceTest {

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private ContentDocumentRepository documentRepository;

    @Mock
    private ContentMapper contentMapper;

    @InjectMocks
    private ContentIndexerService contentIndexerService;

    private Content mockContent;
    private ContentDocument mockDocument;

    @BeforeEach
    void setUp() {
        mockContent = new Content();
        mockContent.setId(1);

        mockDocument = new ContentDocument();
        mockDocument.setId("1");
    }

    @Test
    void reindexAll_shouldFetchMapAndSaveAllDocuments_whenServerStarts() {
        when(contentRepository.findByContentTypeIn(List.of(ContentType.MOVIE, ContentType.SERIES)))
                .thenReturn(List.of(mockContent));
        when(contentMapper.toContentDocument(mockContent)).thenReturn(mockDocument);

        contentIndexerService.reindexAll();

        verify(contentRepository, times(1))
                .findByContentTypeIn(List.of(ContentType.MOVIE, ContentType.SERIES));
        verify(contentMapper, times(1)).toContentDocument(mockContent);
        verify(documentRepository, times(1)).saveAll(List.of(mockDocument));
    }

}
