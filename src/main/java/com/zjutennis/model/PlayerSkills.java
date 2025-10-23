package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a player's technical skills evaluation
 * All skill ratings are on a scale of 1-10
 */
@Entity
@Table(name = "player_skills")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PlayerSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    @JsonBackReference("player-skills")
    private Player player;

    // Technical Skills (1-10 scale)
    @Column(name = "forehand")
    private Double forehand;

    @Column(name = "backhand")
    private Double backhand;

    @Column(name = "baseline")
    private Double baseline;

    @Column(name = "volley")
    private Double volley;

    @Column(name = "smash")
    private Double smash;

    @Column(name = "serve")
    private Double serve;

    @Column(name = "return_serve")
    private Double returnServe;

    // Mental and Physical Skills (1-10 scale)
    @Column(name = "mental")
    private Double mental;

    @Column(name = "movement")
    private Double movement;

    @Column(name = "fitness")
    private Double fitness;

    // Strategy and Tactics (1-10 scale)
    @Column(name = "court_positioning")
    private Double courtPositioning;

    @Column(name = "shot_selection")
    private Double shotSelection;

    @Column(name = "competitive_spirit")
    private Double competitiveSpirit;

    // Notes and Comments
    @Column(name = "strengths", columnDefinition = "TEXT")
    private String strengths;

    @Column(name = "weaknesses", columnDefinition = "TEXT")
    private String weaknesses;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
