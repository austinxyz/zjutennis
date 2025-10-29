package com.zjutennis.dto;

import com.zjutennis.model.VideoAnalysis;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Response DTO for VideoAnalysis
 */
@Data
public class VideoAnalysisResponse {
    private Long id;
    private Long videoId;
    private Long playerId;
    private String playerName;

    // AI Analysis Status
    private Boolean aiAnalyzed;
    private LocalDateTime aiAnalysisDate;

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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static VideoAnalysisResponse fromEntity(VideoAnalysis analysis) {
        VideoAnalysisResponse response = new VideoAnalysisResponse();
        response.setId(analysis.getId());

        if (analysis.getVideo() != null) {
            response.setVideoId(analysis.getVideo().getId());
        }

        if (analysis.getPlayer() != null) {
            response.setPlayerId(analysis.getPlayer().getId());
            response.setPlayerName(analysis.getPlayer().getName());
        }

        response.setAiAnalyzed(analysis.getAiAnalyzed());
        response.setAiAnalysisDate(analysis.getAiAnalysisDate());

        response.setStrengthForehandScore(analysis.getStrengthForehandScore());
        response.setStrengthServeScore(analysis.getStrengthServeScore());
        response.setStrengthVolleyScore(analysis.getStrengthVolleyScore());
        response.setStrengthMovementScore(analysis.getStrengthMovementScore());
        response.setStrengthSummary(analysis.getStrengthSummary());

        response.setWeaknessBackhandScore(analysis.getWeaknessBackhandScore());
        response.setWeaknessConsistencyScore(analysis.getWeaknessConsistencyScore());
        response.setWeaknessPressureScore(analysis.getWeaknessPressureScore());
        response.setWeaknessSummary(analysis.getWeaknessSummary());

        response.setTacticalStyle(analysis.getTacticalStyle());
        response.setAggressionIndex(analysis.getAggressionIndex());
        response.setNetApproachFrequency(analysis.getNetApproachFrequency());
        response.setBaselineRallyPreference(analysis.getBaselineRallyPreference());
        response.setTacticalSummary(analysis.getTacticalSummary());

        response.setAiRecommendations(analysis.getAiRecommendations());
        response.setTrainingFocusAreas(analysis.getTrainingFocusAreas());
        response.setKeyframesJson(analysis.getKeyframesJson());

        response.setStatus(analysis.getStatus());
        response.setProcessingNotes(analysis.getProcessingNotes());

        response.setCreatedAt(analysis.getCreatedAt());
        response.setUpdatedAt(analysis.getUpdatedAt());

        return response;
    }
}
