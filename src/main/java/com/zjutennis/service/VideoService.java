package com.zjutennis.service;

import com.zjutennis.dto.VideoRequest;
import com.zjutennis.dto.VideoResponse;
import com.zjutennis.model.Match;
import com.zjutennis.model.Video;
import com.zjutennis.repository.MatchRepository;
import com.zjutennis.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for Video operations
 */
@Service
@Transactional
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MatchRepository matchRepository;

    /**
     * Get all videos
     */
    public List<VideoResponse> getAllVideos() {
        return videoRepository.findAll().stream()
                .map(VideoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get video by ID
     */
    public VideoResponse getVideoById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
        return VideoResponse.fromEntity(video);
    }

    /**
     * Get video by match ID
     */
    public VideoResponse getVideoByMatchId(Long matchId) {
        Video video = videoRepository.findByMatchId(matchId)
                .orElseThrow(() -> new RuntimeException("Video not found for match: " + matchId));
        return VideoResponse.fromEntity(video);
    }

    /**
     * Check if video exists for match
     */
    public boolean videoExistsForMatch(Long matchId) {
        return videoRepository.existsByMatchId(matchId);
    }

    /**
     * Create video for a match
     */
    public VideoResponse createVideo(VideoRequest request) {
        if (request.getMatchId() == null) {
            throw new RuntimeException("Match ID is required");
        }

        // Check if match exists
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + request.getMatchId()));

        // Check if video already exists for this match
        if (videoRepository.existsByMatchId(request.getMatchId())) {
            throw new RuntimeException("Video already exists for match: " + request.getMatchId());
        }

        Video video = new Video();
        video.setDescription(request.getDescription());
        video.setVideoUrl(request.getVideoUrl());
        video.setThumbnailUrl(request.getThumbnailUrl());
        video.setDurationSeconds(request.getDurationSeconds());
        video.setMatch(match);

        video.setTotalShots(request.getTotalShots());
        video.setErrors(request.getErrors());
        video.setWinners(request.getWinners());
        video.setAces(request.getAces());
        video.setDoubleFaults(request.getDoubleFaults());
        video.setRunningDistanceMeters(request.getRunningDistanceMeters());

        Video saved = videoRepository.save(video);
        return VideoResponse.fromEntity(saved);
    }

    /**
     * Update video
     */
    public VideoResponse updateVideo(Long id, VideoRequest request) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));

        video.setDescription(request.getDescription());
        video.setVideoUrl(request.getVideoUrl());
        video.setThumbnailUrl(request.getThumbnailUrl());
        video.setDurationSeconds(request.getDurationSeconds());

        video.setTotalShots(request.getTotalShots());
        video.setErrors(request.getErrors());
        video.setWinners(request.getWinners());
        video.setAces(request.getAces());
        video.setDoubleFaults(request.getDoubleFaults());
        video.setRunningDistanceMeters(request.getRunningDistanceMeters());

        Video updated = videoRepository.save(video);
        return VideoResponse.fromEntity(updated);
    }

    /**
     * Delete video by ID
     * This will cascade delete all video analyses
     */
    public void deleteVideo(Long id) {
        if (!videoRepository.existsById(id)) {
            throw new RuntimeException("Video not found with id: " + id);
        }
        videoRepository.deleteById(id);
    }

    /**
     * Delete video by match ID
     */
    public void deleteVideoByMatchId(Long matchId) {
        if (!videoRepository.existsByMatchId(matchId)) {
            throw new RuntimeException("Video not found for match: " + matchId);
        }
        videoRepository.deleteByMatchId(matchId);
    }

    /**
     * Get video entity (for internal use)
     */
    public Video getVideoEntity(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
    }
}
