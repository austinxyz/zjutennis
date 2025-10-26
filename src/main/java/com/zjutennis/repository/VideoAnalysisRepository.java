package com.zjutennis.repository;

import com.zjutennis.model.VideoAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for VideoAnalysis entity
 */
@Repository
public interface VideoAnalysisRepository extends JpaRepository<VideoAnalysis, Long> {

    /**
     * Find all videos across all players ordered by match date
     */
    List<VideoAnalysis> findAllByOrderByMatchDateDesc();

    /**
     * Find all videos for a specific player
     */
    List<VideoAnalysis> findByPlayerIdOrderByMatchDateDesc(Long playerId);

    /**
     * Find all videos for a specific player with a specific status
     */
    List<VideoAnalysis> findByPlayerIdAndStatus(Long playerId, String status);

    /**
     * Find all analyzed videos for a player
     */
    List<VideoAnalysis> findByPlayerIdAndAiAnalyzedTrue(Long playerId);

    /**
     * Find pending videos that need AI analysis
     */
    List<VideoAnalysis> findByAiAnalyzedFalseAndStatus(String status);

    /**
     * Count total videos for a player
     */
    long countByPlayerId(Long playerId);

    /**
     * Count analyzed videos for a player
     */
    @Query("SELECT COUNT(v) FROM VideoAnalysis v WHERE v.player.id = :playerId AND v.aiAnalyzed = true")
    long countAnalyzedVideosByPlayerId(@Param("playerId") Long playerId);
}
