package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.ContentCast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentCastRepository extends JpaRepository<ContentCast, Integer> {

}
