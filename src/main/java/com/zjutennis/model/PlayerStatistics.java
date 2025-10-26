package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a player's performance statistics and activity
 */
@Entity
@Table(name = "player_statistics")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PlayerStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    @JsonBackReference("player-statistics")
    private Player player;

    // Rating Systems
    // Singles UTR
    @Column(name = "singles_utr_rating")
    private Double singlesUtrRating;

    @Column(name = "singles_utr_status", length = 20)
    private String singlesUtrStatus; // rated, projected, unrated

    @Column(name = "singles_utr_url", length = 500)
    private String singlesUtrUrl;

    @Column(name = "singles_utr_updated_date")
    private LocalDateTime singlesUtrUpdatedDate;

    // Doubles UTR
    @Column(name = "utr_rating")
    private Double utrRating;

    @Column(name = "utr_status", length = 20)
    private String utrStatus; // rated, projected, unrated

    @Column(name = "utr_url", length = 500)
    private String utrUrl;

    @Column(name = "utr_updated_date")
    private LocalDateTime utrUpdatedDate;

    @Column(name = "ntrp_rating")
    private Double ntrpRating;

    @Column(name = "ntrp_status", length = 20)
    private String ntrpStatus; // C (Computed), S (Self Rating), D (Disqualified), A (Appealed), M (Mixed)

    @Column(name = "ntrp_url", length = 500)
    private String ntrpUrl;

    @Column(name = "dynamic_rating")
    private Double dynamicRating;

    @Column(name = "dynamic_rating_url", length = 500)
    private String dynamicRatingUrl;

    @Column(name = "self_rating")
    private Double selfRating;

    // Match Statistics
    @Column(name = "total_matches")
    private Integer totalMatches;

    @Column(name = "wins")
    private Integer wins;

    @Column(name = "losses")
    private Integer losses;

    @Column(name = "win_rate")
    private Double winRate;

    @Column(name = "singles_win_rate")
    private Double singlesWinRate;

    @Column(name = "doubles_win_rate")
    private Double doublesWinRate;

    // Activity Level
    @Column(name = "play_frequency")
    private String playFrequency; // daily, weekly, monthly, occasionally

    @Column(name = "matches_per_month")
    private Integer matchesPerMonth;

    @Column(name = "practice_hours_per_week")
    private Double practiceHoursPerWeek;

    // Competitive Level
    @Column(name = "competitive_level")
    private String competitiveLevel; // recreational, intermediate, advanced, professional

    @Column(name = "tournament_participation")
    private Boolean tournamentParticipation;

    @Column(name = "league_participation")
    private Boolean leagueParticipation;

    // Performance Metrics
    @Column(name = "serve_percentage")
    private Double servePercentage;

    @Column(name = "first_serve_percentage")
    private Double firstServePercentage;

    @Column(name = "break_point_conversion")
    private Double breakPointConversion;

    @Column(name = "average_match_duration_minutes")
    private Integer averageMatchDurationMinutes;

    // Preferred Playing Style
    @Column(name = "preferred_surface")
    private String preferredSurface; // hard, clay, grass, carpet

    @Column(name = "preferred_playing_style")
    private String preferredPlayingStyle; // baseline, serve-volley, all-court

    @Column(name = "dominant_hand")
    private String dominantHand; // right, left, ambidextrous

    // Partner Preferences (for doubles)
    @Column(name = "preferred_doubles_position")
    private String preferredDoublesPosition; // ad-court, deuce-court, both

    // Availability and Goals
    @Column(name = "availability", columnDefinition = "TEXT")
    private String availability;

    @Column(name = "goals", columnDefinition = "TEXT")
    private String goals;

    @Column(name = "last_match_date")
    private LocalDateTime lastMatchDate;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
