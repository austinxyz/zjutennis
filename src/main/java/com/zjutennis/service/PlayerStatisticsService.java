package com.zjutennis.service;

import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerStatistics;
import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.repository.PlayerStatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class PlayerStatisticsService {

    @Autowired
    private PlayerStatisticsRepository playerStatisticsRepository;

    @Autowired
    private PlayerRepository playerRepository;

    public Optional<PlayerStatistics> getPlayerStatistics(Long playerId) {
        log.debug("Fetching statistics for player id: {}", playerId);
        return playerStatisticsRepository.findByPlayerId(playerId);
    }

    @Transactional
    public PlayerStatistics createPlayerStatistics(Long playerId, PlayerStatistics statistics) {
        log.debug("Creating statistics for player id: {}", playerId);
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        statistics.setPlayer(player);
        return playerStatisticsRepository.save(statistics);
    }

    @Transactional
    public PlayerStatistics updatePlayerStatistics(Long playerId, PlayerStatistics statisticsDetails) {
        log.debug("Updating statistics for player id: {}", playerId);

        PlayerStatistics statistics = playerStatisticsRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Statistics not found for player id: " + playerId));

        // Update ratings
        statistics.setUtrRating(statisticsDetails.getUtrRating());
        statistics.setUtrStatus(statisticsDetails.getUtrStatus());
        statistics.setUtrUrl(statisticsDetails.getUtrUrl());
        statistics.setNtrpRating(statisticsDetails.getNtrpRating());
        statistics.setNtrpStatus(statisticsDetails.getNtrpStatus());
        statistics.setNtrpUrl(statisticsDetails.getNtrpUrl());
        statistics.setDynamicRating(statisticsDetails.getDynamicRating());
        statistics.setDynamicRatingUrl(statisticsDetails.getDynamicRatingUrl());
        statistics.setSelfRating(statisticsDetails.getSelfRating());

        // Update match statistics
        statistics.setTotalMatches(statisticsDetails.getTotalMatches());
        statistics.setWins(statisticsDetails.getWins());
        statistics.setLosses(statisticsDetails.getLosses());
        statistics.setWinRate(statisticsDetails.getWinRate());
        statistics.setSinglesWinRate(statisticsDetails.getSinglesWinRate());
        statistics.setDoublesWinRate(statisticsDetails.getDoublesWinRate());

        // Update activity level
        statistics.setPlayFrequency(statisticsDetails.getPlayFrequency());
        statistics.setMatchesPerMonth(statisticsDetails.getMatchesPerMonth());
        statistics.setPracticeHoursPerWeek(statisticsDetails.getPracticeHoursPerWeek());

        // Update competitive level
        statistics.setCompetitiveLevel(statisticsDetails.getCompetitiveLevel());
        statistics.setTournamentParticipation(statisticsDetails.getTournamentParticipation());
        statistics.setLeagueParticipation(statisticsDetails.getLeagueParticipation());

        // Update performance metrics
        statistics.setServePercentage(statisticsDetails.getServePercentage());
        statistics.setFirstServePercentage(statisticsDetails.getFirstServePercentage());
        statistics.setBreakPointConversion(statisticsDetails.getBreakPointConversion());

        // Update preferences
        statistics.setPreferredSurface(statisticsDetails.getPreferredSurface());
        statistics.setPreferredPlayingStyle(statisticsDetails.getPreferredPlayingStyle());
        statistics.setDominantHand(statisticsDetails.getDominantHand());
        statistics.setPreferredDoublesPosition(statisticsDetails.getPreferredDoublesPosition());

        return playerStatisticsRepository.save(statistics);
    }

    @Transactional
    public void deletePlayerStatistics(Long playerId) {
        log.debug("Deleting statistics for player id: {}", playerId);
        playerStatisticsRepository.deleteByPlayerId(playerId);
    }
}
