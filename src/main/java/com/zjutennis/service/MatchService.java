package com.zjutennis.service;

import com.zjutennis.dto.VideoRequest;
import com.zjutennis.dto.VideoResponse;
import com.zjutennis.model.Match;
import com.zjutennis.model.Player;
import com.zjutennis.model.Video;
import com.zjutennis.repository.MatchRepository;
import com.zjutennis.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing tennis matches
 */
@Service
@Slf4j
public class MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private VideoService videoService;

    /**
     * Get all matches
     */
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    /**
     * Get match by ID
     */
    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    /**
     * Get matches by type (singles/doubles)
     */
    public List<Match> getMatchesByType(String matchType) {
        return matchRepository.findByMatchTypeOrderByMatchTimeDesc(matchType);
    }

    /**
     * Get matches for a specific player
     */
    public List<Match> getPlayerMatches(Long playerId) {
        return matchRepository.findByPlayerId(playerId);
    }

    /**
     * Get recent matches
     */
    public List<Match> getRecentMatches() {
        return matchRepository.findTop10ByOrderByMatchTimeDesc();
    }

    /**
     * Get matches won by our team
     */
    public List<Match> getMatchesWonByOurTeam() {
        return matchRepository.findMatchesWonByOurTeam();
    }

    /**
     * Create a new match
     */
    @Transactional
    public Match createMatch(Match match) {
        log.info("Creating new match: {} at {}", match.getMatchType(), match.getMatchTime());

        if (match.getMatchTime() == null) {
            match.setMatchTime(LocalDateTime.now());
        }

        if (match.getResult() == null) {
            match.setResult("complete");
        }

        // Link player entities if IDs are provided
        linkPlayerEntities(match);

        return matchRepository.save(match);
    }

    /**
     * Update an existing match
     */
    @Transactional
    public Match updateMatch(Long id, Match matchData) {
        log.info("Updating match ID: {}", id);

        return matchRepository.findById(id)
                .map(match -> {
                    // Update basic fields
                    if (matchData.getMatchType() != null) match.setMatchType(matchData.getMatchType());
                    if (matchData.getMatchTime() != null) match.setMatchTime(matchData.getMatchTime());
                    if (matchData.getLocation() != null) match.setLocation(matchData.getLocation());
                    if (matchData.getTournamentName() != null) match.setTournamentName(matchData.getTournamentName());
                    if (matchData.getRound() != null) match.setRound(matchData.getRound());
                    if (matchData.getScore() != null) match.setScore(matchData.getScore());
                    if (matchData.getResult() != null) match.setResult(matchData.getResult());
                    if (matchData.getWinnerSide() != null) match.setWinnerSide(matchData.getWinnerSide());
                    if (matchData.getDurationMinutes() != null) match.setDurationMinutes(matchData.getDurationMinutes());
                    if (matchData.getSurface() != null) match.setSurface(matchData.getSurface());
                    if (matchData.getIndoor() != null) match.setIndoor(matchData.getIndoor());
                    if (matchData.getNotes() != null) match.setNotes(matchData.getNotes());

                    // Update player fields - always update these to allow clearing
                    match.setPlayer1Name(matchData.getPlayer1Name());
                    match.setPlayer2Name(matchData.getPlayer2Name());
                    match.setOpponentPlayer1Name(matchData.getOpponentPlayer1Name());
                    match.setOpponentPlayer2Name(matchData.getOpponentPlayer2Name());

                    // Update player entity references
                    match.setPlayer1(matchData.getPlayer1());
                    match.setPlayer2(matchData.getPlayer2());
                    match.setOpponentPlayer1(matchData.getOpponentPlayer1());
                    match.setOpponentPlayer2(matchData.getOpponentPlayer2());

                    // Link player entities if IDs are provided
                    linkPlayerEntities(match, matchData);

                    return matchRepository.save(match);
                })
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
    }

    /**
     * Delete a match
     */
    @Transactional
    public void deleteMatch(Long id) {
        log.info("Deleting match ID: {}", id);

        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Match not found with id: " + id);
        }

        matchRepository.deleteById(id);
    }

    /**
     * Helper method to link player entities to a match
     */
    private void linkPlayerEntities(Match match) {
        linkPlayerEntity(match, "player1");
        linkPlayerEntity(match, "player2");
        linkPlayerEntity(match, "opponentPlayer1");
        linkPlayerEntity(match, "opponentPlayer2");
    }

    /**
     * Helper method to link player entities during update
     */
    private void linkPlayerEntities(Match match, Match matchData) {
        linkPlayerEntityFromData(match, matchData, "player1");
        linkPlayerEntityFromData(match, matchData, "player2");
        linkPlayerEntityFromData(match, matchData, "opponentPlayer1");
        linkPlayerEntityFromData(match, matchData, "opponentPlayer2");
    }

    /**
     * Link a single player entity
     */
    private void linkPlayerEntity(Match match, String playerField) {
        try {
            Player player = null;
            switch (playerField) {
                case "player1":
                    player = match.getPlayer1();
                    break;
                case "player2":
                    player = match.getPlayer2();
                    break;
                case "opponentPlayer1":
                    player = match.getOpponentPlayer1();
                    break;
                case "opponentPlayer2":
                    player = match.getOpponentPlayer2();
                    break;
            }

            if (player != null && player.getId() != null) {
                final Long playerId = player.getId();
                Player managedPlayer = playerRepository.findById(playerId)
                        .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

                switch (playerField) {
                    case "player1":
                        match.setPlayer1(managedPlayer);
                        if (match.getPlayer1Name() == null || match.getPlayer1Name().isEmpty()) {
                            match.setPlayer1Name(managedPlayer.getName());
                        }
                        break;
                    case "player2":
                        match.setPlayer2(managedPlayer);
                        if (match.getPlayer2Name() == null || match.getPlayer2Name().isEmpty()) {
                            match.setPlayer2Name(managedPlayer.getName());
                        }
                        break;
                    case "opponentPlayer1":
                        match.setOpponentPlayer1(managedPlayer);
                        if (match.getOpponentPlayer1Name() == null || match.getOpponentPlayer1Name().isEmpty()) {
                            match.setOpponentPlayer1Name(managedPlayer.getName());
                        }
                        break;
                    case "opponentPlayer2":
                        match.setOpponentPlayer2(managedPlayer);
                        if (match.getOpponentPlayer2Name() == null || match.getOpponentPlayer2Name().isEmpty()) {
                            match.setOpponentPlayer2Name(managedPlayer.getName());
                        }
                        break;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to link player entity for field {}: {}", playerField, e.getMessage());
        }
    }

    /**
     * Link player entity from matchData during update
     */
    private void linkPlayerEntityFromData(Match match, Match matchData, String playerField) {
        try {
            Player player = null;
            switch (playerField) {
                case "player1":
                    player = matchData.getPlayer1();
                    break;
                case "player2":
                    player = matchData.getPlayer2();
                    break;
                case "opponentPlayer1":
                    player = matchData.getOpponentPlayer1();
                    break;
                case "opponentPlayer2":
                    player = matchData.getOpponentPlayer2();
                    break;
            }

            if (player != null && player.getId() != null) {
                final Long playerId = player.getId();
                Player managedPlayer = playerRepository.findById(playerId)
                        .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

                switch (playerField) {
                    case "player1":
                        match.setPlayer1(managedPlayer);
                        break;
                    case "player2":
                        match.setPlayer2(managedPlayer);
                        break;
                    case "opponentPlayer1":
                        match.setOpponentPlayer1(managedPlayer);
                        break;
                    case "opponentPlayer2":
                        match.setOpponentPlayer2(managedPlayer);
                        break;
                }
            } else {
                // Clear the player reference if no ID provided
                switch (playerField) {
                    case "player1":
                        match.setPlayer1(null);
                        break;
                    case "player2":
                        match.setPlayer2(null);
                        break;
                    case "opponentPlayer1":
                        match.setOpponentPlayer1(null);
                        break;
                    case "opponentPlayer2":
                        match.setOpponentPlayer2(null);
                        break;
                }
            }
        } catch (Exception e) {
            log.warn("Failed to link player entity for field {}: {}", playerField, e.getMessage());
        }
    }

    /**
     * Attach video to match
     */
    @Transactional
    public VideoResponse attachVideo(Long matchId, VideoRequest videoRequest) {
        log.info("Attaching video to match ID: {}", matchId);

        // Check if match exists
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        // Check if match already has a video
        if (match.getVideo() != null) {
            throw new RuntimeException("Match already has a video attached. Remove it first before adding a new one.");
        }

        // Set the match ID in the request
        videoRequest.setMatchId(matchId);

        // Create the video using VideoService
        VideoResponse videoResponse = videoService.createVideo(videoRequest);

        return videoResponse;
    }

    /**
     * Remove video from match
     */
    @Transactional
    public void removeVideo(Long matchId) {
        log.info("Removing video from match ID: {}", matchId);

        // Check if match exists
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + matchId));

        // Check if match has a video
        if (match.getVideo() == null) {
            throw new RuntimeException("Match does not have a video attached");
        }

        // Delete the video (will cascade delete analyses)
        videoService.deleteVideo(match.getVideo().getId());
    }

    /**
     * Get video for a match
     */
    public VideoResponse getMatchVideo(Long matchId) {
        log.info("Getting video for match ID: {}", matchId);

        // Check if match exists
        if (!matchRepository.existsById(matchId)) {
            throw new RuntimeException("Match not found with id: " + matchId);
        }

        return videoService.getVideoByMatchId(matchId);
    }

    /**
     * Check if match has a video
     */
    public boolean hasVideo(Long matchId) {
        return videoService.videoExistsForMatch(matchId);
    }

    /**
     * Get match statistics
     */
    public MatchStatistics getMatchStatistics() {
        MatchStatistics stats = new MatchStatistics();
        stats.setTotalMatches(matchRepository.count());
        stats.setSinglesMatches(matchRepository.countByMatchType("singles"));
        stats.setDoublesMatches(matchRepository.countByMatchType("doubles"));
        return stats;
    }

    /**
     * Inner class for match statistics
     */
    public static class MatchStatistics {
        private long totalMatches;
        private long singlesMatches;
        private long doublesMatches;

        public long getTotalMatches() { return totalMatches; }
        public void setTotalMatches(long totalMatches) { this.totalMatches = totalMatches; }

        public long getSinglesMatches() { return singlesMatches; }
        public void setSinglesMatches(long singlesMatches) { this.singlesMatches = singlesMatches; }

        public long getDoublesMatches() { return doublesMatches; }
        public void setDoublesMatches(long doublesMatches) { this.doublesMatches = doublesMatches; }
    }
}
