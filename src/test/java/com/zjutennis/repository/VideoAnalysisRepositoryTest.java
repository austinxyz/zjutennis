package com.zjutennis.repository;

import com.zjutennis.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("VideoAnalysisRepository Integration Tests")
class VideoAnalysisRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VideoAnalysisRepository videoAnalysisRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchRepository matchRepository;

    private Video video1;
    private Video video2;
    private Player player1;
    private Player player2;
    private VideoAnalysis analysis1;
    private VideoAnalysis analysis2;
    private VideoAnalysis analysis3;

    @BeforeEach
    void setUp() {
        // Create and persist matches
        Match match1 = new Match();
        match1.setMatchType("doubles");
        match1.setMatchTime(LocalDateTime.now());
        match1.setResult("complete");
        match1 = entityManager.persistAndFlush(match1);

        Match match2 = new Match();
        match2.setMatchType("singles");
        match2.setMatchTime(LocalDateTime.now().plusDays(1));
        match2.setResult("complete");
        match2 = entityManager.persistAndFlush(match2);

        // Create and persist videos
        video1 = new Video();
        video1.setDescription("Video 1");
        video1.setMatch(match1);
        video1 = entityManager.persistAndFlush(video1);

        video2 = new Video();
        video2.setDescription("Video 2");
        video2.setMatch(match2);
        video2 = entityManager.persistAndFlush(video2);

        // Create and persist players
        player1 = new Player();
        player1.setName("Player One");
        player1.setEmail("player1@test.com");
        player1.setGender("male");
        player1 = entityManager.persistAndFlush(player1);

        player2 = new Player();
        player2.setName("Player Two");
        player2.setEmail("player2@test.com");
        player2.setGender("female");
        player2 = entityManager.persistAndFlush(player2);

        // Create and persist video analyses
        analysis1 = new VideoAnalysis();
        analysis1.setVideo(video1);
        analysis1.setPlayer(player1);
        analysis1.setAiAnalyzed(false);
        analysis1.setStatus("pending");
        analysis1.setStrengthForehandScore(4.5);
        analysis1 = entityManager.persistAndFlush(analysis1);

        analysis2 = new VideoAnalysis();
        analysis2.setVideo(video1);
        analysis2.setPlayer(player2);
        analysis2.setAiAnalyzed(true);
        analysis2.setAiAnalysisDate(LocalDateTime.now());
        analysis2.setStatus("completed");
        analysis2.setStrengthServeScore(4.0);
        analysis2 = entityManager.persistAndFlush(analysis2);

        analysis3 = new VideoAnalysis();
        analysis3.setVideo(video2);
        analysis3.setPlayer(player1);
        analysis3.setAiAnalyzed(false);
        analysis3.setStatus("pending");
        analysis3 = entityManager.persistAndFlush(analysis3);

        entityManager.clear();
    }

    @Test
    @DisplayName("Should find all analyses")
    void testFindAll() {
        List<VideoAnalysis> analyses = videoAnalysisRepository.findAll();

        assertThat(analyses).hasSize(3);
    }

    @Test
    @DisplayName("Should find analysis by ID")
    void testFindById() {
        Optional<VideoAnalysis> found = videoAnalysisRepository.findById(analysis1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(analysis1.getId());
        assertThat(found.get().getStrengthForehandScore()).isEqualTo(4.5);
    }

    @Test
    @DisplayName("Should find analyses by video ID")
    void testFindByVideoId() {
        List<VideoAnalysis> analyses = videoAnalysisRepository.findByVideoId(video1.getId());

        assertThat(analyses).hasSize(2);
        assertThat(analyses).extracting(VideoAnalysis::getVideo)
                .allMatch(v -> v.getId().equals(video1.getId()));
    }

    @Test
    @DisplayName("Should find analyses by player ID")
    void testFindByPlayerId() {
        List<VideoAnalysis> analyses = videoAnalysisRepository.findByPlayerId(player1.getId());

        assertThat(analyses).hasSize(2);
        assertThat(analyses).extracting(VideoAnalysis::getPlayer)
                .allMatch(p -> p.getId().equals(player1.getId()));
    }

    @Test
    @DisplayName("Should find analysis by video and player")
    void testFindByVideoIdAndPlayerId() {
        Optional<VideoAnalysis> found = videoAnalysisRepository
                .findByVideoIdAndPlayerId(video1.getId(), player1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(analysis1.getId());
        assertThat(found.get().getVideo().getId()).isEqualTo(video1.getId());
        assertThat(found.get().getPlayer().getId()).isEqualTo(player1.getId());
    }

    @Test
    @DisplayName("Should check if analysis exists by video and player")
    void testExistsByVideoIdAndPlayerId() {
        boolean exists = videoAnalysisRepository
                .existsByVideoIdAndPlayerId(video1.getId(), player1.getId());
        assertThat(exists).isTrue();

        boolean notExists = videoAnalysisRepository
                .existsByVideoIdAndPlayerId(video1.getId(), 999L);
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should delete analyses by video ID")
    void testDeleteByVideoId() {
        videoAnalysisRepository.deleteByVideoId(video1.getId());
        entityManager.flush();

        List<VideoAnalysis> remaining = videoAnalysisRepository.findByVideoId(video1.getId());
        assertThat(remaining).isEmpty();

        // Verify other video's analyses still exist
        List<VideoAnalysis> video2Analyses = videoAnalysisRepository.findByVideoId(video2.getId());
        assertThat(video2Analyses).hasSize(1);
    }

    @Test
    @DisplayName("Should delete analyses by player ID")
    void testDeleteByPlayerId() {
        videoAnalysisRepository.deleteByPlayerId(player1.getId());
        entityManager.flush();

        List<VideoAnalysis> remaining = videoAnalysisRepository.findByPlayerId(player1.getId());
        assertThat(remaining).isEmpty();

        // Verify other player's analyses still exist
        List<VideoAnalysis> player2Analyses = videoAnalysisRepository.findByPlayerId(player2.getId());
        assertThat(player2Analyses).hasSize(1);
    }

    @Test
    @DisplayName("Should count analyses by video ID")
    void testCountByVideoId() {
        long count = videoAnalysisRepository.countByVideoId(video1.getId());
        assertThat(count).isEqualTo(2);

        long count2 = videoAnalysisRepository.countByVideoId(video2.getId());
        assertThat(count2).isEqualTo(1);
    }

    @Test
    @DisplayName("Should count analyses by player ID")
    void testCountByPlayerId() {
        long count = videoAnalysisRepository.countByPlayerId(player1.getId());
        assertThat(count).isEqualTo(2);

        long count2 = videoAnalysisRepository.countByPlayerId(player2.getId());
        assertThat(count2).isEqualTo(1);
    }

    @Test
    @DisplayName("Should find analyses by player and AI analyzed status")
    void testFindByPlayerIdAndAiAnalyzed() {
        List<VideoAnalysis> aiAnalyzed = videoAnalysisRepository
                .findByPlayerIdAndAiAnalyzed(player1.getId(), true);
        assertThat(aiAnalyzed).isEmpty();

        List<VideoAnalysis> notAiAnalyzed = videoAnalysisRepository
                .findByPlayerIdAndAiAnalyzed(player1.getId(), false);
        assertThat(notAiAnalyzed).hasSize(2);

        List<VideoAnalysis> player2AiAnalyzed = videoAnalysisRepository
                .findByPlayerIdAndAiAnalyzed(player2.getId(), true);
        assertThat(player2AiAnalyzed).hasSize(1);
    }

    @Test
    @DisplayName("Should save new analysis")
    void testSaveAnalysis() {
        VideoAnalysis newAnalysis = new VideoAnalysis();
        newAnalysis.setVideo(video2);
        newAnalysis.setPlayer(player2);
        newAnalysis.setAiAnalyzed(false);
        newAnalysis.setStatus("pending");
        newAnalysis.setStrengthForehandScore(3.5);
        newAnalysis.setWeaknessBackhandScore(2.5);

        VideoAnalysis saved = videoAnalysisRepository.save(newAnalysis);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStrengthForehandScore()).isEqualTo(3.5);
        assertThat(saved.getWeaknessBackhandScore()).isEqualTo(2.5);
    }

    @Test
    @DisplayName("Should update existing analysis")
    void testUpdateAnalysis() {
        VideoAnalysis toUpdate = videoAnalysisRepository.findById(analysis1.getId()).orElseThrow();

        toUpdate.setAiAnalyzed(true);
        toUpdate.setAiAnalysisDate(LocalDateTime.now());
        toUpdate.setStatus("completed");
        toUpdate.setStrengthServeScore(4.2);
        toUpdate.setAiRecommendations("Focus on backhand");

        VideoAnalysis updated = videoAnalysisRepository.save(toUpdate);

        assertThat(updated.getId()).isEqualTo(analysis1.getId());
        assertThat(updated.getAiAnalyzed()).isTrue();
        assertThat(updated.getStatus()).isEqualTo("completed");
        assertThat(updated.getStrengthServeScore()).isEqualTo(4.2);
        assertThat(updated.getAiRecommendations()).isEqualTo("Focus on backhand");
    }

    @Test
    @DisplayName("Should delete analysis by ID")
    void testDeleteById() {
        videoAnalysisRepository.deleteById(analysis1.getId());
        entityManager.flush();

        Optional<VideoAnalysis> found = videoAnalysisRepository.findById(analysis1.getId());
        assertThat(found).isEmpty();

        // Verify other analyses still exist
        assertThat(videoAnalysisRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("Should persist analysis with all strength fields")
    void testSaveWithAllStrengthFields() {
        VideoAnalysis analysis = new VideoAnalysis();
        analysis.setVideo(video2);
        analysis.setPlayer(player2);
        analysis.setStrengthForehandScore(4.5);
        analysis.setStrengthServeScore(4.0);
        analysis.setStrengthVolleyScore(3.5);
        analysis.setStrengthMovementScore(4.2);
        analysis.setStrengthSummary("Excellent all-around player");

        VideoAnalysis saved = videoAnalysisRepository.save(analysis);
        entityManager.flush();
        entityManager.clear();

        VideoAnalysis retrieved = videoAnalysisRepository.findById(saved.getId()).orElseThrow();

        assertThat(retrieved.getStrengthForehandScore()).isEqualTo(4.5);
        assertThat(retrieved.getStrengthServeScore()).isEqualTo(4.0);
        assertThat(retrieved.getStrengthVolleyScore()).isEqualTo(3.5);
        assertThat(retrieved.getStrengthMovementScore()).isEqualTo(4.2);
        assertThat(retrieved.getStrengthSummary()).isEqualTo("Excellent all-around player");
    }

    @Test
    @DisplayName("Should persist analysis with all weakness fields")
    void testSaveWithAllWeaknessFields() {
        VideoAnalysis analysis = new VideoAnalysis();
        analysis.setVideo(video2);
        analysis.setPlayer(player2);
        analysis.setWeaknessBackhandScore(2.5);
        analysis.setWeaknessConsistencyScore(3.0);
        analysis.setWeaknessPressureScore(2.8);
        analysis.setWeaknessSummary("Needs improvement under pressure");

        VideoAnalysis saved = videoAnalysisRepository.save(analysis);
        entityManager.flush();
        entityManager.clear();

        VideoAnalysis retrieved = videoAnalysisRepository.findById(saved.getId()).orElseThrow();

        assertThat(retrieved.getWeaknessBackhandScore()).isEqualTo(2.5);
        assertThat(retrieved.getWeaknessConsistencyScore()).isEqualTo(3.0);
        assertThat(retrieved.getWeaknessPressureScore()).isEqualTo(2.8);
        assertThat(retrieved.getWeaknessSummary()).isEqualTo("Needs improvement under pressure");
    }

    @Test
    @DisplayName("Should persist analysis with all tactical fields")
    void testSaveWithAllTacticalFields() {
        VideoAnalysis analysis = new VideoAnalysis();
        analysis.setVideo(video2);
        analysis.setPlayer(player2);
        analysis.setTacticalStyle("baseline-player");
        analysis.setAggressionIndex(65.5);
        analysis.setNetApproachFrequency(25.0);
        analysis.setBaselineRallyPreference(85.0);
        analysis.setTacticalSummary("Prefers baseline rallies");

        VideoAnalysis saved = videoAnalysisRepository.save(analysis);
        entityManager.flush();
        entityManager.clear();

        VideoAnalysis retrieved = videoAnalysisRepository.findById(saved.getId()).orElseThrow();

        assertThat(retrieved.getTacticalStyle()).isEqualTo("baseline-player");
        assertThat(retrieved.getAggressionIndex()).isEqualTo(65.5);
        assertThat(retrieved.getNetApproachFrequency()).isEqualTo(25.0);
        assertThat(retrieved.getBaselineRallyPreference()).isEqualTo(85.0);
        assertThat(retrieved.getTacticalSummary()).isEqualTo("Prefers baseline rallies");
    }

    @Test
    @DisplayName("Should maintain relationships with video and player")
    void testRelationships() {
        VideoAnalysis analysis = videoAnalysisRepository.findById(analysis1.getId()).orElseThrow();

        assertThat(analysis.getVideo()).isNotNull();
        assertThat(analysis.getVideo().getId()).isEqualTo(video1.getId());

        assertThat(analysis.getPlayer()).isNotNull();
        assertThat(analysis.getPlayer().getId()).isEqualTo(player1.getId());
        assertThat(analysis.getPlayer().getName()).isEqualTo("Player One");
    }

    @Test
    @DisplayName("Should return empty list when no analyses for video")
    void testFindByVideoIdEmpty() {
        Match newMatch = new Match();
        newMatch.setMatchType("doubles");
        newMatch.setMatchTime(LocalDateTime.now());
        newMatch.setResult("complete");
        newMatch = entityManager.persistAndFlush(newMatch);

        Video videoWithoutAnalyses = new Video();
        videoWithoutAnalyses.setDescription("No analyses");
        videoWithoutAnalyses.setMatch(newMatch);
        videoWithoutAnalyses = entityManager.persistAndFlush(videoWithoutAnalyses);

        List<VideoAnalysis> analyses = videoAnalysisRepository.findByVideoId(videoWithoutAnalyses.getId());

        assertThat(analyses).isEmpty();
    }

    @Test
    @DisplayName("Should count zero when no analyses")
    void testCountZero() {
        long count = videoAnalysisRepository.countByVideoId(999L);
        assertThat(count).isEqualTo(0);
    }
}
