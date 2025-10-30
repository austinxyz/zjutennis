package com.zjutennis.service;

import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerStatistics;
import com.zjutennis.parser.UTRPlayerResultDTO;
import com.zjutennis.parser.UTRService;
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

    @Autowired
    private UTRService utrService;

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

    /**
     * Update player statistics from UTR API using UTR ID
     * Fetches match data from UTR API and updates wins, losses, win rate, and total matches
     *
     * @param playerId The player ID
     * @param utrId The UTR ID (can be just the ID or a full URL)
     * @return Updated PlayerStatistics
     * @throws RuntimeException if player or statistics not found, or if UTR ID is invalid
     */
    @Transactional
    public PlayerStatistics updateFromUTR(Long playerId, String utrId) {
        log.info("Updating player statistics from UTR for player id: {} with UTR ID: {}", playerId, utrId);

        // Get player statistics
        PlayerStatistics statistics = playerStatisticsRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new RuntimeException("Statistics not found for player id: " + playerId));

        // Check if UTR ID is provided
        if (utrId == null || utrId.trim().isEmpty()) {
            throw new RuntimeException("UTR ID cannot be null or empty");
        }

        // Extract UTR ID from URL if it's a URL
        // UTR URLs typically look like: https://app.utrsports.net/profiles/123456
        // or just the ID: 123456
        String extractedUtrId = extractUtrIdFromUrl(utrId);
        log.debug("Extracted UTR ID: {} from input: {}", extractedUtrId, utrId);

        try {
            // Fetch data from UTR API
            UTRPlayerResultDTO utrResult = utrService.getPlayerResults(extractedUtrId);

            if (utrResult != null) {
                // Update statistics
                Integer wins = utrResult.getWins();
                Integer losses = utrResult.getLosses();
                Integer withdrawls = utrResult.getWithdrawls();

                statistics.setWins(wins);
                statistics.setLosses(losses);

                // Calculate total matches: wins + losses + withdrawals
                if (wins != null && losses != null && withdrawls != null) {
                    int totalMatches = wins + losses + withdrawls;
                    statistics.setTotalMatches(totalMatches);

                    // Calculate win rate: wins / (wins + losses + withdrawals)
                    if (totalMatches > 0) {
                        double winRate = (wins * 100.0) / totalMatches;
                        statistics.setWinRate(winRate);
                        log.info("Updated player {} statistics: wins={}, losses={}, withdrawals={}, totalMatches={}, winRate={}%",
                                playerId, wins, losses, withdrawls, totalMatches, String.format("%.2f", winRate));
                    } else {
                        statistics.setWinRate(0.0);
                    }
                }

                return playerStatisticsRepository.save(statistics);
            } else {
                throw new RuntimeException("Failed to fetch UTR data for player id: " + playerId);
            }

        } catch (Exception e) {
            log.error("Error updating statistics from UTR for player {}: {}", playerId, e.getMessage());
            throw new RuntimeException("Failed to update statistics from UTR: " + e.getMessage(), e);
        }
    }

    /**
     * Extract UTR ID from UTR URL
     * Supports formats:
     * - https://app.utrsports.net/profiles/123456
     * - app.utrsports.net/profiles/123456
     * - 123456 (just the ID)
     *
     * @param utrUrl The UTR URL or ID
     * @return The extracted UTR ID
     */
    private String extractUtrIdFromUrl(String utrUrl) {
        if (utrUrl == null || utrUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("UTR URL cannot be null or empty");
        }

        String trimmedUrl = utrUrl.trim();

        // If it's just a number, return it
        if (trimmedUrl.matches("\\d+")) {
            return trimmedUrl;
        }

        // Extract from URL patterns
        // Pattern: https://app.utrsports.net/profiles/123456
        if (trimmedUrl.contains("profiles/")) {
            String[] parts = trimmedUrl.split("profiles/");
            if (parts.length > 1) {
                // Get the ID part and remove any trailing slashes or query parameters
                String idPart = parts[1].split("[/?#]")[0];
                if (idPart.matches("\\d+")) {
                    return idPart;
                }
            }
        }

        // If we couldn't extract a valid ID, throw an exception
        throw new IllegalArgumentException("Invalid UTR URL format: " + utrUrl);
    }
}
