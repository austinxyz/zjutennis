package com.zjutennis.dto;

import lombok.Data;

/**
 * Request DTO for creating/updating video analysis
 */
@Data
public class VideoAnalysisRequest {
    private Long videoId; // Required when creating
    private Long playerId; // Required - which player is being analyzed

    // AI Analysis Status
    private Boolean aiAnalyzed;

    // Strengths (0-5 scale)
    private Double strengthForehandScore;
    private Double strengthServeScore;
    private Double strengthVolleyScore;
    private Double strengthMovementScore;
    private String strengthSummary;

    // Weaknesses (0-5 scale)
    private Double weaknessBackhandScore;
    private Double weaknessConsistencyScore;
    private Double weaknessPressureScore;
    private String weaknessSummary;

    // Tactical Analysis
    private String tacticalStyle;
    private Double aggressionIndex;
    private Double netApproachFrequency;
    private Double baselineRallyPreference;
    private String tacticalSummary;

    // AI Recommendations
    private String aiRecommendations;
    private String trainingFocusAreas;
    private String keyframesJson;

    // Status
    private String status;
    private String processingNotes;
}
