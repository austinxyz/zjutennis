package com.zjutennis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("VideoAnalysis Model Tests")
class VideoAnalysisTest {

    private VideoAnalysis analysis;
    private Video video;
    private Player player;

    @BeforeEach
    void setUp() {
        // Setup Player
        player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setEmail("test@example.com");

        // Setup Video
        video = new Video();
        video.setId(1L);
        video.setDescription("Test Video");

        // Setup VideoAnalysis
        analysis = new VideoAnalysis();
        analysis.setId(1L);
        analysis.setVideo(video);
        analysis.setPlayer(player);
        analysis.setAiAnalyzed(false);
        analysis.setStatus("pending");
    }

    @Test
    @DisplayName("Should create video analysis with basic fields")
    void testVideoAnalysisCreation() {
        assertThat(analysis).isNotNull();
        assertThat(analysis.getId()).isEqualTo(1L);
        assertThat(analysis.getVideo()).isEqualTo(video);
        assertThat(analysis.getPlayer()).isEqualTo(player);
        assertThat(analysis.getAiAnalyzed()).isFalse();
        assertThat(analysis.getStatus()).isEqualTo("pending");
    }

    @Test
    @DisplayName("Should set and get strength scores")
    void testStrengthScores() {
        analysis.setStrengthForehandScore(4.5);
        analysis.setStrengthServeScore(4.0);
        analysis.setStrengthVolleyScore(3.5);
        analysis.setStrengthMovementScore(4.2);
        analysis.setStrengthSummary("Great forehand and movement");

        assertThat(analysis.getStrengthForehandScore()).isEqualTo(4.5);
        assertThat(analysis.getStrengthServeScore()).isEqualTo(4.0);
        assertThat(analysis.getStrengthVolleyScore()).isEqualTo(3.5);
        assertThat(analysis.getStrengthMovementScore()).isEqualTo(4.2);
        assertThat(analysis.getStrengthSummary()).isEqualTo("Great forehand and movement");
    }

    @Test
    @DisplayName("Should set and get weakness scores")
    void testWeaknessScores() {
        analysis.setWeaknessBackhandScore(2.5);
        analysis.setWeaknessConsistencyScore(3.0);
        analysis.setWeaknessPressureScore(2.8);
        analysis.setWeaknessSummary("Needs work on backhand");

        assertThat(analysis.getWeaknessBackhandScore()).isEqualTo(2.5);
        assertThat(analysis.getWeaknessConsistencyScore()).isEqualTo(3.0);
        assertThat(analysis.getWeaknessPressureScore()).isEqualTo(2.8);
        assertThat(analysis.getWeaknessSummary()).isEqualTo("Needs work on backhand");
    }

    @Test
    @DisplayName("Should set and get tactical analysis fields")
    void testTacticalAnalysis() {
        analysis.setTacticalStyle("baseline-player");
        analysis.setAggressionIndex(65.5);
        analysis.setNetApproachFrequency(25.0);
        analysis.setBaselineRallyPreference(85.0);
        analysis.setTacticalSummary("Prefers baseline rallies");

        assertThat(analysis.getTacticalStyle()).isEqualTo("baseline-player");
        assertThat(analysis.getAggressionIndex()).isEqualTo(65.5);
        assertThat(analysis.getNetApproachFrequency()).isEqualTo(25.0);
        assertThat(analysis.getBaselineRallyPreference()).isEqualTo(85.0);
        assertThat(analysis.getTacticalSummary()).isEqualTo("Prefers baseline rallies");
    }

    @Test
    @DisplayName("Should set and get AI recommendations")
    void testAIRecommendations() {
        analysis.setAiRecommendations("Focus on backhand consistency");
        analysis.setTrainingFocusAreas("Backhand drills, Footwork");
        analysis.setKeyframesJson("{\"frames\": [10, 25, 45]}");

        assertThat(analysis.getAiRecommendations()).isEqualTo("Focus on backhand consistency");
        assertThat(analysis.getTrainingFocusAreas()).isEqualTo("Backhand drills, Footwork");
        assertThat(analysis.getKeyframesJson()).isEqualTo("{\"frames\": [10, 25, 45]}");
    }

    @Test
    @DisplayName("Should update AI analysis status")
    void testAIAnalysisStatus() {
        LocalDateTime analysisDate = LocalDateTime.now();

        analysis.setAiAnalyzed(true);
        analysis.setAiAnalysisDate(analysisDate);
        analysis.setStatus("completed");

        assertThat(analysis.getAiAnalyzed()).isTrue();
        assertThat(analysis.getAiAnalysisDate()).isEqualTo(analysisDate);
        assertThat(analysis.getStatus()).isEqualTo("completed");
    }

    @Test
    @DisplayName("Should handle processing notes")
    void testProcessingNotes() {
        analysis.setProcessingNotes("Video processed successfully");
        assertThat(analysis.getProcessingNotes()).isEqualTo("Video processed successfully");
    }

    @Test
    @DisplayName("Should default aiAnalyzed to false")
    void testDefaultAiAnalyzed() {
        VideoAnalysis newAnalysis = new VideoAnalysis();
        assertThat(newAnalysis.getAiAnalyzed()).isFalse();
    }

    @Test
    @DisplayName("Should default status to pending")
    void testDefaultStatus() {
        VideoAnalysis newAnalysis = new VideoAnalysis();
        assertThat(newAnalysis.getStatus()).isEqualTo("pending");
    }

    @Test
    @DisplayName("Should allow null optional fields")
    void testNullOptionalFields() {
        VideoAnalysis newAnalysis = new VideoAnalysis();

        assertThat(newAnalysis.getStrengthForehandScore()).isNull();
        assertThat(newAnalysis.getStrengthServeScore()).isNull();
        assertThat(newAnalysis.getStrengthVolleyScore()).isNull();
        assertThat(newAnalysis.getStrengthMovementScore()).isNull();
        assertThat(newAnalysis.getStrengthSummary()).isNull();
        assertThat(newAnalysis.getWeaknessBackhandScore()).isNull();
        assertThat(newAnalysis.getWeaknessConsistencyScore()).isNull();
        assertThat(newAnalysis.getWeaknessPressureScore()).isNull();
        assertThat(newAnalysis.getWeaknessSummary()).isNull();
        assertThat(newAnalysis.getTacticalStyle()).isNull();
        assertThat(newAnalysis.getAggressionIndex()).isNull();
        assertThat(newAnalysis.getNetApproachFrequency()).isNull();
        assertThat(newAnalysis.getBaselineRallyPreference()).isNull();
        assertThat(newAnalysis.getTacticalSummary()).isNull();
        assertThat(newAnalysis.getAiRecommendations()).isNull();
        assertThat(newAnalysis.getTrainingFocusAreas()).isNull();
        assertThat(newAnalysis.getKeyframesJson()).isNull();
        assertThat(newAnalysis.getProcessingNotes()).isNull();
        assertThat(newAnalysis.getAiAnalysisDate()).isNull();
    }

    @Test
    @DisplayName("Should handle boundary values for scores")
    void testBoundaryScoreValues() {
        // Minimum values
        analysis.setStrengthForehandScore(0.0);
        analysis.setWeaknessBackhandScore(0.0);

        assertThat(analysis.getStrengthForehandScore()).isEqualTo(0.0);
        assertThat(analysis.getWeaknessBackhandScore()).isEqualTo(0.0);

        // Maximum values
        analysis.setStrengthForehandScore(5.0);
        analysis.setWeaknessBackhandScore(5.0);

        assertThat(analysis.getStrengthForehandScore()).isEqualTo(5.0);
        assertThat(analysis.getWeaknessBackhandScore()).isEqualTo(5.0);
    }

    @Test
    @DisplayName("Should handle boundary values for tactical metrics")
    void testBoundaryTacticalMetrics() {
        // Minimum values
        analysis.setAggressionIndex(0.0);
        analysis.setNetApproachFrequency(0.0);
        analysis.setBaselineRallyPreference(0.0);

        assertThat(analysis.getAggressionIndex()).isEqualTo(0.0);
        assertThat(analysis.getNetApproachFrequency()).isEqualTo(0.0);
        assertThat(analysis.getBaselineRallyPreference()).isEqualTo(0.0);

        // Maximum values
        analysis.setAggressionIndex(100.0);
        analysis.setNetApproachFrequency(100.0);
        analysis.setBaselineRallyPreference(100.0);

        assertThat(analysis.getAggressionIndex()).isEqualTo(100.0);
        assertThat(analysis.getNetApproachFrequency()).isEqualTo(100.0);
        assertThat(analysis.getBaselineRallyPreference()).isEqualTo(100.0);
    }

    @Test
    @DisplayName("Should update status through workflow")
    void testStatusWorkflow() {
        // Initial state
        assertThat(analysis.getStatus()).isEqualTo("pending");

        // Processing
        analysis.setStatus("processing");
        assertThat(analysis.getStatus()).isEqualTo("processing");

        // Completed
        analysis.setStatus("completed");
        analysis.setAiAnalyzed(true);
        analysis.setAiAnalysisDate(LocalDateTime.now());

        assertThat(analysis.getStatus()).isEqualTo("completed");
        assertThat(analysis.getAiAnalyzed()).isTrue();
        assertThat(analysis.getAiAnalysisDate()).isNotNull();
    }

    @Test
    @DisplayName("Should handle failed status")
    void testFailedStatus() {
        analysis.setStatus("failed");
        analysis.setProcessingNotes("Error: Video format not supported");

        assertThat(analysis.getStatus()).isEqualTo("failed");
        assertThat(analysis.getProcessingNotes()).isEqualTo("Error: Video format not supported");
        assertThat(analysis.getAiAnalyzed()).isFalse();
    }

    @Test
    @DisplayName("Should maintain relationship with Video")
    void testVideoRelationship() {
        assertThat(analysis.getVideo()).isNotNull();
        assertThat(analysis.getVideo()).isEqualTo(video);
        assertThat(analysis.getVideo().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should maintain relationship with Player")
    void testPlayerRelationship() {
        assertThat(analysis.getPlayer()).isNotNull();
        assertThat(analysis.getPlayer()).isEqualTo(player);
        assertThat(analysis.getPlayer().getId()).isEqualTo(1L);
        assertThat(analysis.getPlayer().getName()).isEqualTo("Test Player");
    }

    @Test
    @DisplayName("Should update all strength fields together")
    void testCompleteStrengthAnalysis() {
        analysis.setStrengthForehandScore(4.5);
        analysis.setStrengthServeScore(4.0);
        analysis.setStrengthVolleyScore(3.5);
        analysis.setStrengthMovementScore(4.2);
        analysis.setStrengthSummary("Player shows exceptional forehand power and movement. " +
                "Serve is consistent with good placement. Volleys need improvement.");

        assertThat(analysis.getStrengthForehandScore()).isEqualTo(4.5);
        assertThat(analysis.getStrengthServeScore()).isEqualTo(4.0);
        assertThat(analysis.getStrengthVolleyScore()).isEqualTo(3.5);
        assertThat(analysis.getStrengthMovementScore()).isEqualTo(4.2);
        assertThat(analysis.getStrengthSummary()).contains("exceptional forehand");
    }

    @Test
    @DisplayName("Should update all weakness fields together")
    void testCompleteWeaknessAnalysis() {
        analysis.setWeaknessBackhandScore(2.5);
        analysis.setWeaknessConsistencyScore(3.0);
        analysis.setWeaknessPressureScore(2.8);
        analysis.setWeaknessSummary("Backhand lacks consistency. Performance drops under pressure.");

        assertThat(analysis.getWeaknessBackhandScore()).isEqualTo(2.5);
        assertThat(analysis.getWeaknessConsistencyScore()).isEqualTo(3.0);
        assertThat(analysis.getWeaknessPressureScore()).isEqualTo(2.8);
        assertThat(analysis.getWeaknessSummary()).contains("Backhand lacks consistency");
    }
}
