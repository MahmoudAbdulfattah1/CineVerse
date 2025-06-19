package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Integer> {
    @Query("""
           SELECT cm 
           FROM CrewMember cm 
           JOIN FETCH cm.alsoKnownAs 
           WHERE cm.id = :id
            """)
    Optional<CrewMember> findByIdWithAliases(@Param("id") int id);

}
