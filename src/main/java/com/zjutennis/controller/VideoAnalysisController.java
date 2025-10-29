package com.zjutennis.controller;

import com.zjutennis.dto.VideoAnalysisRequest;
import com.zjutennis.dto.VideoAnalysisResponse;
import com.zjutennis.service.VideoAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for VideoAnalysis operations
 */
@RestController
@RequestMapping("/api/video-analyses")
@CrossOrigin(origins = "*")
public class VideoAnalysisController {

    @Autowired
    private VideoAnalysisService videoAnalysisService;

    /**
     * Get all video analyses
     * GET /api/video-analyses
     */
    @GetMapping
    public ResponseEntity<List<VideoAnalysisResponse>> getAllAnalyses() {
        List<VideoAnalysisResponse> analyses = videoAnalysisService.getAllAnalyses();
        return ResponseEntity.ok(analyses);
    }

    /**
     * Get analysis by ID
     * GET /api/video-analyses/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VideoAnalysisResponse> getAnalysisById(@PathVariable Long id) {
        try {
            VideoAnalysisResponse analysis = videoAnalysisService.getAnalysisById(id);
            return ResponseEntity.ok(analysis);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get analyses by video ID
     * GET /api/video-analyses/video/{videoId}
     */
    @GetMapping("/video/{videoId}")
    public ResponseEntity<List<VideoAnalysisResponse>> getAnalysesByVideoId(@PathVariable Long videoId) {
        List<VideoAnalysisResponse> analyses = videoAnalysisService.getAnalysesByVideoId(videoId);
        return ResponseEntity.ok(analyses);
    }

    /**
     * Get analyses by player ID
     * GET /api/video-analyses/player/{playerId}
     */
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<VideoAnalysisResponse>> getAnalysesByPlayerId(@PathVariable Long playerId) {
        List<VideoAnalysisResponse> analyses = videoAnalysisService.getAnalysesByPlayerId(playerId);
        return ResponseEntity.ok(analyses);
    }

    /**
     * Get analysis by video and player
     * GET /api/video-analyses/video/{videoId}/player/{playerId}
     */
    @GetMapping("/video/{videoId}/player/{playerId}")
    public ResponseEntity<VideoAnalysisResponse> getAnalysisByVideoAndPlayer(
            @PathVariable Long videoId,
            @PathVariable Long playerId) {
        try {
            VideoAnalysisResponse analysis = videoAnalysisService.getAnalysisByVideoAndPlayer(videoId, playerId);
            return ResponseEntity.ok(analysis);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create video analysis
     * POST /api/video-analyses
     */
    @PostMapping
    public ResponseEntity<VideoAnalysisResponse> createAnalysis(@RequestBody VideoAnalysisRequest request) {
        try {
            VideoAnalysisResponse created = videoAnalysisService.createAnalysis(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update video analysis
     * PUT /api/video-analyses/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<VideoAnalysisResponse> updateAnalysis(
            @PathVariable Long id,
            @RequestBody VideoAnalysisRequest request) {
        try {
            VideoAnalysisResponse updated = videoAnalysisService.updateAnalysis(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete video analysis
     * DELETE /api/video-analyses/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysis(@PathVariable Long id) {
        try {
            videoAnalysisService.deleteAnalysis(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get analysis count for video
     * GET /api/video-analyses/video/{videoId}/count
     */
    @GetMapping("/video/{videoId}/count")
    public ResponseEntity<Long> getAnalysisCountForVideo(@PathVariable Long videoId) {
        long count = videoAnalysisService.getAnalysisCountForVideo(videoId);
        return ResponseEntity.ok(count);
    }
}
