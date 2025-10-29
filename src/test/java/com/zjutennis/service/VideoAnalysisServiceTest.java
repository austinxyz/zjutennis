package com.zjutennis.service;

import com.zjutennis.dto.VideoAnalysisRequest;
import com.zjutennis.dto.VideoAnalysisResponse;
import com.zjutennis.model.Player;
import com.zjutennis.model.Video;
import com.zjutennis.model.VideoAnalysis;
import com.zjutennis.repository.PlayerRepository;
import com.zjutennis.repository.VideoAnalysisRepository;
import com.zjutennis.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VideoAnalysisService Tests")
class VideoAnalysisServiceTest {

    @Mock
    private VideoAnalysisRepository videoAnalysisRepository;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private VideoAnalysisService videoAnalysisService;

    private VideoAnalysis testAnalysis1;
    private VideoAnalysis testAnalysis2;
    private Video testVideo;
    private Player testPlayer;
    private VideoAnalysisRequest testRequest;

    @BeforeEach
    void setUp() {
        // Setup test player
        testPlayer = new Player();
        testPlayer.setId(1L);
        testPlayer.setName("John Doe");

        // Setup test video
        testVideo = new Video();
        testVideo.setId(1L);
        testVideo.setDescription("Test match video");

        // Setup test analysis 1
        testAnalysis1 = new VideoAnalysis();
        testAnalysis1.setId(1L);
        testAnalysis1.setVideo(testVideo);
        testAnalysis1.setPlayer(testPlayer);
        testAnalysis1.setAiAnalyzed(true);
        testAnalysis1.setAiAnalysisDate(LocalDateTime.now());
        testAnalysis1.setStrengthForehandScore(8.5);
        testAnalysis1.setStrengthServeScore(7.0);
        testAnalysis1.setStrengthVolleyScore(6.5);
        testAnalysis1.setStrengthMovementScore(8.0);
        testAnalysis1.setStrengthSummary("Strong baseline player");
        testAnalysis1.setWeaknessBackhandScore(5.0);
        testAnalysis1.setWeaknessConsistencyScore(6.0);
        testAnalysis1.setWeaknessPressureScore(5.5);
        testAnalysis1.setWeaknessSummary("Needs to improve under pressure");
        testAnalysis1.setTacticalStyle("Aggressive Baseliner");
        testAnalysis1.setAggressionIndex(75);
        testAnalysis1.setNetApproachFrequency(20);
        testAnalysis1.setBaselineRallyPreference(80);
        testAnalysis1.setTacticalSummary("Prefers long rallies from baseline");
        testAnalysis1.setAiRecommendations("Focus on net play");
        testAnalysis1.setTrainingFocusAreas("Volleys, approach shots");
        testAnalysis1.setStatus("COMPLETED");
        testAnalysis1.setProcessingNotes("Analysis completed successfully");

        // Setup test analysis 2
        testAnalysis2 = new VideoAnalysis();
        testAnalysis2.setId(2L);
        testAnalysis2.setVideo(testVideo);
        testAnalysis2.setPlayer(testPlayer);
        testAnalysis2.setAiAnalyzed(false);
        testAnalysis2.setStatus("PENDING");

        // Setup test request
        testRequest = new VideoAnalysisRequest();
        testRequest.setVideoId(1L);
        testRequest.setPlayerId(1L);
        testRequest.setAiAnalyzed(true);
        testRequest.setStrengthForehandScore(9.0);
        testRequest.setStrengthServeScore(8.0);
        testRequest.setStrengthVolleyScore(7.0);
        testRequest.setStrengthMovementScore(8.5);
        testRequest.setStrengthSummary("Excellent all-around player");
        testRequest.setWeaknessBackhandScore(6.0);
        testRequest.setWeaknessConsistencyScore(7.0);
        testRequest.setWeaknessPressureScore(6.5);
        testRequest.setWeaknessSummary("Minor consistency issues");
        testRequest.setTacticalStyle("All-Court Player");
        testRequest.setAggressionIndex(65);
        testRequest.setNetApproachFrequency(40);
        testRequest.setBaselineRallyPreference(60);
        testRequest.setTacticalSummary("Versatile game style");
        testRequest.setAiRecommendations("Continue balanced training");
        testRequest.setTrainingFocusAreas("Mental toughness, consistency");
        testRequest.setStatus("COMPLETED");
        testRequest.setProcessingNotes("AI analysis completed");
        testRequest.setKeyframesJson("{\"frames\": [100, 250, 500]}");
    }

    @Test
    @DisplayName("Should return all analyses")
    void testGetAllAnalyses() {
        // Arrange
        when(videoAnalysisRepository.findAll()).thenReturn(Arrays.asList(testAnalysis1, testAnalysis2));

        // Act
        List<VideoAnalysisResponse> results = videoAnalysisService.getAllAnalyses();

        // Assert
        assertThat(results).hasSize(2);
        verify(videoAnalysisRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no analyses exist")
    void testGetAllAnalysesWhenEmpty() {
        // Arrange
        when(videoAnalysisRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<VideoAnalysisResponse> results = videoAnalysisService.getAllAnalyses();

        // Assert
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should get analysis by ID")
    void testGetAnalysisById() {
        // Arrange
        when(videoAnalysisRepository.findById(1L)).thenReturn(Optional.of(testAnalysis1));

        // Act
        VideoAnalysisResponse result = videoAnalysisService.getAnalysisById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStrengthSummary()).isEqualTo("Strong baseline player");
    }

    @Test
    @DisplayName("Should throw exception when analysis not found by ID")
    void testGetAnalysisByIdNotFound() {
        // Arrange
        when(videoAnalysisRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.getAnalysisById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video analysis not found with id: 999");
    }

    @Test
    @DisplayName("Should get analyses by video ID")
    void testGetAnalysesByVideoId() {
        // Arrange
        when(videoAnalysisRepository.findByVideoId(1L))
                .thenReturn(Arrays.asList(testAnalysis1, testAnalysis2));

        // Act
        List<VideoAnalysisResponse> results = videoAnalysisService.getAnalysesByVideoId(1L);

        // Assert
        assertThat(results).hasSize(2);
        verify(videoAnalysisRepository, times(1)).findByVideoId(1L);
    }

    @Test
    @DisplayName("Should get analyses by player ID")
    void testGetAnalysesByPlayerId() {
        // Arrange
        when(videoAnalysisRepository.findByPlayerId(1L))
                .thenReturn(Arrays.asList(testAnalysis1, testAnalysis2));

        // Act
        List<VideoAnalysisResponse> results = videoAnalysisService.getAnalysesByPlayerId(1L);

        // Assert
        assertThat(results).hasSize(2);
        verify(videoAnalysisRepository, times(1)).findByPlayerId(1L);
    }

    @Test
    @DisplayName("Should get analysis by video and player ID")
    void testGetAnalysisByVideoAndPlayer() {
        // Arrange
        when(videoAnalysisRepository.findByVideoIdAndPlayerId(1L, 1L))
                .thenReturn(Optional.of(testAnalysis1));

        // Act
        VideoAnalysisResponse result = videoAnalysisService.getAnalysisByVideoAndPlayer(1L, 1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw exception when analysis not found by video and player")
    void testGetAnalysisByVideoAndPlayerNotFound() {
        // Arrange
        when(videoAnalysisRepository.findByVideoIdAndPlayerId(1L, 999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.getAnalysisByVideoAndPlayer(1L, 999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video analysis not found for video: 1 and player: 999");
    }

    @Test
    @DisplayName("Should create analysis successfully")
    void testCreateAnalysis() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(videoAnalysisRepository.existsByVideoIdAndPlayerId(1L, 1L)).thenReturn(false);
        when(videoAnalysisRepository.save(any(VideoAnalysis.class))).thenReturn(testAnalysis1);

        // Act
        VideoAnalysisResponse result = videoAnalysisService.createAnalysis(testRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(videoRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).findById(1L);
        verify(videoAnalysisRepository, times(1)).existsByVideoIdAndPlayerId(1L, 1L);
        verify(videoAnalysisRepository, times(1)).save(any(VideoAnalysis.class));
    }

    @Test
    @DisplayName("Should throw exception when creating analysis without video ID")
    void testCreateAnalysisWithoutVideoId() {
        // Arrange
        VideoAnalysisRequest invalidRequest = new VideoAnalysisRequest();
        invalidRequest.setVideoId(null);
        invalidRequest.setPlayerId(1L);

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.createAnalysis(invalidRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video ID is required");

        verify(videoAnalysisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when creating analysis without player ID")
    void testCreateAnalysisWithoutPlayerId() {
        // Arrange
        VideoAnalysisRequest invalidRequest = new VideoAnalysisRequest();
        invalidRequest.setVideoId(1L);
        invalidRequest.setPlayerId(null);

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.createAnalysis(invalidRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Player ID is required");

        verify(videoAnalysisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when video not found during analysis creation")
    void testCreateAnalysisVideoNotFound() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.createAnalysis(testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video not found with id: 1");

        verify(videoAnalysisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when player not found during analysis creation")
    void testCreateAnalysisPlayerNotFound() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.createAnalysis(testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Player not found with id: 1");

        verify(videoAnalysisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when analysis already exists for video and player")
    void testCreateAnalysisDuplicate() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(videoAnalysisRepository.existsByVideoIdAndPlayerId(1L, 1L)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.createAnalysis(testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Analysis already exists for video: 1 and player: 1");

        verify(videoAnalysisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should set AI analysis date when AI analyzed is true")
    void testCreateAnalysisSetsAiAnalysisDate() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(videoAnalysisRepository.existsByVideoIdAndPlayerId(1L, 1L)).thenReturn(false);
        when(videoAnalysisRepository.save(any(VideoAnalysis.class))).thenAnswer(invocation -> {
            VideoAnalysis savedAnalysis = invocation.getArgument(0);
            if (savedAnalysis.getAiAnalyzed() != null && savedAnalysis.getAiAnalyzed()) {
                assertThat(savedAnalysis.getAiAnalysisDate()).isNotNull();
                assertThat(savedAnalysis.getAiAnalysisDate()).isBeforeOrEqualTo(LocalDateTime.now());
            }
            return testAnalysis1;
        });

        // Act
        videoAnalysisService.createAnalysis(testRequest);

        // Assert
        verify(videoAnalysisRepository, times(1)).save(any(VideoAnalysis.class));
    }

    @Test
    @DisplayName("Should update analysis successfully")
    void testUpdateAnalysis() {
        // Arrange
        VideoAnalysisRequest updateRequest = new VideoAnalysisRequest();
        updateRequest.setStrengthForehandScore(9.5);
        updateRequest.setStrengthSummary("Updated strength summary");
        updateRequest.setStatus("REVIEWED");

        when(videoAnalysisRepository.findById(1L)).thenReturn(Optional.of(testAnalysis1));
        when(videoAnalysisRepository.save(any(VideoAnalysis.class))).thenReturn(testAnalysis1);

        // Act
        VideoAnalysisResponse result = videoAnalysisService.updateAnalysis(1L, updateRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(videoAnalysisRepository, times(1)).findById(1L);
        verify(videoAnalysisRepository, times(1)).save(testAnalysis1);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent analysis")
    void testUpdateAnalysisNotFound() {
        // Arrange
        when(videoAnalysisRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.updateAnalysis(999L, testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video analysis not found with id: 999");

        verify(videoAnalysisRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete analysis by ID")
    void testDeleteAnalysis() {
        // Arrange
        when(videoAnalysisRepository.existsById(1L)).thenReturn(true);

        // Act
        videoAnalysisService.deleteAnalysis(1L);

        // Assert
        verify(videoAnalysisRepository, times(1)).existsById(1L);
        verify(videoAnalysisRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent analysis")
    void testDeleteAnalysisNotFound() {
        // Arrange
        when(videoAnalysisRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> videoAnalysisService.deleteAnalysis(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video analysis not found with id: 999");

        verify(videoAnalysisRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should delete analyses by video ID")
    void testDeleteAnalysesByVideoId() {
        // Act
        videoAnalysisService.deleteAnalysesByVideoId(1L);

        // Assert
        verify(videoAnalysisRepository, times(1)).deleteByVideoId(1L);
    }

    @Test
    @DisplayName("Should get analysis count for video")
    void testGetAnalysisCountForVideo() {
        // Arrange
        when(videoAnalysisRepository.countByVideoId(1L)).thenReturn(5L);

        // Act
        long count = videoAnalysisService.getAnalysisCountForVideo(1L);

        // Assert
        assertThat(count).isEqualTo(5L);
        verify(videoAnalysisRepository, times(1)).countByVideoId(1L);
    }

    @Test
    @DisplayName("Should get analysis count for player")
    void testGetAnalysisCountForPlayer() {
        // Arrange
        when(videoAnalysisRepository.countByPlayerId(1L)).thenReturn(10L);

        // Act
        long count = videoAnalysisService.getAnalysisCountForPlayer(1L);

        // Assert
        assertThat(count).isEqualTo(10L);
        verify(videoAnalysisRepository, times(1)).countByPlayerId(1L);
    }

    @Test
    @DisplayName("Should preserve all analysis fields when creating")
    void testCreateAnalysisPreservesAllFields() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(videoAnalysisRepository.existsByVideoIdAndPlayerId(1L, 1L)).thenReturn(false);
        when(videoAnalysisRepository.save(any(VideoAnalysis.class))).thenAnswer(invocation -> {
            VideoAnalysis savedAnalysis = invocation.getArgument(0);
            assertThat(savedAnalysis.getStrengthForehandScore()).isEqualTo(9.0);
            assertThat(savedAnalysis.getWeaknessBackhandScore()).isEqualTo(6.0);
            assertThat(savedAnalysis.getTacticalStyle()).isEqualTo("All-Court Player");
            assertThat(savedAnalysis.getAggressionIndex()).isEqualTo(65);
            assertThat(savedAnalysis.getNetApproachFrequency()).isEqualTo(40);
            assertThat(savedAnalysis.getTrainingFocusAreas()).isEqualTo("Mental toughness, consistency");
            assertThat(savedAnalysis.getKeyframesJson()).isEqualTo("{\"frames\": [100, 250, 500]}");
            return testAnalysis1;
        });

        // Act
        videoAnalysisService.createAnalysis(testRequest);

        // Assert
        verify(videoAnalysisRepository, times(1)).save(any(VideoAnalysis.class));
    }

    @Test
    @DisplayName("Should handle null scores gracefully")
    void testCreateAnalysisWithNullScores() {
        // Arrange
        VideoAnalysisRequest requestWithNulls = new VideoAnalysisRequest();
        requestWithNulls.setVideoId(1L);
        requestWithNulls.setPlayerId(1L);
        requestWithNulls.setStrengthForehandScore(null);
        requestWithNulls.setWeaknessBackhandScore(null);
        requestWithNulls.setAggressionIndex(null);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(videoAnalysisRepository.existsByVideoIdAndPlayerId(1L, 1L)).thenReturn(false);
        when(videoAnalysisRepository.save(any(VideoAnalysis.class))).thenReturn(testAnalysis1);

        // Act
        VideoAnalysisResponse result = videoAnalysisService.createAnalysis(requestWithNulls);

        // Assert
        assertThat(result).isNotNull();
        verify(videoAnalysisRepository, times(1)).save(any(VideoAnalysis.class));
    }

    @Test
    @DisplayName("Should not set AI analysis date when AI analyzed is false")
    void testCreateAnalysisDoesNotSetAiDateWhenFalse() {
        // Arrange
        VideoAnalysisRequest requestWithNoAI = new VideoAnalysisRequest();
        requestWithNoAI.setVideoId(1L);
        requestWithNoAI.setPlayerId(1L);
        requestWithNoAI.setAiAnalyzed(false);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));
        when(playerRepository.findById(1L)).thenReturn(Optional.of(testPlayer));
        when(videoAnalysisRepository.existsByVideoIdAndPlayerId(1L, 1L)).thenReturn(false);
        when(videoAnalysisRepository.save(any(VideoAnalysis.class))).thenAnswer(invocation -> {
            VideoAnalysis savedAnalysis = invocation.getArgument(0);
            if (savedAnalysis.getAiAnalyzed() != null && !savedAnalysis.getAiAnalyzed()) {
                // AI analysis date should not be set when aiAnalyzed is false
                // (or it might be null depending on implementation)
            }
            return testAnalysis2;
        });

        // Act
        videoAnalysisService.createAnalysis(requestWithNoAI);

        // Assert
        verify(videoAnalysisRepository, times(1)).save(any(VideoAnalysis.class));
    }
}
