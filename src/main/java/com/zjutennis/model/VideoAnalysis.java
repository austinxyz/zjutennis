package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * VideoAnalysis entity representing player-specific performance analysis for a video
 * Each analysis focuses on ONE player's performance in a video
 * Multiple analyses can exist for the same video (one per player)
 */
@Entity
@Table(name = "video_analysis")
@Data
@EntityListeners(AuditingEntityListener.class)
public class VideoAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to Video (required)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    @JsonBackReference("video-analyses")
    private Video video;

    // Relationship to Player (required - which player is being analyzed)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @JsonBackReference("player-analyses")
    private Player player;

    // AI Analysis Status
    @Column(name = "ai_analyzed")
    private Boolean aiAnalyzed = false;

    @Column(name = "ai_analysis_date")
    private LocalDateTime aiAnalysisDate;

    // Strengths Analysis (0-5 scale)
    @Column(name = "strength_forehand_score")
    private Double strengthForehandScore;

    @Column(name = "strength_serve_score")
    private Double strengthServeScore;

    @Column(name = "strength_volley_score")
    private Double strengthVolleyScore;

    @Column(name = "strength_movement_score")
    private Double strengthMovementScore;

    @Column(name = "strength_summary", columnDefinition = "TEXT")
    private String strengthSummary; // AI-generated text summary

    // Weaknesses Analysis (0-5 scale, higher = more issues)
    @Column(name = "weakness_backhand_score")
    private Double weaknessBackhandScore;

    @Column(name = "weakness_consistency_score")
    private Double weaknessConsistencyScore;

    @Column(name = "weakness_pressure_score")
    private Double weaknessPressureScore;

    @Column(name = "weakness_summary", columnDefinition = "TEXT")
    private String weaknessSummary; // AI-generated text summary

    // Tactical Analysis
    @Column(name = "tactical_style", length = 50)
    private String tacticalStyle; // aggressive, defensive, balanced, all-court

    @Column(name = "aggression_index")
    private Double aggressionIndex; // 0-100

    @Column(name = "net_approach_frequency")
    private Double netApproachFrequency; // Percentage

    @Column(name = "baseline_rally_preference")
    private Double baselineRallyPreference; // Percentage

    @Column(name = "tactical_summary", columnDefinition = "TEXT")
    private String tacticalSummary; // AI-generated tactical analysis

    // AI Recommendations
    @Column(name = "ai_recommendations", columnDefinition = "TEXT")
    private String aiRecommendations; // JSON format

    @Column(name = "training_focus_areas", columnDefinition = "TEXT")
    private String trainingFocusAreas;

    // Video Keyframes/Highlights
    @Column(name = "keyframes_json", columnDefinition = "TEXT")
    private String keyframesJson; // JSON array of timestamps and descriptions

    // Status and Metadata
    @Column(name = "status", length = 20)
    private String status = "pending"; // pending, processing, completed, failed

    @Column(name = "processing_notes", columnDefinition = "TEXT")
    private String processingNotes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
