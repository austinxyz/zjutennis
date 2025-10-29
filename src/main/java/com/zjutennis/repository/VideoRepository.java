package com.zjutennis.repository;

import com.zjutennis.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Video entity
 */
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    /**
     * Find video by match ID
     * @param matchId Match ID
     * @return Optional Video
     */
    Optional<Video> findByMatchId(Long matchId);

    /**
     * Check if video exists for a match
     * @param matchId Match ID
     * @return true if exists
     */
    boolean existsByMatchId(Long matchId);

    /**
     * Delete video by match ID
     * @param matchId Match ID
     */
    void deleteByMatchId(Long matchId);
}
