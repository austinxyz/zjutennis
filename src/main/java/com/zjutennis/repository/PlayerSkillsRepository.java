package com.zjutennis.repository;

import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerSkills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerSkillsRepository extends JpaRepository<PlayerSkills, Long> {

    Optional<PlayerSkills> findByPlayer(Player player);

    Optional<PlayerSkills> findByPlayerId(Long playerId);

    void deleteByPlayerId(Long playerId);
}
