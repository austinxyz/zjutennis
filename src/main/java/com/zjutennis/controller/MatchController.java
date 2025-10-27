package com.zjutennis.controller;

import com.zjutennis.model.Match;
import com.zjutennis.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller for Match management
 */
@RestController
@RequestMapping("/api/matches")
@CrossOrigin(origins = "*")
@Slf4j
public class MatchController {

    @Autowired
    private MatchService matchService;

    /**
     * Get all matches
     */
    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        log.info("GET /api/matches - Fetching all matches");
        List<Match> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    /**
     * Get match by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Match> getMatchById(@PathVariable Long id) {
        log.info("GET /api/matches/{} - Fetching match by ID", id);
        return matchService.getMatchById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get matches by type (singles/doubles)
     */
    @GetMapping("/type/{matchType}")
    public ResponseEntity<List<Match>> getMatchesByType(@PathVariable String matchType) {
        log.info("GET /api/matches/type/{} - Fetching matches by type", matchType);
        List<Match> matches = matchService.getMatchesByType(matchType);
        return ResponseEntity.ok(matches);
    }

    /**
     * Get matches for a specific player
     */
    @GetMapping("/player/{playerId}")
    public ResponseEntity<List<Match>> getPlayerMatches(@PathVariable Long playerId) {
        log.info("GET /api/matches/player/{} - Fetching matches for player", playerId);
        List<Match> matches = matchService.getPlayerMatches(playerId);
        return ResponseEntity.ok(matches);
    }

    /**
     * Get recent matches
     */
    @GetMapping("/recent")
    public ResponseEntity<List<Match>> getRecentMatches() {
        log.info("GET /api/matches/recent - Fetching recent matches");
        List<Match> matches = matchService.getRecentMatches();
        return ResponseEntity.ok(matches);
    }

    /**
     * Get matches won by our team
     */
    @GetMapping("/wins")
    public ResponseEntity<List<Match>> getMatchesWonByOurTeam() {
        log.info("GET /api/matches/wins - Fetching matches won by our team");
        List<Match> matches = matchService.getMatchesWonByOurTeam();
        return ResponseEntity.ok(matches);
    }

    /**
     * Create a new match
     */
    @PostMapping
    public ResponseEntity<Match> createMatch(@RequestBody Match match) {
        log.info("POST /api/matches - Creating new match");
        try {
            Match createdMatch = matchService.createMatch(match);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMatch);
        } catch (Exception e) {
            log.error("Error creating match: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing match
     */
    @PutMapping("/{id}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long id, @RequestBody Match match) {
        log.info("PUT /api/matches/{} - Updating match", id);
        try {
            Match updatedMatch = matchService.updateMatch(id, match);
            return ResponseEntity.ok(updatedMatch);
        } catch (RuntimeException e) {
            log.error("Error updating match {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error updating match {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a match
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMatch(@PathVariable Long id) {
        log.info("DELETE /api/matches/{} - Deleting match", id);
        try {
            matchService.deleteMatch(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Match deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            log.error("Error deleting match {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error deleting match {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get match statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<MatchService.MatchStatistics> getMatchStatistics() {
        log.info("GET /api/matches/statistics - Fetching match statistics");
        MatchService.MatchStatistics stats = matchService.getMatchStatistics();
        return ResponseEntity.ok(stats);
    }
}
