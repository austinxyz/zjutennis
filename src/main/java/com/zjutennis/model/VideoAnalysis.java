package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing video analysis for a player's match
 * Supports both uploaded videos and external links (YouTube, etc.)
 * Includes AI-generated analysis of strengths, weaknesses, and tactics
 */
@Entity
@Table(name = "video_analysis")
@Data
@EntityListeners(AuditingEntityListener.class)
public class VideoAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    @JsonBackReference("player-videos")
    private Player player;

    // Video Information
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url", length = 500)
    private String videoUrl; // External URL (YouTube, etc.)

    @Column(name = "video_file_path", length = 500)
    private String videoFilePath; // Local uploaded file path

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "match_date")
    private LocalDate matchDate;

    // Match Summary Statistics
    @Column(name = "total_shots")
    private Integer totalShots;

    @Column(name = "errors")
    private Integer errors; // Unforced errors

    @Column(name = "winners")
    private Integer winners;

    @Column(name = "aces")
    private Integer aces;

    @Column(name = "double_faults")
    private Integer doubleFaults;

    @Column(name = "running_distance_meters")
    private Double runningDistanceMeters;

    // AI Analysis Status
    @Column(name = "ai_analyzed")
    private Boolean aiAnalyzed = false;

    @Column(name = "ai_analysis_date")
    private LocalDateTime aiAnalysisDate;

    // Strengths Analysis (0-10 scale)
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

    // Weaknesses Analysis (0-10 scale, higher = more issues)
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
