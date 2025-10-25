package com.zjutennis.service;

import com.zjutennis.dto.ImportResult;
import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerSkills;
import com.zjutennis.model.PlayerSkillsHistory;
import com.zjutennis.model.PlayerStatistics;
import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.repository.PlayerSkillsHistoryRepository;
import com.zjutennis.util.CSVUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerSkillsHistoryRepository playerSkillsHistoryRepository;

    public List<Player> getAllPlayers() {
        log.debug("Fetching all players");
        return playerRepository.findAll();
    }

    public Optional<Player> getPlayerById(Long id) {
        log.debug("Fetching player by id: {}", id);
        return playerRepository.findById(id);
    }

    public Optional<Player> getPlayerByEmail(String email) {
        log.debug("Fetching player by email: {}", email);
        return playerRepository.findByEmail(email);
    }

    public List<Player> getPlayersByCity(String city) {
        log.debug("Fetching players by city: {}", city);
        return playerRepository.findByCity(city);
    }

    @Transactional
    public Player createPlayer(Player player) {
        log.debug("Creating new player: {}", player.getName());
        return playerRepository.save(player);
    }

    @Transactional
    public Player updatePlayer(Long id, Player playerDetails) {
        log.debug("Updating player with id: {}", id);
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + id));

        // Update basic info
        player.setName(playerDetails.getName());
        player.setEmail(playerDetails.getEmail());
        player.setCity(playerDetails.getCity());
        player.setCountry(playerDetails.getCountry());
        player.setPhoneNumber(playerDetails.getPhoneNumber());
        player.setGender(playerDetails.getGender());

        // Update skills if provided
        if (playerDetails.getSkills() != null) {
            PlayerSkills newSkills = playerDetails.getSkills();
            PlayerSkills existingSkills = player.getSkills();

            // Check if strengths or weaknesses have changed and create history record
            boolean strengthsChanged = false;
            boolean weaknessesChanged = false;

            if (existingSkills != null) {
                String oldStrengths = existingSkills.getStrengths();
                String newStrengthsValue = newSkills.getStrengths();
                String oldWeaknesses = existingSkills.getWeaknesses();
                String newWeaknessesValue = newSkills.getWeaknesses();

                strengthsChanged = (newStrengthsValue != null && !newStrengthsValue.equals(oldStrengths)) ||
                                  (newStrengthsValue == null && oldStrengths != null);
                weaknessesChanged = (newWeaknessesValue != null && !newWeaknessesValue.equals(oldWeaknesses)) ||
                                   (newWeaknessesValue == null && oldWeaknesses != null);
            } else {
                // No existing skills, any non-null value is a change
                strengthsChanged = newSkills.getStrengths() != null && !newSkills.getStrengths().trim().isEmpty();
                weaknessesChanged = newSkills.getWeaknesses() != null && !newSkills.getWeaknesses().trim().isEmpty();
            }

            // Create history record if either strengths or weaknesses changed
            if (strengthsChanged || weaknessesChanged) {
                PlayerSkillsHistory history = new PlayerSkillsHistory();
                history.setPlayer(player);
                history.setStrengths(newSkills.getStrengths());
                history.setWeaknesses(newSkills.getWeaknesses());
                playerSkillsHistoryRepository.save(history);
                log.debug("Created skills history record for player {}", id);
            }

            player.setSkills(newSkills);
        }

        // Update statistics if provided
        if (playerDetails.getStatistics() != null) {
            PlayerStatistics newStats = playerDetails.getStatistics();
            PlayerStatistics existingStats = player.getStatistics();

            // Check if UTR rating has changed and update the date accordingly
            if (existingStats != null && newStats.getUtrRating() != null) {
                Double oldUtr = existingStats.getUtrRating();
                Double newUtr = newStats.getUtrRating();

                if (oldUtr == null || !oldUtr.equals(newUtr)) {
                    newStats.setUtrUpdatedDate(LocalDateTime.now());
                }
            } else if (newStats.getUtrRating() != null) {
                // First time setting UTR rating
                newStats.setUtrUpdatedDate(LocalDateTime.now());
            }

            player.setStatistics(newStats);
        }

        // Update alumni if provided
        if (playerDetails.getAlumni() != null) {
            player.setAlumni(playerDetails.getAlumni());
        }

        return playerRepository.save(player);
    }

    @Transactional
    public ImportResult importPlayersFromCSV(MultipartFile file) throws IOException {
        log.debug("Importing players from CSV file: {}", file.getOriginalFilename());

        List<Map<String, String>> rows = CSVUtil.parseCSV(file.getInputStream());

        int successCount = 0;
        int errorCount = 0;

        for (Map<String, String> row : rows) {
            try {
                String playerIdStr = row.get("Player ID");
                if (playerIdStr == null || playerIdStr.trim().isEmpty() || playerIdStr.equals("-")) {
                    errorCount++;
                    continue;
                }

                Long playerId = Long.parseLong(playerIdStr.trim());
                Optional<Player> playerOpt = playerRepository.findById(playerId);

                if (!playerOpt.isPresent()) {
                    log.warn("Player not found with id: {}", playerId);
                    errorCount++;
                    continue;
                }

                Player player = playerOpt.get();
                boolean playerUpdated = false;

                // Update player basic info
                String name = row.get("Name");
                if (name != null && !name.trim().isEmpty() && !name.equals("-")) {
                    player.setName(name.trim());
                    playerUpdated = true;
                }

                String gender = row.get("Gender");
                if (gender != null && !gender.trim().isEmpty() && !gender.equals("-")) {
                    player.setGender(gender.trim().toLowerCase());
                    playerUpdated = true;
                }

                if (playerUpdated) {
                    playerRepository.save(player);
                }

                // Update player statistics
                boolean statsUpdated = false;
                PlayerStatistics statistics = player.getStatistics();
                if (statistics == null) {
                    statistics = new PlayerStatistics();
                }

                String utrRating = row.get("UTR Rating");
                if (utrRating != null && !utrRating.trim().isEmpty() && !utrRating.equals("-")) {
                    Double oldUtr = statistics.getUtrRating();
                    Double newUtr = Double.parseDouble(utrRating.trim());

                    // Update UTR updated date if rating changed
                    if (oldUtr == null || !oldUtr.equals(newUtr)) {
                        statistics.setUtrUpdatedDate(LocalDateTime.now());
                    }

                    statistics.setUtrRating(newUtr);
                    statsUpdated = true;
                }

                String utrStatus = row.get("UTR Status");
                if (utrStatus != null && !utrStatus.trim().isEmpty() && !utrStatus.equals("-")) {
                    statistics.setUtrStatus(utrStatus.trim());
                    statsUpdated = true;
                }

                String ntrpRating = row.get("NTRP Rating");
                if (ntrpRating != null && !ntrpRating.trim().isEmpty() && !ntrpRating.equals("-")) {
                    statistics.setNtrpRating(Double.parseDouble(ntrpRating.trim()));
                    statsUpdated = true;
                }

                String ntrpStatus = row.get("NTRP Status");
                if (ntrpStatus != null && !ntrpStatus.trim().isEmpty() && !ntrpStatus.equals("-")) {
                    statistics.setNtrpStatus(ntrpStatus.trim());
                    statsUpdated = true;
                }

                String dynamicRating = row.get("Dynamic Rating");
                if (dynamicRating != null && !dynamicRating.trim().isEmpty() && !dynamicRating.equals("-")) {
                    statistics.setDynamicRating(Double.parseDouble(dynamicRating.trim()));
                    statsUpdated = true;
                }

                String winRate = row.get("Win Rate");
                if (winRate != null && !winRate.trim().isEmpty() && !winRate.equals("-")) {
                    String winRateStr = winRate.trim().replace("%", "");
                    statistics.setWinRate(Double.parseDouble(winRateStr));
                    statsUpdated = true;
                }

                if (statsUpdated) {
                    player.setStatistics(statistics);
                    playerRepository.save(player);
                }

                successCount++;
            } catch (Exception e) {
                log.error("Error processing row: {}", row, e);
                errorCount++;
            }
        }

        String message = String.format("Import completed: %d players updated, %d errors occurred.",
                                       successCount, errorCount);
        log.info(message);

        return new ImportResult(successCount, errorCount, message);
    }
}
