package com.zjutennis.repository;

import com.zjutennis.model.VideoAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for VideoAnalysis entity
 */
@Repository
public interface VideoAnalysisRepository extends JpaRepository<VideoAnalysis, Long> {

    /**
     * Find all analyses for a specific video
     * @param videoId Video ID
     * @return List of analyses
     */
    List<VideoAnalysis> findByVideoId(Long videoId);

    /**
     * Find all analyses for a specific player
     * @param playerId Player ID
     * @return List of analyses
     */
    List<VideoAnalysis> findByPlayerId(Long playerId);

    /**
     * Find analysis for a specific video and player
     * @param videoId Video ID
     * @param playerId Player ID
     * @return Optional VideoAnalysis
     */
    Optional<VideoAnalysis> findByVideoIdAndPlayerId(Long videoId, Long playerId);

    /**
     * Check if analysis exists for video and player
     * @param videoId Video ID
     * @param playerId Player ID
     * @return true if exists
     */
    boolean existsByVideoIdAndPlayerId(Long videoId, Long playerId);

    /**
     * Delete all analyses for a specific video
     * @param videoId Video ID
     */
    void deleteByVideoId(Long videoId);

    /**
     * Delete all analyses for a specific player
     * @param playerId Player ID
     */
    void deleteByPlayerId(Long playerId);

    /**
     * Count analyses for a specific video
     * @param videoId Video ID
     * @return Count
     */
    long countByVideoId(Long videoId);

    /**
     * Count analyses for a specific player
     * @param playerId Player ID
     * @return Count
     */
    long countByPlayerId(Long playerId);

    /**
     * Find all AI-analyzed videos for a player
     * @param playerId Player ID
     * @param aiAnalyzed AI analyzed status
     * @return List of analyses
     */
    List<VideoAnalysis> findByPlayerIdAndAiAnalyzed(Long playerId, Boolean aiAnalyzed);
}
