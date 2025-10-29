package com.zjutennis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Player Model Tests")
class PlayerTest {

    private Player player;
    private PlayerSkills skills;
    private PlayerStatistics statistics;
    private PlayerAlumni alumni;

    @BeforeEach
    void setUp() {
        // Setup Player
        player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setEmail("test@example.com");
        player.setCity("San Francisco");
        player.setCountry("United States");
        player.setPhoneNumber("1234567890");
        player.setGender("male");

        // Setup PlayerSkills
        skills = new PlayerSkills();
        skills.setId(1L);
        skills.setForehand(4.5);
        skills.setBackhand(4.0);
        skills.setServe(4.2);
        skills.setPlayer(player);

        // Setup PlayerStatistics
        statistics = new PlayerStatistics();
        statistics.setId(1L);
        statistics.setUtrRating(6.5);
        statistics.setTotalMatches(50);
        statistics.setWins(30);
        statistics.setLosses(20);
        statistics.setPlayer(player);

        // Setup PlayerAlumni
        alumni = new PlayerAlumni();
        alumni.setId(1L);
        alumni.setGraduationUniversity1("Zhejiang University");
        alumni.setGraduationYear1(2010);
        alumni.setPlayer(player);

        player.setSkills(skills);
        player.setStatistics(statistics);
        player.setAlumni(alumni);
    }

    @Test
    @DisplayName("Should create player with all basic fields")
    void testPlayerCreation() {
        assertThat(player).isNotNull();
        assertThat(player.getId()).isEqualTo(1L);
        assertThat(player.getName()).isEqualTo("Test Player");
        assertThat(player.getEmail()).isEqualTo("test@example.com");
        assertThat(player.getCity()).isEqualTo("San Francisco");
        assertThat(player.getCountry()).isEqualTo("United States");
        assertThat(player.getPhoneNumber()).isEqualTo("1234567890");
        assertThat(player.getGender()).isEqualTo("male");
    }

    @Test
    @DisplayName("Should maintain relationship with PlayerSkills")
    void testSkillsRelationship() {
        assertThat(player.getSkills()).isNotNull();
        assertThat(player.getSkills()).isEqualTo(skills);
        assertThat(player.getSkills().getForehand()).isEqualTo(4.5);
        assertThat(player.getSkills().getBackhand()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("Should maintain relationship with PlayerStatistics")
    void testStatisticsRelationship() {
        assertThat(player.getStatistics()).isNotNull();
        assertThat(player.getStatistics()).isEqualTo(statistics);
        assertThat(player.getStatistics().getUtrRating()).isEqualTo(6.5);
        assertThat(player.getStatistics().getTotalMatches()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should maintain relationship with PlayerAlumni")
    void testAlumniRelationship() {
        assertThat(player.getAlumni()).isNotNull();
        assertThat(player.getAlumni()).isEqualTo(alumni);
        assertThat(player.getAlumni().getGraduationUniversity1()).isEqualTo("Zhejiang University");
    }

    @Test
    @DisplayName("Should allow null optional fields")
    void testNullOptionalFields() {
        Player newPlayer = new Player();
        newPlayer.setName("Minimal Player");

        assertThat(newPlayer.getName()).isEqualTo("Minimal Player");
        assertThat(newPlayer.getEmail()).isNull();
        assertThat(newPlayer.getCity()).isNull();
        assertThat(newPlayer.getCountry()).isNull();
        assertThat(newPlayer.getPhoneNumber()).isNull();
        assertThat(newPlayer.getGender()).isNull();
        assertThat(newPlayer.getSkills()).isNull();
        assertThat(newPlayer.getStatistics()).isNull();
        assertThat(newPlayer.getAlumni()).isNull();
    }

    @Test
    @DisplayName("Should update player basic fields")
    void testUpdatePlayerFields() {
        player.setName("Updated Name");
        player.setEmail("updated@example.com");
        player.setCity("New York");
        player.setGender("female");

        assertThat(player.getName()).isEqualTo("Updated Name");
        assertThat(player.getEmail()).isEqualTo("updated@example.com");
        assertThat(player.getCity()).isEqualTo("New York");
        assertThat(player.getGender()).isEqualTo("female");
    }

    @Test
    @DisplayName("Should handle different gender values")
    void testGenderValues() {
        player.setGender("male");
        assertThat(player.getGender()).isEqualTo("male");

        player.setGender("female");
        assertThat(player.getGender()).isEqualTo("female");

        player.setGender("other");
        assertThat(player.getGender()).isEqualTo("other");
    }

    @Test
    @DisplayName("Should add video analyses")
    void testVideoAnalysesRelationship() {
        VideoAnalysis analysis1 = new VideoAnalysis();
        analysis1.setId(1L);
        analysis1.setPlayer(player);

        VideoAnalysis analysis2 = new VideoAnalysis();
        analysis2.setId(2L);
        analysis2.setPlayer(player);

        player.getVideoAnalyses().add(analysis1);
        player.getVideoAnalyses().add(analysis2);

        assertThat(player.getVideoAnalyses()).hasSize(2);
        assertThat(player.getVideoAnalyses()).containsExactly(analysis1, analysis2);
    }

    @Test
    @DisplayName("Should initialize with empty video analyses list")
    void testEmptyVideoAnalysesList() {
        Player newPlayer = new Player();
        assertThat(newPlayer.getVideoAnalyses()).isNotNull();
        assertThat(newPlayer.getVideoAnalyses()).isEmpty();
    }

    @Test
    @DisplayName("Should set all contact information")
    void testContactInformation() {
        player.setEmail("player@tennis.com");
        player.setPhoneNumber("+1-415-555-0123");
        player.setCity("Los Angeles");
        player.setCountry("USA");

        assertThat(player.getEmail()).isEqualTo("player@tennis.com");
        assertThat(player.getPhoneNumber()).isEqualTo("+1-415-555-0123");
        assertThat(player.getCity()).isEqualTo("Los Angeles");
        assertThat(player.getCountry()).isEqualTo("USA");
    }

    @Test
    @DisplayName("Should handle international phone formats")
    void testInternationalPhoneFormats() {
        player.setPhoneNumber("+86-138-0000-0000");
        assertThat(player.getPhoneNumber()).isEqualTo("+86-138-0000-0000");

        player.setPhoneNumber("(408) 555-1234");
        assertThat(player.getPhoneNumber()).isEqualTo("(408) 555-1234");
    }

    @Test
    @DisplayName("Should handle email validation format")
    void testEmailFormat() {
        player.setEmail("valid.email@domain.com");
        assertThat(player.getEmail()).isEqualTo("valid.email@domain.com");

        player.setEmail("user+tag@example.co.uk");
        assertThat(player.getEmail()).isEqualTo("user+tag@example.co.uk");
    }

    @Test
    @DisplayName("Should support cascade operations for dependent entities")
    void testCascadeRelationships() {
        // This tests the setup - actual cascade is tested in repository tests
        player.setSkills(skills);
        player.setStatistics(statistics);
        player.setAlumni(alumni);

        assertThat(player.getSkills()).isNotNull();
        assertThat(player.getStatistics()).isNotNull();
        assertThat(player.getAlumni()).isNotNull();

        // Setting to null should work
        player.setSkills(null);
        assertThat(player.getSkills()).isNull();
    }
}
