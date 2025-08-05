package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.document.ContentDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentDocumentRepository extends ElasticsearchRepository<ContentDocument, String> {
    @Query("""
    {
      "query": {
        "bool": {
          "should": [
            {
              "multi_match": {
                "query": ?0,
                "fields": ["title^4", "overview"],
                "type": "best_fields",
                "fuzziness": "2",
                "prefix_length": 0,
                "max_expansions": 100
              }
            },
            {
              "multi_match": {
                "query": ?0,
                "fields": ["title^3", "overview"],
                "type": "most_fields",
                "fuzziness": "AUTO"
              }
            },
            {
              "match": {
                "title": {
                  "query": ?0,
                  "fuzziness": "2",
                  "boost": 2
                }
              }
            }
          ]
        }
      }
    }
    """)
    Page<ContentDocument> searchByTitle(String keyword, Pageable pageable);
}

