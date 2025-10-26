package com.zjutennis.controller;

import com.zjutennis.dto.ImportResult;
import com.zjutennis.dto.PlayerSearchRequest;
import com.zjutennis.dto.PlayerSearchResponse;
import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerSkillsHistory;
import com.zjutennis.service.PlayerService;
import com.zjutennis.service.PlayerSkillsHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@CrossOrigin(origins = "*")
@Slf4j
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerSkillsHistoryService playerSkillsHistoryService;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        log.info("GET /api/players - Fetching all players");
        List<Player> players = playerService.getAllPlayers();
        return ResponseEntity.ok(players);
    }

    @PostMapping("/search")
    public ResponseEntity<PlayerSearchResponse> searchPlayers(@RequestBody PlayerSearchRequest searchRequest) {
        log.info("POST /api/players/search - Searching players with filters");
        PlayerSearchResponse response = playerService.searchPlayers(searchRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        log.info("GET /api/players/{} - Fetching player by id", id);
        return playerService.getPlayerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Player> getPlayerByEmail(@PathVariable String email) {
        log.info("GET /api/players/email/{} - Fetching player by email", email);
        return playerService.getPlayerByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Player>> getPlayersByCity(@PathVariable String city) {
        log.info("GET /api/players/city/{} - Fetching players by city", city);
        List<Player> players = playerService.getPlayersByCity(city);
        return ResponseEntity.ok(players);
    }

    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        log.info("POST /api/players - Creating new player: {}", player.getName());
        Player createdPlayer = playerService.createPlayer(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        log.info("PUT /api/players/{} - Updating player", id);
        try {
            Player updatedPlayer = playerService.updatePlayer(id, player);
            return ResponseEntity.ok(updatedPlayer);
        } catch (RuntimeException e) {
            log.error("Error updating player with id: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/skills-history")
    public ResponseEntity<List<PlayerSkillsHistory>> getPlayerSkillsHistory(@PathVariable Long id) {
        log.info("GET /api/players/{}/skills-history - Fetching skills history", id);
        List<PlayerSkillsHistory> history = playerSkillsHistoryService.getPlayerSkillsHistory(id);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/import")
    public ResponseEntity<ImportResult> importPlayers(@RequestParam("file") MultipartFile file) {
        log.info("POST /api/players/import - Importing players from CSV file: {}", file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ImportResult(0, 0, "File is empty"));
            }

            if (!file.getOriginalFilename().endsWith(".csv")) {
                return ResponseEntity.badRequest()
                        .body(new ImportResult(0, 0, "File must be a CSV file"));
            }

            ImportResult result = playerService.importPlayersFromCSV(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Error importing players from CSV", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ImportResult(0, 0, "Import failed: " + e.getMessage()));
        }
    }
}
