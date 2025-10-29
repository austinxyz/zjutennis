package com.zjutennis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjutennis.dto.ImportResult;
import com.zjutennis.dto.PlayerSearchRequest;
import com.zjutennis.dto.PlayerSearchResponse;
import com.zjutennis.model.Player;
import com.zjutennis.model.PlayerSkillsHistory;
import com.zjutennis.service.PlayerService;
import com.zjutennis.service.PlayerSkillsHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
@DisplayName("PlayerController Tests")
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PlayerSkillsHistoryService playerSkillsHistoryService;

    private Player testPlayer1;
    private Player testPlayer2;
    private PlayerSearchRequest searchRequest;
    private PlayerSearchResponse searchResponse;

    @BeforeEach
    void setUp() {
        testPlayer1 = new Player();
        testPlayer1.setId(1L);
        testPlayer1.setName("John Doe");
        testPlayer1.setEmail("john@example.com");
        testPlayer1.setCity("Hangzhou");
        testPlayer1.setCountry("China");
        testPlayer1.setGender("male");

        testPlayer2 = new Player();
        testPlayer2.setId(2L);
        testPlayer2.setName("Jane Smith");
        testPlayer2.setEmail("jane@example.com");
        testPlayer2.setCity("Beijing");
        testPlayer2.setCountry("China");
        testPlayer2.setGender("female");

        searchRequest = new PlayerSearchRequest();
        searchRequest.setName("john");
        searchRequest.setPage(1);
        searchRequest.setPageSize(25);

        searchResponse = new PlayerSearchResponse(
                Arrays.asList(testPlayer1),
                1L,
                1,
                25
        );
    }

    @Test
    @DisplayName("Should get all players successfully")
    void testGetAllPlayers() throws Exception {
        // Arrange
        when(playerService.getAllPlayers()).thenReturn(Arrays.asList(testPlayer1, testPlayer2));

        // Act & Assert
        mockMvc.perform(get("/api/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(playerService, times(1)).getAllPlayers();
    }

    @Test
    @DisplayName("Should return empty list when no players exist")
    void testGetAllPlayersEmpty() throws Exception {
        // Arrange
        when(playerService.getAllPlayers()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/players")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should search players successfully")
    void testSearchPlayers() throws Exception {
        // Arrange
        when(playerService.searchPlayers(any(PlayerSearchRequest.class))).thenReturn(searchResponse);

        // Act & Assert
        mockMvc.perform(post("/api/players/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", hasSize(1)))
                .andExpect(jsonPath("$.players[0].name").value("John Doe"))
                .andExpect(jsonPath("$.totalCount").value(1))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.pageSize").value(25));

        verify(playerService, times(1)).searchPlayers(any(PlayerSearchRequest.class));
    }

    @Test
    @DisplayName("Should get player by ID successfully")
    void testGetPlayerById() throws Exception {
        // Arrange
        when(playerService.getPlayerById(1L)).thenReturn(Optional.of(testPlayer1));

        // Act & Assert
        mockMvc.perform(get("/api/players/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(playerService, times(1)).getPlayerById(1L);
    }

    @Test
    @DisplayName("Should return 404 when player not found by ID")
    void testGetPlayerByIdNotFound() throws Exception {
        // Arrange
        when(playerService.getPlayerById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/players/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(playerService, times(1)).getPlayerById(999L);
    }

    @Test
    @DisplayName("Should get player by email successfully")
    void testGetPlayerByEmail() throws Exception {
        // Arrange
        when(playerService.getPlayerByEmail("john@example.com")).thenReturn(Optional.of(testPlayer1));

        // Act & Assert
        mockMvc.perform(get("/api/players/email/john@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(playerService, times(1)).getPlayerByEmail("john@example.com");
    }

    @Test
    @DisplayName("Should return 404 when player not found by email")
    void testGetPlayerByEmailNotFound() throws Exception {
        // Arrange
        when(playerService.getPlayerByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/players/email/notfound@example.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get players by city successfully")
    void testGetPlayersByCity() throws Exception {
        // Arrange
        when(playerService.getPlayersByCity("Hangzhou")).thenReturn(Arrays.asList(testPlayer1));

        // Act & Assert
        mockMvc.perform(get("/api/players/city/Hangzhou")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].city").value("Hangzhou"));

        verify(playerService, times(1)).getPlayersByCity("Hangzhou");
    }

    @Test
    @DisplayName("Should create player successfully")
    void testCreatePlayer() throws Exception {
        // Arrange
        when(playerService.createPlayer(any(Player.class))).thenReturn(testPlayer1);

        // Act & Assert
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayer1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(playerService, times(1)).createPlayer(any(Player.class));
    }

    @Test
    @DisplayName("Should update player successfully")
    void testUpdatePlayer() throws Exception {
        // Arrange
        Player updatedPlayer = new Player();
        updatedPlayer.setId(1L);
        updatedPlayer.setName("John Updated");
        updatedPlayer.setEmail("john.updated@example.com");

        when(playerService.updatePlayer(eq(1L), any(Player.class))).thenReturn(updatedPlayer);

        // Act & Assert
        mockMvc.perform(put("/api/players/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPlayer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));

        verify(playerService, times(1)).updatePlayer(eq(1L), any(Player.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent player")
    void testUpdatePlayerNotFound() throws Exception {
        // Arrange
        when(playerService.updatePlayer(eq(999L), any(Player.class)))
                .thenThrow(new RuntimeException("Player not found with id: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/players/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPlayer1)))
                .andExpect(status().isNotFound());

        verify(playerService, times(1)).updatePlayer(eq(999L), any(Player.class));
    }

    @Test
    @DisplayName("Should get player skills history successfully")
    void testGetPlayerSkillsHistory() throws Exception {
        // Arrange
        PlayerSkillsHistory history1 = new PlayerSkillsHistory();
        history1.setId(1L);
        history1.setStrengths("Powerful serve");
        history1.setWeaknesses("Backhand");
        history1.setRecordedAt(LocalDateTime.now());

        PlayerSkillsHistory history2 = new PlayerSkillsHistory();
        history2.setId(2L);
        history2.setStrengths("Improved backhand");
        history2.setWeaknesses("Net play");
        history2.setRecordedAt(LocalDateTime.now());

        List<PlayerSkillsHistory> historyList = Arrays.asList(history1, history2);
        when(playerSkillsHistoryService.getPlayerSkillsHistory(1L)).thenReturn(historyList);

        // Act & Assert
        mockMvc.perform(get("/api/players/1/skills-history")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].strengths").value("Powerful serve"))
                .andExpect(jsonPath("$[1].strengths").value("Improved backhand"));

        verify(playerSkillsHistoryService, times(1)).getPlayerSkillsHistory(1L);
    }

    @Test
    @DisplayName("Should import players from CSV successfully")
    void testImportPlayersSuccess() throws Exception {
        // Arrange
        String csvContent = "Player ID,Name,Gender\n1,John Doe,male\n2,Jane Smith,female";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "players.csv",
                "text/csv",
                csvContent.getBytes()
        );

        ImportResult result = new ImportResult(2, 0, "Import completed: 2 players updated, 0 errors occurred.");
        when(playerService.importPlayersFromCSV(any())).thenReturn(result);

        // Act & Assert
        mockMvc.perform(multipart("/api/players/import")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successCount").value(2))
                .andExpect(jsonPath("$.errorCount").value(0))
                .andExpect(jsonPath("$.message").value(containsString("2 players updated")));

        verify(playerService, times(1)).importPlayersFromCSV(any());
    }

    @Test
    @DisplayName("Should return 400 when importing empty file")
    void testImportPlayersEmptyFile() throws Exception {
        // Arrange
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.csv",
                "text/csv",
                new byte[0]
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/players/import")
                        .file(emptyFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("File is empty"));

        verify(playerService, never()).importPlayersFromCSV(any());
    }

    @Test
    @DisplayName("Should return 400 when importing non-CSV file")
    void testImportPlayersInvalidFileType() throws Exception {
        // Arrange
        MockMultipartFile txtFile = new MockMultipartFile(
                "file",
                "players.txt",
                "text/plain",
                "some content".getBytes()
        );

        // Act & Assert
        mockMvc.perform(multipart("/api/players/import")
                        .file(txtFile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("File must be a CSV file"));

        verify(playerService, never()).importPlayersFromCSV(any());
    }

    @Test
    @DisplayName("Should return 500 when import fails")
    void testImportPlayersException() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "players.csv",
                "text/csv",
                "Player ID,Name\n1,John".getBytes()
        );

        when(playerService.importPlayersFromCSV(any())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        mockMvc.perform(multipart("/api/players/import")
                        .file(file))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(containsString("Import failed")));
    }

    @Test
    @DisplayName("Should handle search with no results")
    void testSearchPlayersNoResults() throws Exception {
        // Arrange
        PlayerSearchResponse emptyResponse = new PlayerSearchResponse(
                Collections.emptyList(),
                0L,
                1,
                25
        );
        when(playerService.searchPlayers(any(PlayerSearchRequest.class))).thenReturn(emptyResponse);

        // Act & Assert
        mockMvc.perform(post("/api/players/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", hasSize(0)))
                .andExpect(jsonPath("$.totalCount").value(0));
    }

    @Test
    @DisplayName("Should handle search with multiple filters")
    void testSearchPlayersWithFilters() throws Exception {
        // Arrange
        PlayerSearchRequest complexRequest = new PlayerSearchRequest();
        complexRequest.setName("john");
        complexRequest.setGender("male");
        complexRequest.setCity("hangzhou");
        complexRequest.setUtrMin(10.0);
        complexRequest.setUtrMax(15.0);

        when(playerService.searchPlayers(any(PlayerSearchRequest.class))).thenReturn(searchResponse);

        // Act & Assert
        mockMvc.perform(post("/api/players/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(complexRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.players", hasSize(1)));

        verify(playerService, times(1)).searchPlayers(any(PlayerSearchRequest.class));
    }

    @Test
    @DisplayName("Should handle CORS preflight request")
    void testCORSSupport() throws Exception {
        // Act & Assert
        mockMvc.perform(options("/api/players")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://localhost:3000"))
                .andExpect(status().isOk());
    }
}
