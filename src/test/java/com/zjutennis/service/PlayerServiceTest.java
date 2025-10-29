package com.zjutennis.service;

import com.zjutennis.dto.ImportResult;
import com.zjutennis.dto.PlayerSearchRequest;
import com.zjutennis.dto.PlayerSearchResponse;
import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerAlumni;
import com.zjutennis.model.PlayerSkills;
import com.zjutennis.model.PlayerSkillsHistory;
import com.zjutennis.model.PlayerStatistics;
import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.repository.PlayerSkillsHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlayerService Tests")
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private PlayerSkillsHistoryRepository playerSkillsHistoryRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player testPlayer1;
    private Player testPlayer2;
    private Player testPlayer3;

    @BeforeEach
    void setUp() {
        // Setup test player 1 - Male, high UTR
        testPlayer1 = new Player();
        testPlayer1.setId(1L);
        testPlayer1.setName("John Doe");
        testPlayer1.setEmail("john@example.com");
        testPlayer1.setCity("Hangzhou");
        testPlayer1.setCountry("China");
        testPlayer1.setGender("male");

        PlayerStatistics stats1 = new PlayerStatistics();
        stats1.setUtrRating(12.5);
        stats1.setNtrpRating(5.0);
        stats1.setWinRate(75.0);
        testPlayer1.setStatistics(stats1);

        PlayerAlumni alumni1 = new PlayerAlumni();
        alumni1.setGraduationUniversity1("Zhejiang University");
        testPlayer1.setAlumni(alumni1);

        PlayerSkills skills1 = new PlayerSkills();
        skills1.setStrengths("Powerful serve");
        skills1.setWeaknesses("Backhand");
        testPlayer1.setSkills(skills1);

        // Setup test player 2 - Female, medium UTR
        testPlayer2 = new Player();
        testPlayer2.setId(2L);
        testPlayer2.setName("Jane Smith");
        testPlayer2.setEmail("jane@example.com");
        testPlayer2.setCity("Beijing");
        testPlayer2.setCountry("China");
        testPlayer2.setGender("female");

        PlayerStatistics stats2 = new PlayerStatistics();
        stats2.setUtrRating(8.0);
        stats2.setNtrpRating(4.0);
        stats2.setWinRate(60.0);
        testPlayer2.setStatistics(stats2);

        // Setup test player 3 - Male, no statistics
        testPlayer3 = new Player();
        testPlayer3.setId(3L);
        testPlayer3.setName("Bob Wilson");
        testPlayer3.setEmail("bob@example.com");
        testPlayer3.setCity("Shanghai");
        testPlayer3.setCountry("China");
        testPlayer3.setGender("male");
    }

    @Test
    @DisplayName("Should return all players")
    void testGetAllPlayers() {
        // Arrange
        List<Player> players = Arrays.asList(testPlayer1, testPlayer2, testPlayer3);
        when(playerRepository.findAll()).thenReturn(players);

        // Act
        List<Player> result = playerService.getAllPlayers();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly(testPlayer1, testPlayer2, testPlayer3);
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no players exist")
    void testGetAllPlayersWhenEmpty() {
        // Arrange
        when(playerRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Player> result = playerService.getAllPlayers();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should search players by name")
    void testSearchPlayersByName() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setName("john");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(1);
        assertThat(response.getPlayers().get(0).getName()).isEqualTo("John Doe");
        assertThat(response.getTotalCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should search players by gender")
    void testSearchPlayersByGender() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setGender("female");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(1);
        assertThat(response.getPlayers().get(0).getGender()).isEqualTo("female");
    }

    @Test
    @DisplayName("Should search players by UTR range")
    void testSearchPlayersByUtrRange() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setUtrMin(8.0);
        request.setUtrMax(13.0);
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(2);
        assertThat(response.getPlayers()).extracting("name")
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    @DisplayName("Should search players by NTRP rating")
    void testSearchPlayersByNtrp() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setNtrp(5.0);
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(1);
        assertThat(response.getPlayers().get(0).getStatistics().getNtrpRating()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("Should search players by win rate range")
    void testSearchPlayersByWinRateRange() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setWinRateMin(70.0);
        request.setWinRateMax(80.0);
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(1);
        assertThat(response.getPlayers().get(0).getStatistics().getWinRate()).isEqualTo(75.0);
    }

    @Test
    @DisplayName("Should search players by university")
    void testSearchPlayersByUniversity() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setUniversity("zhejiang");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(1);
        assertThat(response.getPlayers().get(0).getAlumni().getGraduationUniversity1())
                .contains("Zhejiang");
    }

    @Test
    @DisplayName("Should search players by city")
    void testSearchPlayersByCity() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setCity("beijing");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(1);
        assertThat(response.getPlayers().get(0).getCity()).isEqualTo("Beijing");
    }

    @Test
    @DisplayName("Should search players by country")
    void testSearchPlayersByCountry() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setCountry("china");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(3);
    }

    @Test
    @DisplayName("Should search players with multiple filters")
    void testSearchPlayersWithMultipleFilters() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setGender("male");
        request.setUtrMin(10.0);
        request.setCity("hangzhou");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(1);
        assertThat(response.getPlayers().get(0).getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should sort players by UTR ascending")
    void testSortPlayersByUtrAscending() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setSortBy("utr");
        request.setSortOrder("asc");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(3);
        assertThat(response.getPlayers().get(0).getName()).isEqualTo("Bob Wilson"); // No stats (sorted as -1)
        assertThat(response.getPlayers().get(1).getName()).isEqualTo("Jane Smith"); // 8.0
        assertThat(response.getPlayers().get(2).getName()).isEqualTo("John Doe"); // 12.5
    }

    @Test
    @DisplayName("Should sort players by UTR descending")
    void testSortPlayersByUtrDescending() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setSortBy("utr");
        request.setSortOrder("desc");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(3);
        assertThat(response.getPlayers().get(0).getName()).isEqualTo("John Doe"); // 12.5
        assertThat(response.getPlayers().get(1).getName()).isEqualTo("Jane Smith"); // 8.0
    }

    @Test
    @DisplayName("Should sort players by gender")
    void testSortPlayersByGender() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setSortBy("gender");
        request.setSortOrder("asc");
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers().get(0).getGender()).isEqualTo("female");
        assertThat(response.getPlayers().get(1).getGender()).isEqualTo("male");
    }

    @Test
    @DisplayName("Should paginate search results")
    void testPaginateSearchResults() {
        // Arrange
        List<Player> manyPlayers = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Player p = new Player();
            p.setId((long) i);
            p.setName("Player " + i);
            manyPlayers.add(p);
        }

        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setPage(2);
        request.setPageSize(10);
        when(playerRepository.findAll()).thenReturn(manyPlayers);

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(10);
        assertThat(response.getTotalCount()).isEqualTo(30);
        assertThat(response.getPage()).isEqualTo(2);
        assertThat(response.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should use default pagination when not specified")
    void testDefaultPagination() {
        // Arrange
        List<Player> players = Arrays.asList(testPlayer1, testPlayer2);
        PlayerSearchRequest request = new PlayerSearchRequest();
        when(playerRepository.findAll()).thenReturn(players);

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getPageSize()).isEqualTo(25);
    }

    @Test
    @DisplayName("Should get player by ID")
    void testGetPlayerById() {
        // Arrange
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer1));

        // Act
        Optional<Player> result = playerService.getPlayerById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should return empty when player not found by ID")
    void testGetPlayerByIdNotFound() {
        // Arrange
        when(playerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Player> result = playerService.getPlayerById(999L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get player by email")
    void testGetPlayerByEmail() {
        // Arrange
        when(playerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testPlayer1));

        // Act
        Optional<Player> result = playerService.getPlayerByEmail("john@example.com");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should get players by city")
    void testGetPlayersByCity() {
        // Arrange
        when(playerRepository.findByCity("Hangzhou")).thenReturn(Arrays.asList(testPlayer1));

        // Act
        List<Player> result = playerService.getPlayersByCity("Hangzhou");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCity()).isEqualTo("Hangzhou");
    }

    @Test
    @DisplayName("Should create new player")
    void testCreatePlayer() {
        // Arrange
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer1);

        // Act
        Player result = playerService.createPlayer(testPlayer1);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(playerRepository, times(1)).save(testPlayer1);
    }

    @Test
    @DisplayName("Should update player basic info")
    void testUpdatePlayerBasicInfo() {
        // Arrange
        Player updatedDetails = new Player();
        updatedDetails.setName("John Updated");
        updatedDetails.setEmail("john.new@example.com");
        updatedDetails.setCity("Shanghai");
        updatedDetails.setCountry("USA");
        updatedDetails.setPhoneNumber("+1234567890");
        updatedDetails.setGender("male");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer1));
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer1);

        // Act
        Player result = playerService.updatePlayer(1L, updatedDetails);

        // Assert
        assertThat(result.getName()).isEqualTo("John Updated");
        assertThat(result.getEmail()).isEqualTo("john.new@example.com");
        assertThat(result.getCity()).isEqualTo("Shanghai");
        verify(playerRepository, times(1)).save(testPlayer1);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent player")
    void testUpdatePlayerNotFound() {
        // Arrange
        when(playerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> playerService.updatePlayer(999L, testPlayer1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Player not found with id: 999");
    }

    @Test
    @DisplayName("Should create skills history when strengths change")
    void testUpdatePlayerCreatesSkillsHistory() {
        // Arrange
        Player updatedDetails = new Player();
        PlayerSkills newSkills = new PlayerSkills();
        newSkills.setStrengths("New strength");
        newSkills.setWeaknesses("Backhand");
        updatedDetails.setSkills(newSkills);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer1));
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer1);

        // Act
        playerService.updatePlayer(1L, updatedDetails);

        // Assert
        ArgumentCaptor<PlayerSkillsHistory> historyCaptor = ArgumentCaptor.forClass(PlayerSkillsHistory.class);
        verify(playerSkillsHistoryRepository, times(1)).save(historyCaptor.capture());

        PlayerSkillsHistory savedHistory = historyCaptor.getValue();
        assertThat(savedHistory.getStrengths()).isEqualTo("New strength");
        assertThat(savedHistory.getWeaknesses()).isEqualTo("Backhand");
    }

    @Test
    @DisplayName("Should not create skills history when skills unchanged")
    void testUpdatePlayerNoSkillsHistory() {
        // Arrange
        Player updatedDetails = new Player();
        PlayerSkills sameSkills = new PlayerSkills();
        sameSkills.setStrengths("Powerful serve");
        sameSkills.setWeaknesses("Backhand");
        updatedDetails.setSkills(sameSkills);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer1));
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer1);

        // Act
        playerService.updatePlayer(1L, updatedDetails);

        // Assert
        verify(playerSkillsHistoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update UTR updated date when UTR changes")
    void testUpdatePlayerUpdatesUtrDate() {
        // Arrange
        Player updatedDetails = new Player();
        PlayerStatistics newStats = new PlayerStatistics();
        newStats.setUtrRating(13.0); // Different from current 12.5
        updatedDetails.setStatistics(newStats);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer1));
        when(playerRepository.save(any(Player.class))).thenReturn(testPlayer1);

        // Act
        playerService.updatePlayer(1L, updatedDetails);

        // Assert
        assertThat(newStats.getUtrUpdatedDate()).isNotNull();
        assertThat(newStats.getUtrUpdatedDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should import players from CSV successfully")
    void testImportPlayersFromCSV() throws IOException {
        // Arrange
        String csvContent = "Player ID,Name,Gender,UTR Rating,UTR Status,NTRP Rating,NTRP Status,Dynamic Rating,Win Rate\n" +
                            "1,John Doe,male,12.5,Verified,5.0,Verified,12.8,75%\n" +
                            "2,Jane Smith,female,8.0,Verified,4.0,Verified,8.2,60%";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "players.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer1));
        when(playerRepository.findById(2L)).thenReturn(Optional.of(testPlayer2));
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ImportResult result = playerService.importPlayersFromCSV(file);

        // Assert
        assertThat(result.getSuccessCount()).isEqualTo(2);
        assertThat(result.getErrorCount()).isEqualTo(0);
        verify(playerRepository, atLeast(2)).save(any(Player.class));
    }

    @Test
    @DisplayName("Should handle errors in CSV import")
    void testImportPlayersFromCSVWithErrors() throws IOException {
        // Arrange
        String csvContent = "Player ID,Name,Gender,UTR Rating\n" +
                            "999,Invalid Player,male,12.5\n" +  // Non-existent player
                            "-,Missing ID,male,8.0";             // Invalid ID

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "players.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(playerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ImportResult result = playerService.importPlayersFromCSV(file);

        // Assert
        assertThat(result.getErrorCount()).isEqualTo(2);
        assertThat(result.getSuccessCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should update UTR date when importing from CSV")
    void testImportCSVUpdatesUtrDate() throws IOException {
        // Arrange
        String csvContent = "Player ID,Name,UTR Rating\n1,John Doe,13.0";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "players.csv",
                "text/csv",
                csvContent.getBytes()
        );

        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer1));
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ImportResult result = playerService.importPlayersFromCSV(file);

        // Assert
        assertThat(result.getSuccessCount()).isEqualTo(1);
        assertThat(testPlayer1.getStatistics().getUtrUpdatedDate()).isNotNull();
    }

    @Test
    @DisplayName("Should handle empty CSV file")
    void testImportEmptyCSV() throws IOException {
        // Arrange
        String csvContent = "";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                csvContent.getBytes()
        );

        // Act
        ImportResult result = playerService.importPlayersFromCSV(file);

        // Assert
        assertThat(result.getSuccessCount()).isEqualTo(0);
        assertThat(result.getErrorCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should filter out players without statistics when searching by UTR")
    void testSearchFiltersPlayersWithoutStatistics() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setUtrMin(5.0);
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2, testPlayer3));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).hasSize(2);
        assertThat(response.getPlayers()).extracting("name")
                .containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    @DisplayName("Should return empty page when page number exceeds results")
    void testPaginationBeyondResults() {
        // Arrange
        PlayerSearchRequest request = new PlayerSearchRequest();
        request.setPage(10);
        request.setPageSize(10);
        when(playerRepository.findAll()).thenReturn(Arrays.asList(testPlayer1, testPlayer2));

        // Act
        PlayerSearchResponse response = playerService.searchPlayers(request);

        // Assert
        assertThat(response.getPlayers()).isEmpty();
        assertThat(response.getTotalCount()).isEqualTo(2);
    }
}
