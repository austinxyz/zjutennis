package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * MatchPlayer entity representing a player's participation in a match
 * Supports both registered players (with player_id) and non-registered players (with just a name)
 */
@Entity
@Table(name = "match_players")
@Data
@EntityListeners(AuditingEntityListener.class)
public class MatchPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    @JsonBackReference("match-players")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    @JsonBackReference("player-matches")
    private Player player; // Nullable for non-registered players

    @Column(name = "player_name", nullable = false, length = 100)
    private String playerName; // Name for display (required even if player_id is set)

    @Column(name = "team", nullable = false, length = 10)
    private String team; // 'team1' or 'team2'

    @Column(name = "position")
    private Integer position; // 1 or 2 for doubles, null for singles

    @Column(name = "is_our_team")
    private Boolean isOurTeam = false; // True if this is a tracked player (our team member)

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
