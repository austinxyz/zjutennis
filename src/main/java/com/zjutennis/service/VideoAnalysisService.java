package com.zjutennis.service;

import com.zjutennis.dto.VideoAnalysisRequest;
import com.zjutennis.dto.VideoAnalysisResponse;
import com.zjutennis.model.Player;
import com.zjutennis.model.Video;
import com.zjutennis.model.VideoAnalysis;
import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.repository.VideoAnalysisRepository;
import com.zjutennis.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for VideoAnalysis operations
 */
@Service
@Transactional
public class VideoAnalysisService {

    @Autowired
    private VideoAnalysisRepository videoAnalysisRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * Get all video analyses
     */
    public List<VideoAnalysisResponse> getAllAnalyses() {
        return videoAnalysisRepository.findAll().stream()
                .map(VideoAnalysisResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get analysis by ID
     */
    public VideoAnalysisResponse getAnalysisById(Long id) {
        VideoAnalysis analysis = videoAnalysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video analysis not found with id: " + id));
        return VideoAnalysisResponse.fromEntity(analysis);
    }

    /**
     * Get all analyses for a video
     */
    public List<VideoAnalysisResponse> getAnalysesByVideoId(Long videoId) {
        return videoAnalysisRepository.findByVideoId(videoId).stream()
                .map(VideoAnalysisResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get all analyses for a player
     */
    public List<VideoAnalysisResponse> getAnalysesByPlayerId(Long playerId) {
        return videoAnalysisRepository.findByPlayerId(playerId).stream()
                .map(VideoAnalysisResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get analysis for specific video and player
     */
    public VideoAnalysisResponse getAnalysisByVideoAndPlayer(Long videoId, Long playerId) {
        VideoAnalysis analysis = videoAnalysisRepository.findByVideoIdAndPlayerId(videoId, playerId)
                .orElseThrow(() -> new RuntimeException(
                        "Video analysis not found for video: " + videoId + " and player: " + playerId));
        return VideoAnalysisResponse.fromEntity(analysis);
    }

    /**
     * Create video analysis
     */
    public VideoAnalysisResponse createAnalysis(VideoAnalysisRequest request) {
        if (request.getVideoId() == null) {
            throw new RuntimeException("Video ID is required");
        }
        if (request.getPlayerId() == null) {
            throw new RuntimeException("Player ID is required");
        }

        // Check if video exists
        Video video = videoRepository.findById(request.getVideoId())
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + request.getVideoId()));

        // Check if player exists
        Player player = playerRepository.findById(request.getPlayerId())
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + request.getPlayerId()));

        // Check if analysis already exists for this video and player
        if (videoAnalysisRepository.existsByVideoIdAndPlayerId(request.getVideoId(), request.getPlayerId())) {
            throw new RuntimeException("Analysis already exists for video: " + request.getVideoId()
                    + " and player: " + request.getPlayerId());
        }

        VideoAnalysis analysis = new VideoAnalysis();
        analysis.setVideo(video);
        analysis.setPlayer(player);
        updateAnalysisFromRequest(analysis, request);

        VideoAnalysis saved = videoAnalysisRepository.save(analysis);
        return VideoAnalysisResponse.fromEntity(saved);
    }

    /**
     * Update video analysis
     */
    public VideoAnalysisResponse updateAnalysis(Long id, VideoAnalysisRequest request) {
        VideoAnalysis analysis = videoAnalysisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video analysis not found with id: " + id));

        updateAnalysisFromRequest(analysis, request);

        VideoAnalysis updated = videoAnalysisRepository.save(analysis);
        return VideoAnalysisResponse.fromEntity(updated);
    }

    /**
     * Delete video analysis
     */
    public void deleteAnalysis(Long id) {
        if (!videoAnalysisRepository.existsById(id)) {
            throw new RuntimeException("Video analysis not found with id: " + id);
        }
        videoAnalysisRepository.deleteById(id);
    }

    /**
     * Delete all analyses for a video
     */
    public void deleteAnalysesByVideoId(Long videoId) {
        videoAnalysisRepository.deleteByVideoId(videoId);
    }

    /**
     * Get analysis count for a video
     */
    public long getAnalysisCountForVideo(Long videoId) {
        return videoAnalysisRepository.countByVideoId(videoId);
    }

    /**
     * Get analysis count for a player
     */
    public long getAnalysisCountForPlayer(Long playerId) {
        return videoAnalysisRepository.countByPlayerId(playerId);
    }

    /**
     * Helper method to update analysis from request
     */
    private void updateAnalysisFromRequest(VideoAnalysis analysis, VideoAnalysisRequest request) {
        if (request.getAiAnalyzed() != null) {
            analysis.setAiAnalyzed(request.getAiAnalyzed());
            if (request.getAiAnalyzed()) {
                analysis.setAiAnalysisDate(LocalDateTime.now());
            }
        }

        analysis.setStrengthForehandScore(request.getStrengthForehandScore());
        analysis.setStrengthServeScore(request.getStrengthServeScore());
        analysis.setStrengthVolleyScore(request.getStrengthVolleyScore());
        analysis.setStrengthMovementScore(request.getStrengthMovementScore());
        analysis.setStrengthSummary(request.getStrengthSummary());

        analysis.setWeaknessBackhandScore(request.getWeaknessBackhandScore());
        analysis.setWeaknessConsistencyScore(request.getWeaknessConsistencyScore());
        analysis.setWeaknessPressureScore(request.getWeaknessPressureScore());
        analysis.setWeaknessSummary(request.getWeaknessSummary());

        analysis.setTacticalStyle(request.getTacticalStyle());
        analysis.setAggressionIndex(request.getAggressionIndex());
        analysis.setNetApproachFrequency(request.getNetApproachFrequency());
        analysis.setBaselineRallyPreference(request.getBaselineRallyPreference());
        analysis.setTacticalSummary(request.getTacticalSummary());

        analysis.setAiRecommendations(request.getAiRecommendations());
        analysis.setTrainingFocusAreas(request.getTrainingFocusAreas());
        analysis.setKeyframesJson(request.getKeyframesJson());

        if (request.getStatus() != null) {
            analysis.setStatus(request.getStatus());
        }
        analysis.setProcessingNotes(request.getProcessingNotes());
    }
}
