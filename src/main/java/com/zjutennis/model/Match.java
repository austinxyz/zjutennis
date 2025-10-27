package com.zjutennis.model;

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
 * Match entity representing a tennis match (singles or doubles)
 */
@Entity
@Table(name = "matches")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_type", nullable = false, length = 20)
    private String matchType; // 'singles' or 'doubles'

    @Column(name = "match_time", nullable = false)
    private LocalDateTime matchTime;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "tournament_name", length = 200)
    private String tournamentName;

    @Column(name = "round", length = 50)
    private String round;

    @Column(name = "score", length = 100)
    private String score; // e.g., "6-4, 6-3"

    @Column(name = "result", nullable = false, length = 20)
    private String result = "complete"; // 'complete', 'retired', 'default', 'double_default'

    @Column(name = "winner_side", length = 10)
    private String winnerSide; // 'team1' or 'team2'

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "surface", length = 50)
    private String surface; // 'hard', 'clay', 'grass', 'carpet'

    @Column(name = "indoor")
    private Boolean indoor = false;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    // Player fields - Team 1 (Our Team)
    @ManyToOne
    @JoinColumn(name = "player1_id")
    private Player player1;

    @Column(name = "player1_name", length = 100)
    private String player1Name;

    @ManyToOne
    @JoinColumn(name = "player2_id")
    private Player player2;

    @Column(name = "player2_name", length = 100)
    private String player2Name;

    // Player fields - Team 2 (Opponent)
    @ManyToOne
    @JoinColumn(name = "opponent_player1_id")
    private Player opponentPlayer1;

    @Column(name = "opponent_player1_name", length = 100)
    private String opponentPlayer1Name;

    @ManyToOne
    @JoinColumn(name = "opponent_player2_id")
    private Player opponentPlayer2;

    @Column(name = "opponent_player2_name", length = 100)
    private String opponentPlayer2Name;

    // Relationships
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    @JsonManagedReference("match-videos")
    private List<VideoAnalysis> videos = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public void addVideo(VideoAnalysis video) {
        videos.add(video);
        video.setMatch(this);
    }

    public void removeVideo(VideoAnalysis video) {
        videos.remove(video);
        video.setMatch(null);
    }
}
