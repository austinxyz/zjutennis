package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a player's alumni information
 */
@Entity
@Table(name = "player_alumni")
@Data
@EntityListeners(AuditingEntityListener.class)
public class PlayerAlumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "player_id", nullable = false, unique = true)
    @JsonBackReference("player-alumni")
    private Player player;

    // Player's Graduation Universities
    @Column(name = "graduation_university1", length = 200)
    private String graduationUniversity1;

    @Column(name = "graduation_year1")
    private Integer graduationYear1;

    @Column(name = "graduation_university2", length = 200)
    private String graduationUniversity2;

    @Column(name = "graduation_year2")
    private Integer graduationYear2;

    @Column(name = "graduation_university3", length = 200)
    private String graduationUniversity3;

    @Column(name = "graduation_year3")
    private Integer graduationYear3;

    // Couple's Graduation Universities
    @Column(name = "couple_graduation_university1", length = 200)
    private String coupleGraduationUniversity1;

    @Column(name = "couple_graduation_year1")
    private Integer coupleGraduationYear1;

    @Column(name = "couple_graduation_university2", length = 200)
    private String coupleGraduationUniversity2;

    @Column(name = "couple_graduation_year2")
    private Integer coupleGraduationYear2;

    @Column(name = "couple_graduation_university3", length = 200)
    private String coupleGraduationUniversity3;

    @Column(name = "couple_graduation_year3")
    private Integer coupleGraduationYear3;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
