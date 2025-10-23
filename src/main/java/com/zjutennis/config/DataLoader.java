package com.zjutennis.config;

import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.service.DataImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private DataImportService dataImportService;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        long count = playerRepository.count();
        log.info("Current player count in database: {}", count);

        if (count == 0) {
            log.info("No players found in database. Importing from CSV...");
            dataImportService.importPlayersFromCSV("ZJUAlumni.csv");
            log.info("CSV import completed. Total players: {}", playerRepository.count());
        } else {
            log.info("Players already exist in database. Skipping CSV import.");
            log.info("To re-import, please truncate the players table first.");
        }
    }
}
