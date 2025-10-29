package com.zjutennis.controller;

import com.zjutennis.dto.ErrorResponse;
import com.zjutennis.dto.VideoRequest;
import com.zjutennis.dto.VideoResponse;
import com.zjutennis.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Video operations
 */
@RestController
@RequestMapping("/api/videos")
@CrossOrigin(origins = "*")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * Get all videos
     * GET /api/videos
     */
    @GetMapping
    public ResponseEntity<List<VideoResponse>> getAllVideos() {
        List<VideoResponse> videos = videoService.getAllVideos();
        return ResponseEntity.ok(videos);
    }

    /**
     * Get video by ID
     * GET /api/videos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable Long id) {
        try {
            VideoResponse video = videoService.getVideoById(id);
            return ResponseEntity.ok(video);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create video
     * POST /api/videos
     */
    @PostMapping
    public ResponseEntity<?> createVideo(@RequestBody VideoRequest request) {
        try {
            VideoResponse created = videoService.createVideo(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Update video
     * PUT /api/videos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<VideoResponse> updateVideo(
            @PathVariable Long id,
            @RequestBody VideoRequest request) {
        try {
            VideoResponse updated = videoService.updateVideo(id, request);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete video
     * DELETE /api/videos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        try {
            videoService.deleteVideo(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
