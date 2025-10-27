package com.zjutennis.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for creating or updating video analysis entries
 * Accepts matchId as a Long instead of nested Match object
 */
@Data
public class VideoAnalysisRequest {
    private String title;
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Long matchId;  // Match ID instead of Match object
    private LocalDate matchDate;
    private Integer durationSeconds;
    private Integer totalShots;
    private Integer errors;
    private Integer winners;
    private Integer aces;
    private Integer doubleFaults;
    private Double runningDistanceMeters;
}
