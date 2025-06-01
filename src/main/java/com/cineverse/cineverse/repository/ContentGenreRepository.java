package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.ContentGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentGenreRepository extends JpaRepository<ContentGenre, Integer> {

}
