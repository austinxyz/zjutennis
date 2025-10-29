package com.zjutennis.dto;

import com.zjutennis.model.Video;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO for Video
 */
@Data
public class VideoResponse {
    private Long id;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Integer durationSeconds;
    private Long matchId;
    private LocalDateTime matchTime; // From match
    private String tournamentName; // From match

    // Match statistics
    private Integer totalShots;
    private Integer errors;
    private Integer winners;
    private Integer aces;
    private Integer doubleFaults;
    private Double runningDistanceMeters;

    // Analysis count
    private Integer analysisCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static VideoResponse fromEntity(Video video) {
        VideoResponse response = new VideoResponse();
        response.setId(video.getId());
        response.setDescription(video.getDescription());
        response.setVideoUrl(video.getVideoUrl());
        response.setThumbnailUrl(video.getThumbnailUrl());
        response.setDurationSeconds(video.getDurationSeconds());

        if (video.getMatch() != null) {
            response.setMatchId(video.getMatch().getId());
            response.setMatchTime(video.getMatch().getMatchTime());
            response.setTournamentName(video.getMatch().getTournamentName());
        }

        response.setTotalShots(video.getTotalShots());
        response.setErrors(video.getErrors());
        response.setWinners(video.getWinners());
        response.setAces(video.getAces());
        response.setDoubleFaults(video.getDoubleFaults());
        response.setRunningDistanceMeters(video.getRunningDistanceMeters());

        if (video.getAnalyses() != null) {
            response.setAnalysisCount(video.getAnalyses().size());
        } else {
            response.setAnalysisCount(0);
        }

        response.setCreatedAt(video.getCreatedAt());
        response.setUpdatedAt(video.getUpdatedAt());

        return response;
    }
}
