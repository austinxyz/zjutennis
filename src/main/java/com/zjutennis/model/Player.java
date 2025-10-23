package com.zjutennis.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "players")
@Data
@EntityListeners(AuditingEntityListener.class)
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "country")
    private String country;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "gender", length = 10)
    private String gender; // male, female, other

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("player-skills")
    private PlayerSkills skills;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("player-statistics")
    private PlayerStatistics statistics;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference("player-alumni")
    private PlayerAlumni alumni;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods to maintain bidirectional relationship
    public void setSkills(PlayerSkills skills) {
        this.skills = skills;
        if (skills != null) {
            skills.setPlayer(this);
        }
    }

    public void setStatistics(PlayerStatistics statistics) {
        this.statistics = statistics;
        if (statistics != null) {
            statistics.setPlayer(this);
        }
    }

    public void setAlumni(PlayerAlumni alumni) {
        this.alumni = alumni;
        if (alumni != null) {
            alumni.setPlayer(this);
        }
    }
}
