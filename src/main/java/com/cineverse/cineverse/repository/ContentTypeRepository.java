package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentTypeRepository extends JpaRepository<Content, Integer> {
    @Query("SELECT c.contentType FROM Content c WHERE c.id = :id")
    Optional<ContentType> findContentTypeById(@Param("id") int id);
}