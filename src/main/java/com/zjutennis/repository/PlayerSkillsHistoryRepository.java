package com.zjutennis.repository;

import com.zjutennis.model.PlayerSkillsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerSkillsHistoryRepository extends JpaRepository<PlayerSkillsHistory, Long> {

    /**
     * Find all history records for a specific player, ordered by creation date (newest first)
     */
    List<PlayerSkillsHistory> findByPlayerIdOrderByCreatedAtDesc(Long playerId);
}
