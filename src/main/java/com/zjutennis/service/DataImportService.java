package com.zjutennis.service;

import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerStatistics;
import com.zjutennis.repository.PlayerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataImportService {

    @Autowired
    private PlayerRepository playerRepository;

    @Transactional
    public void importPlayersFromCSV(String csvFilePath) {
        log.info("Starting CSV import from: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            );

            List<Player> players = new ArrayList<>();
            String line;
            int lineNumber = 0;

            // Skip first two lines (header)
            reader.readLine();
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    Player player = parseCsvLine(line);
                    if (player != null) {
                        players.add(player);
                    }
                } catch (Exception e) {
                    log.error("Error parsing line {}: {}", lineNumber, line, e);
                }
            }

            reader.close();

            // Save all players
            if (!players.isEmpty()) {
                playerRepository.saveAll(players);
                log.info("Successfully imported {} players from CSV", players.size());
            } else {
                log.warn("No players found in CSV file");
            }

        } catch (Exception e) {
            log.error("Error importing CSV file: {}", csvFilePath, e);
            throw new RuntimeException("Failed to import CSV file", e);
        }
    }

    private Player parseCsvLine(String line) {
        // Split by comma
        String[] fields = line.split(",", -1);

        if (fields.length < 6) {
            log.warn("Invalid line format (expected 6 fields): {}", line);
            return null;
        }

        try {
            Player player = new Player();

            // Skip row number (field 0)
            // Field 1: First name (名)
            // Field 2: Last name (姓)
            String firstName = fields[1].trim();
            String lastName = fields[2].trim();

            if (firstName.isEmpty() && lastName.isEmpty()) {
                return null;
            }

            // Combine first and last name
            player.setName(firstName + " " + lastName);

            // Create PlayerStatistics to store UTR rating and status
            PlayerStatistics statistics = new PlayerStatistics();
            boolean hasStatistics = false;

            // Field 3: UTR Rating
            String utrString = fields[3].trim();
            if (!utrString.isEmpty() && !utrString.equalsIgnoreCase("NA")) {
                try {
                    statistics.setUtrRating(Double.parseDouble(utrString));
                    hasStatistics = true;
                } catch (NumberFormatException e) {
                    log.warn("Invalid UTR rating for {}: {}", player.getName(), utrString);
                }
            }

            // Field 4: Gender (M/F) - we can store this in a custom field if needed
            // For now, we'll skip it as it's not in our Player model

            // Field 5: Status (Rated/Projected/Unrated)
            String status = fields[5].trim();
            if (!status.isEmpty()) {
                statistics.setUtrStatus(status.toLowerCase());
                hasStatistics = true;
            } else {
                statistics.setUtrStatus("unrated");
                hasStatistics = true;
            }

            // Set statistics on player if any were populated
            if (hasStatistics) {
                player.setStatistics(statistics);
            }

            return player;

        } catch (Exception e) {
            log.error("Error parsing player data from line: {}", line, e);
            return null;
        }
    }
}
