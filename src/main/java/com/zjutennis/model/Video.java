package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Video entity representing a match video
 * Each video belongs to one match and can have multiple analyses (one per player)
 */
@Entity
@Table(name = "videos")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "video_url", length = 500)
    private String videoUrl; // External URL (YouTube, Vimeo, etc.)

    @Column(name = "thumbnail_url", length = 500)
    private String thumbnailUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    // Relationship to Match (required)
    @OneToOne
    @JoinColumn(name = "match_id", nullable = false)
    @JsonBackReference("match-video")
    private Match match;

    // Match Statistics (for the entire match/video)
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

    // Relationship to VideoAnalysis (one video can have multiple analyses)
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("video-analyses")
    private List<VideoAnalysis> analyses = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods for managing analyses
    public void addAnalysis(VideoAnalysis analysis) {
        analyses.add(analysis);
        analysis.setVideo(this);
    }

    public void removeAnalysis(VideoAnalysis analysis) {
        analyses.remove(analysis);
        analysis.setVideo(null);
    }
}
