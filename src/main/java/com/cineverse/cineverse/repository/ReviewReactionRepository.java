package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Review;
import com.cineverse.cineverse.domain.entity.ReviewReaction;
import com.cineverse.cineverse.domain.enums.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewReactionRepository extends JpaRepository<ReviewReaction, Integer> {

}
