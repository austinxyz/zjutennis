package com.zjutennis.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service for fetching player data from UTR API
 * No authentication token required for basic player results
 */
@Service
public class UTRService {

    private static final Logger logger = LoggerFactory.getLogger(UTRService.class);

    private static final String UTR_PLAYER_RESULTS_URL = "https://app.utrsports.net/api/v1/player/%s/results?year=last";

    private final RestTemplate restTemplate;

    public UTRService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Fetch player results from UTR API
     * @param utrId The UTR player ID
     * @return UTRPlayerResultDTO containing wins, losses, and withdrawals
     * @throws RuntimeException if API call fails
     */
    public UTRPlayerResultDTO getPlayerResults(String utrId) {
        if (utrId == null || utrId.trim().isEmpty()) {
            throw new IllegalArgumentException("UTR ID cannot be null or empty");
        }

        String url = String.format(UTR_PLAYER_RESULTS_URL, utrId);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

            HttpEntity<String> entity = new HttpEntity<>("{}", headers);

            logger.info("Fetching UTR player results for ID: {}", utrId);
            ResponseEntity<UTRPlayerResultDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                UTRPlayerResultDTO.class
            );

            UTRPlayerResultDTO result = response.getBody();

            if (result != null) {
                logger.info("Successfully fetched UTR data for player {}: wins={}, losses={}, withdrawals={}",
                    utrId, result.getWins(), result.getLosses(), result.getWithdrawls());
            }

            return result;

        } catch (RestClientException ex) {
            logger.error("Failed to fetch UTR data for player ID {}: {}", utrId, ex.getMessage());
            throw new RuntimeException("Failed to fetch UTR player results: " + ex.getMessage(), ex);
        }
    }

    /**
     * Get win rate for a player
     * @param utrId The UTR player ID
     * @return Win rate as percentage (0-100), or null if no matches
     */
    public Double getPlayerWinRate(String utrId) {
        UTRPlayerResultDTO result = getPlayerResults(utrId);
        return result != null ? result.getWinRate() : null;
    }

    /**
     * Get total matches count for a player
     * @param utrId The UTR player ID
     * @return Total number of matches (wins + losses)
     */
    public Integer getPlayerTotalMatches(String utrId) {
        UTRPlayerResultDTO result = getPlayerResults(utrId);
        return result != null ? result.getTotalMatches() : null;
    }
}
