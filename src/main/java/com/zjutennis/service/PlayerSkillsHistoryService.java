package com.zjutennis.service;

import com.zjutennis.model.PlayerSkillsHistory;
import com.zjutennis.repository.PlayerSkillsHistoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PlayerSkillsHistoryService {

    @Autowired
    private PlayerSkillsHistoryRepository playerSkillsHistoryRepository;

    /**
     * Get all history records for a player, sorted by newest first
     */
    public List<PlayerSkillsHistory> getPlayerSkillsHistory(Long playerId) {
        log.debug("Fetching skills history for player: {}", playerId);
        return playerSkillsHistoryRepository.findByPlayerIdOrderByCreatedAtDesc(playerId);
    }
}
