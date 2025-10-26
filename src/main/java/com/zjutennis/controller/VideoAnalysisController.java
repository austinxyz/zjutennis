package com.zjutennis.controller;

import com.zjutennis.model.VideoAnalysis;
import com.zjutennis.service.VideoAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Video Analysis operations
 * Provides endpoints for managing player match videos and AI analysis results
 */
@RestController
@RequestMapping("/api/video-analysis")
@CrossOrigin(origins = "*")
@Slf4j
public class VideoAnalysisController {

    @Autowired
    private VideoAnalysisService videoAnalysisService;

    /**
     * Get all videos across all players
     * GET /api/video-analysis
     */
    @GetMapping
    public ResponseEntity<List<VideoAnalysis>> getAllVideos() {
        log.info("GET /api/video-analysis - Fetching all videos");
        List<VideoAnalysis> videos = videoAnalysisService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    /**
     * Get all videos for a specific player
     * GET /api/video-analysis/player/{playerId}
     */
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<VideoAnalysis>> getPlayerVideos(@PathVariable Long playerId) {
        log.info("GET /api/video-analysis/player/{} - Fetching videos for player", playerId);
        List<VideoAnalysis> videos = videoAnalysisService.getPlayerVideos(playerId);
        return ResponseEntity.ok(videos);
    }

    /**
     * Get a specific video by ID
     * GET /api/video-analysis/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VideoAnalysis> getVideoById(@PathVariable Long id) {
        log.info("GET /api/video-analysis/{} - Fetching video by id", id);
        return videoAnalysisService.getVideoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new video analysis entry
     * POST /api/video-analysis/player/{playerId}
     */
    @PostMapping("/player/{playerId}")
    public ResponseEntity<VideoAnalysis> createVideo(
            @PathVariable Long playerId,
            @RequestBody VideoAnalysis videoAnalysis) {
        log.info("POST /api/video-analysis/player/{} - Creating new video", playerId);
        try {
            VideoAnalysis created = videoAnalysisService.createVideo(playerId, videoAnalysis);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            log.error("Error creating video for player {}: {}", playerId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update an existing video analysis entry
     * PUT /api/video-analysis/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<VideoAnalysis> updateVideo(
            @PathVariable Long id,
            @RequestBody VideoAnalysis videoAnalysis) {
        log.info("PUT /api/video-analysis/{} - Updating video", id);
        try {
            VideoAnalysis updated = videoAnalysisService.updateVideo(id, videoAnalysis);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Error updating video {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a video analysis entry
     * DELETE /api/video-analysis/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        log.info("DELETE /api/video-analysis/{} - Deleting video", id);
        try {
            videoAnalysisService.deleteVideo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting video {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get all analyzed videos for a player
     * GET /api/video-analysis/player/{playerId}/analyzed
     */
    @GetMapping("/player/{playerId}/analyzed")
    public ResponseEntity<List<VideoAnalysis>> getAnalyzedVideos(@PathVariable Long playerId) {
        log.info("GET /api/video-analysis/player/{}/analyzed - Fetching analyzed videos", playerId);
        List<VideoAnalysis> videos = videoAnalysisService.getAnalyzedVideos(playerId);
        return ResponseEntity.ok(videos);
    }

    /**
     * Get video statistics for a player
     * GET /api/video-analysis/player/{playerId}/stats
     */
    @GetMapping("/player/{playerId}/stats")
    public ResponseEntity<Map<String, Long>> getPlayerVideoStats(@PathVariable Long playerId) {
        log.info("GET /api/video-analysis/player/{}/stats - Fetching video stats", playerId);
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalVideos", videoAnalysisService.getPlayerVideoCount(playerId));
        stats.put("analyzedVideos", videoAnalysisService.getPlayerAnalyzedVideoCount(playerId));
        return ResponseEntity.ok(stats);
    }

    /**
     * Mark a video as analyzed
     * POST /api/video-analysis/{id}/mark-analyzed
     */
    @PostMapping("/{id}/mark-analyzed")
    public ResponseEntity<VideoAnalysis> markAsAnalyzed(@PathVariable Long id) {
        log.info("POST /api/video-analysis/{}/mark-analyzed - Marking video as analyzed", id);
        try {
            VideoAnalysis updated = videoAnalysisService.markAsAnalyzed(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Error marking video {} as analyzed: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update AI analysis results for a video
     * PUT /api/video-analysis/{id}/ai-analysis
     */
    @PutMapping("/{id}/ai-analysis")
    public ResponseEntity<VideoAnalysis> updateAIAnalysis(
            @PathVariable Long id,
            @RequestBody VideoAnalysis aiResults) {
        log.info("PUT /api/video-analysis/{}/ai-analysis - Updating AI analysis", id);
        try {
            VideoAnalysis updated = videoAnalysisService.updateAIAnalysis(id, aiResults);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            log.error("Error updating AI analysis for video {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
