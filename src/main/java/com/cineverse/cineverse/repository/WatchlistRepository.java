package com.cineverse.cineverse.repository;

import com.cineverse.cineverse.domain.entity.Content;
import com.cineverse.cineverse.domain.entity.User;
import com.cineverse.cineverse.domain.entity.Watchlist;
import com.cineverse.cineverse.domain.enums.WatchingStatus;
import com.cineverse.cineverse.dto.watchlist.WatchlistProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {
    @Query("""
                SELECT
                    w.id AS id,
                    c.id AS contentId,
                    c.title AS title,
                    c.overview AS overview,
                    c.posterPath AS contentPoster,
                    c.contentType AS contentType,
                    c.imdbRate AS imdbRate,
                    w.watchingStatus AS watchingStatus,
                    w.createdAt AS createdAt,
                    w.updatedAt AS updatedAt
                FROM Watchlist w
                JOIN w.content c
                WHERE w.user.id = :userId AND w.watchingStatus = :watchingStatus
                ORDER BY w.updatedAt DESC
            """)
    Page<WatchlistProjection> findWatchlistByUserIdAndStatus(
            @Param("userId") int userId,
            @Param("watchingStatus") WatchingStatus watchingStatus,
            Pageable pageable
    );


    boolean existsByUserId(int userId);

    boolean existsByUserAndContent(User user, Content content);

    boolean existsByUserIdAndContentId(int userId, int contentId);


}
