package com.zjutennis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Match Model Tests")
class MatchTest {

    private Match match;
    private Player player1;
    private Player player2;
    private Player opponent1;
    private Player opponent2;
    private Video video;

    @BeforeEach
    void setUp() {
        // Setup Players
        player1 = new Player();
        player1.setId(1L);
        player1.setName("John Doe");
        player1.setEmail("john@example.com");

        player2 = new Player();
        player2.setId(2L);
        player2.setName("Jane Smith");
        player2.setEmail("jane@example.com");

        opponent1 = new Player();
        opponent1.setId(3L);
        opponent1.setName("Bob Wilson");
        opponent1.setEmail("bob@example.com");

        opponent2 = new Player();
        opponent2.setId(4L);
        opponent2.setName("Alice Brown");
        opponent2.setEmail("alice@example.com");

        // Setup Video
        video = new Video();
        video.setId(1L);
        video.setDescription("Match video");

        // Setup Match
        match = new Match();
        match.setId(1L);
        match.setMatchType("doubles");
        match.setMatchTime(LocalDateTime.of(2024, 1, 15, 14, 0));
        match.setLocation("Tennis Center Court 1");
        match.setTournamentName("Spring Championship");
        match.setRound("Quarterfinals");
        match.setScore("6-4, 6-3");
        match.setResult("complete");
        match.setWinnerSide("team1");
        match.setDurationMinutes(95);
        match.setSurface("hard");
        match.setIndoor(false);
        match.setPlayer1(player1);
        match.setPlayer1Name("John Doe");
        match.setPlayer2(player2);
        match.setPlayer2Name("Jane Smith");
        match.setOpponentPlayer1(opponent1);
        match.setOpponentPlayer1Name("Bob Wilson");
        match.setOpponentPlayer2(opponent2);
        match.setOpponentPlayer2Name("Alice Brown");
    }

    @Test
    @DisplayName("Should create match with all basic fields")
    void testMatchCreation() {
        assertThat(match).isNotNull();
        assertThat(match.getId()).isEqualTo(1L);
        assertThat(match.getMatchType()).isEqualTo("doubles");
        assertThat(match.getMatchTime()).isEqualTo(LocalDateTime.of(2024, 1, 15, 14, 0));
        assertThat(match.getLocation()).isEqualTo("Tennis Center Court 1");
        assertThat(match.getTournamentName()).isEqualTo("Spring Championship");
        assertThat(match.getRound()).isEqualTo("Quarterfinals");
        assertThat(match.getScore()).isEqualTo("6-4, 6-3");
        assertThat(match.getResult()).isEqualTo("complete");
        assertThat(match.getWinnerSide()).isEqualTo("team1");
        assertThat(match.getDurationMinutes()).isEqualTo(95);
        assertThat(match.getSurface()).isEqualTo("hard");
        assertThat(match.getIndoor()).isFalse();
    }

    @Test
    @DisplayName("Should maintain relationship with team 1 players")
    void testTeam1PlayersRelationship() {
        assertThat(match.getPlayer1()).isNotNull();
        assertThat(match.getPlayer1()).isEqualTo(player1);
        assertThat(match.getPlayer1().getName()).isEqualTo("John Doe");
        assertThat(match.getPlayer1Name()).isEqualTo("John Doe");

        assertThat(match.getPlayer2()).isNotNull();
        assertThat(match.getPlayer2()).isEqualTo(player2);
        assertThat(match.getPlayer2().getName()).isEqualTo("Jane Smith");
        assertThat(match.getPlayer2Name()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("Should maintain relationship with team 2 (opponent) players")
    void testTeam2PlayersRelationship() {
        assertThat(match.getOpponentPlayer1()).isNotNull();
        assertThat(match.getOpponentPlayer1()).isEqualTo(opponent1);
        assertThat(match.getOpponentPlayer1().getName()).isEqualTo("Bob Wilson");
        assertThat(match.getOpponentPlayer1Name()).isEqualTo("Bob Wilson");

        assertThat(match.getOpponentPlayer2()).isNotNull();
        assertThat(match.getOpponentPlayer2()).isEqualTo(opponent2);
        assertThat(match.getOpponentPlayer2().getName()).isEqualTo("Alice Brown");
        assertThat(match.getOpponentPlayer2Name()).isEqualTo("Alice Brown");
    }

    @Test
    @DisplayName("Should set video using helper method and maintain bidirectional relationship")
    void testSetVideoHelper() {
        match.setVideo(video);

        assertThat(match.getVideo()).isNotNull();
        assertThat(match.getVideo()).isEqualTo(video);
        assertThat(video.getMatch()).isEqualTo(match);
    }

    @Test
    @DisplayName("Should remove video using helper method")
    void testRemoveVideoHelper() {
        match.setVideo(video);
        assertThat(match.getVideo()).isNotNull();

        match.removeVideo();

        assertThat(match.getVideo()).isNull();
        assertThat(video.getMatch()).isNull();
    }

    @Test
    @DisplayName("Should handle singles match type")
    void testSinglesMatch() {
        Match singlesMatch = new Match();
        singlesMatch.setMatchType("singles");
        singlesMatch.setMatchTime(LocalDateTime.now());
        singlesMatch.setResult("complete");
        singlesMatch.setPlayer1(player1);
        singlesMatch.setPlayer1Name("John Doe");
        singlesMatch.setOpponentPlayer1(opponent1);
        singlesMatch.setOpponentPlayer1Name("Bob Wilson");

        assertThat(singlesMatch.getMatchType()).isEqualTo("singles");
        assertThat(singlesMatch.getPlayer1()).isNotNull();
        assertThat(singlesMatch.getOpponentPlayer1()).isNotNull();
        assertThat(singlesMatch.getPlayer2()).isNull();
        assertThat(singlesMatch.getOpponentPlayer2()).isNull();
    }

    @Test
    @DisplayName("Should handle doubles match type")
    void testDoublesMatch() {
        assertThat(match.getMatchType()).isEqualTo("doubles");
        assertThat(match.getPlayer1()).isNotNull();
        assertThat(match.getPlayer2()).isNotNull();
        assertThat(match.getOpponentPlayer1()).isNotNull();
        assertThat(match.getOpponentPlayer2()).isNotNull();
    }

    @Test
    @DisplayName("Should handle different result values")
    void testResultValues() {
        match.setResult("complete");
        assertThat(match.getResult()).isEqualTo("complete");

        match.setResult("retired");
        assertThat(match.getResult()).isEqualTo("retired");

        match.setResult("default");
        assertThat(match.getResult()).isEqualTo("default");

        match.setResult("double_default");
        assertThat(match.getResult()).isEqualTo("double_default");
    }

    @Test
    @DisplayName("Should handle different surface types")
    void testSurfaceTypes() {
        match.setSurface("hard");
        assertThat(match.getSurface()).isEqualTo("hard");

        match.setSurface("clay");
        assertThat(match.getSurface()).isEqualTo("clay");

        match.setSurface("grass");
        assertThat(match.getSurface()).isEqualTo("grass");

        match.setSurface("carpet");
        assertThat(match.getSurface()).isEqualTo("carpet");
    }

    @Test
    @DisplayName("Should handle winner side values")
    void testWinnerSideValues() {
        match.setWinnerSide("team1");
        assertThat(match.getWinnerSide()).isEqualTo("team1");

        match.setWinnerSide("team2");
        assertThat(match.getWinnerSide()).isEqualTo("team2");
    }

    @Test
    @DisplayName("Should handle indoor/outdoor values")
    void testIndoorOutdoorValues() {
        match.setIndoor(true);
        assertThat(match.getIndoor()).isTrue();

        match.setIndoor(false);
        assertThat(match.getIndoor()).isFalse();
    }

    @Test
    @DisplayName("Should allow null optional fields")
    void testNullOptionalFields() {
        Match minimalMatch = new Match();
        minimalMatch.setMatchType("singles");
        minimalMatch.setMatchTime(LocalDateTime.now());
        minimalMatch.setResult("complete");

        assertThat(minimalMatch.getMatchType()).isEqualTo("singles");
        assertThat(minimalMatch.getMatchTime()).isNotNull();
        assertThat(minimalMatch.getLocation()).isNull();
        assertThat(minimalMatch.getTournamentName()).isNull();
        assertThat(minimalMatch.getRound()).isNull();
        assertThat(minimalMatch.getScore()).isNull();
        assertThat(minimalMatch.getWinnerSide()).isNull();
        assertThat(minimalMatch.getDurationMinutes()).isNull();
        assertThat(minimalMatch.getSurface()).isNull();
        assertThat(minimalMatch.getNotes()).isNull();
        assertThat(minimalMatch.getPlayer1()).isNull();
        assertThat(minimalMatch.getPlayer2()).isNull();
        assertThat(minimalMatch.getOpponentPlayer1()).isNull();
        assertThat(minimalMatch.getOpponentPlayer2()).isNull();
        assertThat(minimalMatch.getVideo()).isNull();
    }

    @Test
    @DisplayName("Should update match fields")
    void testUpdateMatchFields() {
        match.setScore("7-6, 6-4");
        match.setDurationMinutes(120);
        match.setWinnerSide("team2");
        match.setNotes("Great match with intense rallies");

        assertThat(match.getScore()).isEqualTo("7-6, 6-4");
        assertThat(match.getDurationMinutes()).isEqualTo(120);
        assertThat(match.getWinnerSide()).isEqualTo("team2");
        assertThat(match.getNotes()).isEqualTo("Great match with intense rallies");
    }

    @Test
    @DisplayName("Should default result to complete")
    void testDefaultResultValue() {
        Match newMatch = new Match();
        assertThat(newMatch.getResult()).isEqualTo("complete");
    }

    @Test
    @DisplayName("Should default indoor to false")
    void testDefaultIndoorValue() {
        Match newMatch = new Match();
        assertThat(newMatch.getIndoor()).isFalse();
    }

    @Test
    @DisplayName("Should handle long notes text")
    void testLongNotesText() {
        String longNotes = "This was an exceptional match that showcased excellent tennis. " +
                "Both teams displayed great skill and sportsmanship throughout the match. " +
                "The first set was particularly competitive with several tie-breaks. " +
                "Weather conditions were ideal for outdoor play.";

        match.setNotes(longNotes);
        assertThat(match.getNotes()).isEqualTo(longNotes);
        assertThat(match.getNotes().length()).isGreaterThan(200);
    }

    @Test
    @DisplayName("Should handle various score formats")
    void testScoreFormats() {
        match.setScore("6-4, 6-3");
        assertThat(match.getScore()).isEqualTo("6-4, 6-3");

        match.setScore("7-6(5), 3-6, 6-4");
        assertThat(match.getScore()).isEqualTo("7-6(5), 3-6, 6-4");

        match.setScore("6-0, 6-1");
        assertThat(match.getScore()).isEqualTo("6-0, 6-1");
    }

    @Test
    @DisplayName("Should maintain player name consistency")
    void testPlayerNameConsistency() {
        // When player object is set, name fields should be kept in sync manually
        match.setPlayer1(player1);
        match.setPlayer1Name(player1.getName());

        assertThat(match.getPlayer1Name()).isEqualTo(match.getPlayer1().getName());
        assertThat(match.getPlayer1Name()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should handle match time in various formats")
    void testMatchTimeFormats() {
        LocalDateTime morning = LocalDateTime.of(2024, 6, 15, 9, 0);
        LocalDateTime afternoon = LocalDateTime.of(2024, 6, 15, 14, 30);
        LocalDateTime evening = LocalDateTime.of(2024, 6, 15, 18, 45);

        match.setMatchTime(morning);
        assertThat(match.getMatchTime()).isEqualTo(morning);

        match.setMatchTime(afternoon);
        assertThat(match.getMatchTime()).isEqualTo(afternoon);

        match.setMatchTime(evening);
        assertThat(match.getMatchTime()).isEqualTo(evening);
    }

    @Test
    @DisplayName("Should handle different tournament rounds")
    void testTournamentRounds() {
        match.setRound("First Round");
        assertThat(match.getRound()).isEqualTo("First Round");

        match.setRound("Quarterfinals");
        assertThat(match.getRound()).isEqualTo("Quarterfinals");

        match.setRound("Semifinals");
        assertThat(match.getRound()).isEqualTo("Semifinals");

        match.setRound("Final");
        assertThat(match.getRound()).isEqualTo("Final");
    }

    @Test
    @DisplayName("Should support cascade operations for video")
    void testVideoCascadeRelationship() {
        // This tests the setup - actual cascade is tested in repository tests
        match.setVideo(video);

        assertThat(match.getVideo()).isNotNull();

        // Setting to null should work
        match.setVideo(null);
        assertThat(match.getVideo()).isNull();
    }

    @Test
    @DisplayName("Should handle null video in removeVideo helper")
    void testRemoveVideoWhenNull() {
        assertThat(match.getVideo()).isNull();

        // Should not throw exception
        match.removeVideo();

        assertThat(match.getVideo()).isNull();
    }
}
