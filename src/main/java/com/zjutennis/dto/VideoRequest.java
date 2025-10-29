package com.zjutennis.dto;

import lombok.Data;

/**
 * Request DTO for creating/updating videos
 */
@Data
public class VideoRequest {
    private String description;
    private String videoUrl;
    private String thumbnailUrl;
    private Integer durationSeconds;
    private Long matchId; // Required when creating

    // Match statistics
    private Integer totalShots;
    private Integer errors;
    private Integer winners;
    private Integer aces;
    private Integer doubleFaults;
    private Double runningDistanceMeters;
}
