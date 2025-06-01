package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.ContentProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentProviderRepository extends JpaRepository<ContentProvider, Integer> {

}
