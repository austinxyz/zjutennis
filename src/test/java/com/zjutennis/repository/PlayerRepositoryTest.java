package com.zjutennis.repository;

import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerAlumni;
import com.zjutennis.model.PlayerSkills;
import com.zjutennis.model.PlayerStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("PlayerRepository Tests")
class PlayerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlayerRepository playerRepository;

    private Player testPlayer1;
    private Player testPlayer2;
    private Player testPlayer3;

    @BeforeEach
    void setUp() {
        // Setup test player 1 - Male, Hangzhou
        testPlayer1 = new Player();
        testPlayer1.setName("John Doe");
        testPlayer1.setEmail("john@example.com");
        testPlayer1.setCity("Hangzhou");
        testPlayer1.setCountry("China");
        testPlayer1.setPhoneNumber("+86-1234567890");
        testPlayer1.setGender("male");

        PlayerStatistics stats1 = new PlayerStatistics();
        stats1.setUtrRating(12.5);
        stats1.setUtrStatus("Verified");
        stats1.setUtrUpdatedDate(LocalDateTime.now());
        stats1.setNtrpRating(5.0);
        stats1.setNtrpStatus("Verified");
        stats1.setDynamicRating(12.8);
        stats1.setWinRate(75.0);
        stats1.setPlayer(testPlayer1);
        testPlayer1.setStatistics(stats1);

        PlayerSkills skills1 = new PlayerSkills();
        skills1.setStrengths("Powerful serve, Strong forehand");
        skills1.setWeaknesses("Backhand consistency");
        skills1.setPlayer(testPlayer1);
        testPlayer1.setSkills(skills1);

        PlayerAlumni alumni1 = new PlayerAlumni();
        alumni1.setGraduationUniversity1("Zhejiang University");
        alumni1.setGraduationYear1(2020);
        alumni1.setPlayer(testPlayer1);
        testPlayer1.setAlumni(alumni1);

        // Setup test player 2 - Female, Beijing
        testPlayer2 = new Player();
        testPlayer2.setName("Jane Smith");
        testPlayer2.setEmail("jane@example.com");
        testPlayer2.setCity("Beijing");
        testPlayer2.setCountry("China");
        testPlayer2.setPhoneNumber("+86-9876543210");
        testPlayer2.setGender("female");

        PlayerStatistics stats2 = new PlayerStatistics();
        stats2.setUtrRating(8.0);
        stats2.setUtrStatus("Verified");
        stats2.setNtrpRating(4.0);
        stats2.setWinRate(60.0);
        stats2.setPlayer(testPlayer2);
        testPlayer2.setStatistics(stats2);

        // Setup test player 3 - Male, Hangzhou (same city as player 1)
        testPlayer3 = new Player();
        testPlayer3.setName("Bob Wilson");
        testPlayer3.setEmail("bob@example.com");
        testPlayer3.setCity("Hangzhou");
        testPlayer3.setCountry("USA");
        testPlayer3.setGender("male");

        entityManager.persist(testPlayer1);
        entityManager.persist(testPlayer2);
        entityManager.persist(testPlayer3);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Should find player by email")
    void testFindByEmail() {
        // Act
        Optional<Player> result = playerRepository.findByEmail("john@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
        assertThat(result.get().getCity()).isEqualTo("Hangzhou");
    }

    @Test
    @DisplayName("Should return empty when email not found")
    void testFindByEmailNotFound() {
        // Act
        Optional<Player> result = playerRepository.findByEmail("notfound@example.com");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find player by email case-sensitive")
    void testFindByEmailCaseSensitive() {
        // Act
        Optional<Player> result = playerRepository.findByEmail("JOHN@EXAMPLE.COM");

        // Assert
        // Email search should be case-sensitive by default in JPA
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should find players by city")
    void testFindByCity() {
        // Act
        List<Player> results = playerRepository.findByCity("Hangzhou");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting("city").containsOnly("Hangzhou");
        assertThat(results).extracting("name").containsExactlyInAnyOrder("John Doe", "Bob Wilson");
    }

    @Test
    @DisplayName("Should return empty list when city not found")
    void testFindByCityNotFound() {
        // Act
        List<Player> results = playerRepository.findByCity("Shanghai");

        // Assert
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should find single player by city")
    void testFindByCitySingleResult() {
        // Act
        List<Player> results = playerRepository.findByCity("Beijing");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Jane Smith");
        assertThat(results.get(0).getCity()).isEqualTo("Beijing");
    }

    @Test
    @DisplayName("Should save new player with all relationships")
    void testSavePlayerWithRelationships() {
        // Arrange
        Player newPlayer = new Player();
        newPlayer.setName("Alice Chen");
        newPlayer.setEmail("alice@example.com");
        newPlayer.setCity("Shanghai");
        newPlayer.setCountry("China");
        newPlayer.setGender("female");

        PlayerStatistics stats = new PlayerStatistics();
        stats.setUtrRating(10.0);
        stats.setNtrpRating(4.5);
        stats.setWinRate(65.0);
        stats.setPlayer(newPlayer);
        newPlayer.setStatistics(stats);

        PlayerSkills skills = new PlayerSkills();
        skills.setStrengths("All-around player");
        skills.setWeaknesses("Net play");
        skills.setPlayer(newPlayer);
        newPlayer.setSkills(skills);

        // Act
        Player savedPlayer = playerRepository.save(newPlayer);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Player foundPlayer = playerRepository.findById(savedPlayer.getId()).orElseThrow();
        assertThat(foundPlayer.getName()).isEqualTo("Alice Chen");
        assertThat(foundPlayer.getStatistics()).isNotNull();
        assertThat(foundPlayer.getStatistics().getUtrRating()).isEqualTo(10.0);
        assertThat(foundPlayer.getSkills()).isNotNull();
        assertThat(foundPlayer.getSkills().getStrengths()).isEqualTo("All-around player");
    }

    @Test
    @DisplayName("Should update existing player")
    void testUpdatePlayer() {
        // Arrange
        Player player = playerRepository.findByEmail("john@example.com").orElseThrow();
        player.setName("John Updated");
        player.setCity("Shanghai");

        // Act
        playerRepository.save(player);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Player updatedPlayer = playerRepository.findById(player.getId()).orElseThrow();
        assertThat(updatedPlayer.getName()).isEqualTo("John Updated");
        assertThat(updatedPlayer.getCity()).isEqualTo("Shanghai");
        assertThat(updatedPlayer.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should delete player")
    void testDeletePlayer() {
        // Arrange
        Player player = playerRepository.findByEmail("bob@example.com").orElseThrow();
        Long playerId = player.getId();

        // Act
        playerRepository.delete(player);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Optional<Player> deletedPlayer = playerRepository.findById(playerId);
        assertThat(deletedPlayer).isEmpty();
    }

    @Test
    @DisplayName("Should find all players")
    void testFindAll() {
        // Act
        List<Player> players = playerRepository.findAll();

        // Assert
        assertThat(players).hasSize(3);
        assertThat(players).extracting("name")
                .containsExactlyInAnyOrder("John Doe", "Jane Smith", "Bob Wilson");
    }

    @Test
    @DisplayName("Should find player by ID")
    void testFindById() {
        // Arrange
        Player player = playerRepository.findByEmail("jane@example.com").orElseThrow();
        Long playerId = player.getId();

        // Act
        Optional<Player> result = playerRepository.findById(playerId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Jane Smith");
        assertThat(result.get().getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @DisplayName("Should count all players")
    void testCount() {
        // Act
        long count = playerRepository.count();

        // Assert
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("Should check if player exists by ID")
    void testExistsById() {
        // Arrange
        Player player = playerRepository.findByEmail("john@example.com").orElseThrow();

        // Act & Assert
        assertThat(playerRepository.existsById(player.getId())).isTrue();
        assertThat(playerRepository.existsById(9999L)).isFalse();
    }

    @Test
    @DisplayName("Should load player with statistics eagerly")
    void testLoadPlayerWithStatistics() {
        // Act
        Optional<Player> result = playerRepository.findByEmail("john@example.com");

        // Assert
        assertThat(result).isPresent();
        Player player = result.get();
        assertThat(player.getStatistics()).isNotNull();
        assertThat(player.getStatistics().getUtrRating()).isEqualTo(12.5);
        assertThat(player.getStatistics().getNtrpRating()).isEqualTo(5.0);
        assertThat(player.getStatistics().getWinRate()).isEqualTo(75.0);
    }

    @Test
    @DisplayName("Should load player with skills eagerly")
    void testLoadPlayerWithSkills() {
        // Act
        Optional<Player> result = playerRepository.findByEmail("john@example.com");

        // Assert
        assertThat(result).isPresent();
        Player player = result.get();
        assertThat(player.getSkills()).isNotNull();
        assertThat(player.getSkills().getStrengths()).contains("Powerful serve");
        assertThat(player.getSkills().getWeaknesses()).contains("Backhand");
    }

    @Test
    @DisplayName("Should load player with alumni information")
    void testLoadPlayerWithAlumni() {
        // Act
        Optional<Player> result = playerRepository.findByEmail("john@example.com");

        // Assert
        assertThat(result).isPresent();
        Player player = result.get();
        assertThat(player.getAlumni()).isNotNull();
        assertThat(player.getAlumni().getGraduationUniversity1()).isEqualTo("Zhejiang University");
        assertThat(player.getAlumni().getGraduationYear1()).isEqualTo(2020);
    }

    @Test
    @DisplayName("Should handle player without statistics")
    void testPlayerWithoutStatistics() {
        // Arrange
        Player player = playerRepository.findByEmail("bob@example.com").orElseThrow();

        // Assert
        assertThat(player.getStatistics()).isNull();
    }

    @Test
    @DisplayName("Should update player statistics")
    void testUpdatePlayerStatistics() {
        // Arrange
        Player player = playerRepository.findByEmail("john@example.com").orElseThrow();
        player.getStatistics().setUtrRating(13.0);
        player.getStatistics().setWinRate(80.0);

        // Act
        playerRepository.save(player);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Player updatedPlayer = playerRepository.findById(player.getId()).orElseThrow();
        assertThat(updatedPlayer.getStatistics().getUtrRating()).isEqualTo(13.0);
        assertThat(updatedPlayer.getStatistics().getWinRate()).isEqualTo(80.0);
    }

    @Test
    @DisplayName("Should handle null values in optional fields")
    void testPlayerWithNullOptionalFields() {
        // Arrange
        Player minimalPlayer = new Player();
        minimalPlayer.setName("Minimal Player");
        minimalPlayer.setEmail("minimal@example.com");
        // City, country, phoneNumber, gender are null

        // Act
        Player savedPlayer = playerRepository.save(minimalPlayer);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Player foundPlayer = playerRepository.findById(savedPlayer.getId()).orElseThrow();
        assertThat(foundPlayer.getName()).isEqualTo("Minimal Player");
        assertThat(foundPlayer.getEmail()).isEqualTo("minimal@example.com");
        assertThat(foundPlayer.getCity()).isNull();
        assertThat(foundPlayer.getCountry()).isNull();
        assertThat(foundPlayer.getPhoneNumber()).isNull();
    }

    @Test
    @DisplayName("Should maintain referential integrity with cascade operations")
    void testCascadeOperations() {
        // Arrange
        Player player = new Player();
        player.setName("Cascade Test");
        player.setEmail("cascade@example.com");

        PlayerStatistics stats = new PlayerStatistics();
        stats.setUtrRating(9.0);
        stats.setPlayer(player);
        player.setStatistics(stats);

        // Act - Save player should cascade to statistics
        Player savedPlayer = playerRepository.save(player);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Player foundPlayer = playerRepository.findById(savedPlayer.getId()).orElseThrow();
        assertThat(foundPlayer.getStatistics()).isNotNull();
        assertThat(foundPlayer.getStatistics().getUtrRating()).isEqualTo(9.0);

        // Act - Delete player should cascade to statistics
        playerRepository.delete(foundPlayer);
        entityManager.flush();
        entityManager.clear();

        // Assert
        assertThat(playerRepository.findById(savedPlayer.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should find players by city with different case")
    void testFindByCityCaseSensitive() {
        // Act
        List<Player> lowerCase = playerRepository.findByCity("hangzhou");
        List<Player> upperCase = playerRepository.findByCity("HANGZHOU");
        List<Player> properCase = playerRepository.findByCity("Hangzhou");

        // Assert - JPA default is case-sensitive
        assertThat(lowerCase).isEmpty();
        assertThat(upperCase).isEmpty();
        assertThat(properCase).hasSize(2);
    }
}
