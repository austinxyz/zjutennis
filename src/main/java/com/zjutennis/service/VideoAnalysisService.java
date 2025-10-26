package com.zjutennis.service;

import com.zjutennis.model.Player;
import com.zjutennis.model.VideoAnalysis;
import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.repository.VideoAnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing video analysis operations
 */
@Service
@Slf4j
public class VideoAnalysisService {

    @Autowired
    private VideoAnalysisRepository videoAnalysisRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Get all videos across all players
     */
    public List<VideoAnalysis> getAllVideos() {
        log.debug("Fetching all videos across all players");
        return videoAnalysisRepository.findAllByOrderByMatchDateDesc();
    }

    /**
     * Get all videos for a specific player
     */
    public List<VideoAnalysis> getPlayerVideos(Long playerId) {
        log.debug("Fetching all videos for player ID: {}", playerId);
        return videoAnalysisRepository.findByPlayerIdOrderByMatchDateDesc(playerId);
    }

    /**
     * Get a specific video by ID
     */
    public Optional<VideoAnalysis> getVideoById(Long id) {
        log.debug("Fetching video with ID: {}", id);
        return videoAnalysisRepository.findById(id);
    }

    /**
     * Create a new video analysis entry
     */
    @Transactional
    public VideoAnalysis createVideo(Long playerId, VideoAnalysis videoAnalysis) {
        log.debug("Creating new video for player ID: {}", playerId);

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        videoAnalysis.setPlayer(player);
        videoAnalysis.setUploadDate(LocalDateTime.now());
        videoAnalysis.setStatus("pending");
        videoAnalysis.setAiAnalyzed(false);

        return videoAnalysisRepository.save(videoAnalysis);
    }

    /**
     * Update an existing video analysis entry
     */
    @Transactional
    public VideoAnalysis updateVideo(Long id, VideoAnalysis updatedVideo) {
        log.debug("Updating video with ID: {}", id);

        return videoAnalysisRepository.findById(id)
                .map(video -> {
                    // Update basic information
                    if (updatedVideo.getTitle() != null) {
                        video.setTitle(updatedVideo.getTitle());
                    }
                    if (updatedVideo.getDescription() != null) {
                        video.setDescription(updatedVideo.getDescription());
                    }
                    if (updatedVideo.getVideoUrl() != null) {
                        video.setVideoUrl(updatedVideo.getVideoUrl());
                    }
                    if (updatedVideo.getThumbnailUrl() != null) {
                        video.setThumbnailUrl(updatedVideo.getThumbnailUrl());
                    }
                    if (updatedVideo.getMatchDate() != null) {
                        video.setMatchDate(updatedVideo.getMatchDate());
                    }

                    // Update match statistics
                    if (updatedVideo.getTotalShots() != null) {
                        video.setTotalShots(updatedVideo.getTotalShots());
                    }
                    if (updatedVideo.getErrors() != null) {
                        video.setErrors(updatedVideo.getErrors());
                    }
                    if (updatedVideo.getWinners() != null) {
                        video.setWinners(updatedVideo.getWinners());
                    }
                    if (updatedVideo.getAces() != null) {
                        video.setAces(updatedVideo.getAces());
                    }
                    if (updatedVideo.getDoubleFaults() != null) {
                        video.setDoubleFaults(updatedVideo.getDoubleFaults());
                    }
                    if (updatedVideo.getRunningDistanceMeters() != null) {
                        video.setRunningDistanceMeters(updatedVideo.getRunningDistanceMeters());
                    }

                    // Update AI analysis results if provided
                    if (updatedVideo.getStrengthSummary() != null) {
                        video.setStrengthSummary(updatedVideo.getStrengthSummary());
                    }
                    if (updatedVideo.getWeaknessSummary() != null) {
                        video.setWeaknessSummary(updatedVideo.getWeaknessSummary());
                    }
                    if (updatedVideo.getTacticalSummary() != null) {
                        video.setTacticalSummary(updatedVideo.getTacticalSummary());
                    }
                    if (updatedVideo.getAiRecommendations() != null) {
                        video.setAiRecommendations(updatedVideo.getAiRecommendations());
                    }

                    // Update status
                    if (updatedVideo.getStatus() != null) {
                        video.setStatus(updatedVideo.getStatus());
                    }

                    return videoAnalysisRepository.save(video);
                })
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
    }

    /**
     * Delete a video analysis entry
     */
    @Transactional
    public void deleteVideo(Long id) {
        log.debug("Deleting video with ID: {}", id);
        videoAnalysisRepository.deleteById(id);
    }

    /**
     * Mark video as analyzed by AI
     */
    @Transactional
    public VideoAnalysis markAsAnalyzed(Long id) {
        log.debug("Marking video as analyzed: {}", id);

        return videoAnalysisRepository.findById(id)
                .map(video -> {
                    video.setAiAnalyzed(true);
                    video.setAiAnalysisDate(LocalDateTime.now());
                    video.setStatus("completed");
                    return videoAnalysisRepository.save(video);
                })
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
    }

    /**
     * Get all analyzed videos for a player
     */
    public List<VideoAnalysis> getAnalyzedVideos(Long playerId) {
        log.debug("Fetching analyzed videos for player ID: {}", playerId);
        return videoAnalysisRepository.findByPlayerIdAndAiAnalyzedTrue(playerId);
    }

    /**
     * Get count of videos for a player
     */
    public long getPlayerVideoCount(Long playerId) {
        return videoAnalysisRepository.countByPlayerId(playerId);
    }

    /**
     * Get count of analyzed videos for a player
     */
    public long getPlayerAnalyzedVideoCount(Long playerId) {
        return videoAnalysisRepository.countAnalyzedVideosByPlayerId(playerId);
    }

    /**
     * Update AI analysis results for a video
     */
    @Transactional
    public VideoAnalysis updateAIAnalysis(Long id, VideoAnalysis aiResults) {
        log.debug("Updating AI analysis for video ID: {}", id);

        return videoAnalysisRepository.findById(id)
                .map(video -> {
                    // Update strength scores
                    video.setStrengthForehandScore(aiResults.getStrengthForehandScore());
                    video.setStrengthServeScore(aiResults.getStrengthServeScore());
                    video.setStrengthVolleyScore(aiResults.getStrengthVolleyScore());
                    video.setStrengthMovementScore(aiResults.getStrengthMovementScore());
                    video.setStrengthSummary(aiResults.getStrengthSummary());

                    // Update weakness scores
                    video.setWeaknessBackhandScore(aiResults.getWeaknessBackhandScore());
                    video.setWeaknessConsistencyScore(aiResults.getWeaknessConsistencyScore());
                    video.setWeaknessPressureScore(aiResults.getWeaknessPressureScore());
                    video.setWeaknessSummary(aiResults.getWeaknessSummary());

                    // Update tactical analysis
                    video.setTacticalStyle(aiResults.getTacticalStyle());
                    video.setAggressionIndex(aiResults.getAggressionIndex());
                    video.setNetApproachFrequency(aiResults.getNetApproachFrequency());
                    video.setBaselineRallyPreference(aiResults.getBaselineRallyPreference());
                    video.setTacticalSummary(aiResults.getTacticalSummary());

                    // Update recommendations
                    video.setAiRecommendations(aiResults.getAiRecommendations());
                    video.setTrainingFocusAreas(aiResults.getTrainingFocusAreas());

                    // Update keyframes
                    video.setKeyframesJson(aiResults.getKeyframesJson());

                    // Mark as analyzed
                    video.setAiAnalyzed(true);
                    video.setAiAnalysisDate(LocalDateTime.now());
                    video.setStatus("completed");

                    return videoAnalysisRepository.save(video);
                })
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
    }
}
