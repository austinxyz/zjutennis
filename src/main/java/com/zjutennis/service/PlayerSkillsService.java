package com.zjutennis.service;

import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerSkills;
import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.repository.PlayerSkillsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class PlayerSkillsService {

    @Autowired
    private PlayerSkillsRepository playerSkillsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Optional<PlayerSkills> getPlayerSkills(Long playerId) {
        log.debug("Fetching skills for player id: {}", playerId);
        return playerSkillsRepository.findByPlayerId(playerId);
    }

    @Transactional
    public PlayerSkills createPlayerSkills(Long playerId, PlayerSkills skills) {
        log.debug("Creating skills for player id: {}", playerId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        skills.setPlayer(player);
        return playerSkillsRepository.save(skills);
    }

    @Transactional
    public PlayerSkills updatePlayerSkills(Long playerId, PlayerSkills skillsDetails) {
        log.debug("Updating skills for player id: {}", playerId);

        PlayerSkills skills = playerSkillsRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Skills not found for player id: " + playerId));

        skills.setForehand(skillsDetails.getForehand());
        skills.setBackhand(skillsDetails.getBackhand());
        skills.setBaseline(skillsDetails.getBaseline());
        skills.setVolley(skillsDetails.getVolley());
        skills.setSmash(skillsDetails.getSmash());
        skills.setServe(skillsDetails.getServe());
        skills.setReturnServe(skillsDetails.getReturnServe());
        skills.setMental(skillsDetails.getMental());
        skills.setMovement(skillsDetails.getMovement());
        skills.setFitness(skillsDetails.getFitness());
        skills.setCourtPositioning(skillsDetails.getCourtPositioning());
        skills.setShotSelection(skillsDetails.getShotSelection());
        skills.setCompetitiveSpirit(skillsDetails.getCompetitiveSpirit());
        skills.setStrengths(skillsDetails.getStrengths());
        skills.setWeaknesses(skillsDetails.getWeaknesses());
        skills.setNotes(skillsDetails.getNotes());

        return playerSkillsRepository.save(skills);
    }

    @Transactional
    public void deletePlayerSkills(Long playerId) {
        log.debug("Deleting skills for player id: {}", playerId);
        playerSkillsRepository.deleteByPlayerId(playerId);
    }
}
