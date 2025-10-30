package com.zjutennis.parser;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * DTO for UTR Player Results API Response
 * Maps to https://app.utrsports.net/api/v1/player/{UTRID}/results?year=last
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UTRPlayerResultDTO {

    @JsonProperty("wins")
    private Integer wins;

    @JsonProperty("losses")
    private Integer losses;

    @JsonProperty("withdrawls")
    private Integer withdrawls;

    /**
     * Calculate win rate percentage
     * @return win rate as percentage (0-100), or null if no matches
     */
    public Double getWinRate() {
        if (wins == null || losses == null) {
            return null;
        }
        int totalMatches = wins + losses;
        if (totalMatches == 0) {
            return null;
        }
        return (wins * 100.0) / totalMatches;
    }

    /**
     * Get total matches count
     * @return total number of matches (wins + losses)
     */
    public Integer getTotalMatches() {
        if (wins == null || losses == null) {
            return null;
        }
        return wins + losses;
    }
}
