package com.zjutennis.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Video Model Tests")
class VideoTest {

    private Video video;
    private Match match;
    private VideoAnalysis analysis1;
    private VideoAnalysis analysis2;

    @BeforeEach
    void setUp() {
        // Setup Match
        match = new Match();
        match.setId(1L);
        match.setMatchType("doubles");
        match.setMatchTime(LocalDateTime.now());
        match.setTournamentName("Test Tournament");

        // Setup Video
        video = new Video();
        video.setId(1L);
        video.setDescription("Test video description");
        video.setVideoUrl("https://youtube.com/test");
        video.setThumbnailUrl("https://youtube.com/thumbnail");
        video.setDurationSeconds(3600);
        video.setMatch(match);
        video.setTotalShots(150);
        video.setErrors(20);
        video.setWinners(30);
        video.setAces(5);
        video.setDoubleFaults(3);
        video.setRunningDistanceMeters(2500.5);

        // Setup VideoAnalysis
        analysis1 = new VideoAnalysis();
        analysis1.setId(1L);
        analysis1.setVideo(video);

        analysis2 = new VideoAnalysis();
        analysis2.setId(2L);
        analysis2.setVideo(video);
    }

    @Test
    @DisplayName("Should create video with all fields")
    void testVideoCreation() {
        assertThat(video).isNotNull();
        assertThat(video.getId()).isEqualTo(1L);
        assertThat(video.getDescription()).isEqualTo("Test video description");
        assertThat(video.getVideoUrl()).isEqualTo("https://youtube.com/test");
        assertThat(video.getThumbnailUrl()).isEqualTo("https://youtube.com/thumbnail");
        assertThat(video.getDurationSeconds()).isEqualTo(3600);
        assertThat(video.getMatch()).isEqualTo(match);
    }

    @Test
    @DisplayName("Should have correct statistics fields")
    void testVideoStatistics() {
        assertThat(video.getTotalShots()).isEqualTo(150);
        assertThat(video.getErrors()).isEqualTo(20);
        assertThat(video.getWinners()).isEqualTo(30);
        assertThat(video.getAces()).isEqualTo(5);
        assertThat(video.getDoubleFaults()).isEqualTo(3);
        assertThat(video.getRunningDistanceMeters()).isEqualTo(2500.5);
    }

    @Test
    @DisplayName("Should maintain relationship with Match")
    void testMatchRelationship() {
        assertThat(video.getMatch()).isNotNull();
        assertThat(video.getMatch().getId()).isEqualTo(1L);
        assertThat(video.getMatch().getTournamentName()).isEqualTo("Test Tournament");
    }

    @Test
    @DisplayName("Should add analysis to video")
    void testAddAnalysis() {
        video.addAnalysis(analysis1);

        assertThat(video.getAnalyses()).hasSize(1);
        assertThat(video.getAnalyses()).contains(analysis1);
        assertThat(analysis1.getVideo()).isEqualTo(video);
    }

    @Test
    @DisplayName("Should add multiple analyses to video")
    void testAddMultipleAnalyses() {
        video.addAnalysis(analysis1);
        video.addAnalysis(analysis2);

        assertThat(video.getAnalyses()).hasSize(2);
        assertThat(video.getAnalyses()).containsExactly(analysis1, analysis2);
    }

    @Test
    @DisplayName("Should remove analysis from video")
    void testRemoveAnalysis() {
        video.addAnalysis(analysis1);
        video.addAnalysis(analysis2);

        video.removeAnalysis(analysis1);

        assertThat(video.getAnalyses()).hasSize(1);
        assertThat(video.getAnalyses()).contains(analysis2);
        assertThat(video.getAnalyses()).doesNotContain(analysis1);
        assertThat(analysis1.getVideo()).isNull();
    }

    @Test
    @DisplayName("Should handle removing non-existent analysis gracefully")
    void testRemoveNonExistentAnalysis() {
        video.addAnalysis(analysis1);

        VideoAnalysis nonExistent = new VideoAnalysis();
        nonExistent.setId(999L);

        video.removeAnalysis(nonExistent);

        assertThat(video.getAnalyses()).hasSize(1);
        assertThat(video.getAnalyses()).contains(analysis1);
    }

    @Test
    @DisplayName("Should initialize with empty analyses list")
    void testEmptyAnalysesList() {
        Video newVideo = new Video();
        assertThat(newVideo.getAnalyses()).isNotNull();
        assertThat(newVideo.getAnalyses()).isEmpty();
    }

    @Test
    @DisplayName("Should allow null optional fields")
    void testNullOptionalFields() {
        Video newVideo = new Video();

        assertThat(newVideo.getDescription()).isNull();
        assertThat(newVideo.getVideoUrl()).isNull();
        assertThat(newVideo.getThumbnailUrl()).isNull();
        assertThat(newVideo.getDurationSeconds()).isNull();
        assertThat(newVideo.getTotalShots()).isNull();
        assertThat(newVideo.getErrors()).isNull();
        assertThat(newVideo.getWinners()).isNull();
        assertThat(newVideo.getAces()).isNull();
        assertThat(newVideo.getDoubleFaults()).isNull();
        assertThat(newVideo.getRunningDistanceMeters()).isNull();
    }

    @Test
    @DisplayName("Should update video fields")
    void testUpdateVideoFields() {
        video.setDescription("Updated description");
        video.setVideoUrl("https://youtube.com/updated");
        video.setDurationSeconds(7200);
        video.setTotalShots(200);

        assertThat(video.getDescription()).isEqualTo("Updated description");
        assertThat(video.getVideoUrl()).isEqualTo("https://youtube.com/updated");
        assertThat(video.getDurationSeconds()).isEqualTo(7200);
        assertThat(video.getTotalShots()).isEqualTo(200);
    }

    @Test
    @DisplayName("Should maintain bidirectional relationship when adding analysis")
    void testBidirectionalRelationshipOnAdd() {
        video.addAnalysis(analysis1);

        assertThat(analysis1.getVideo()).isEqualTo(video);
        assertThat(video.getAnalyses()).contains(analysis1);
    }

    @Test
    @DisplayName("Should clear bidirectional relationship when removing analysis")
    void testBidirectionalRelationshipOnRemove() {
        video.addAnalysis(analysis1);
        video.removeAnalysis(analysis1);

        assertThat(analysis1.getVideo()).isNull();
        assertThat(video.getAnalyses()).doesNotContain(analysis1);
    }

    @Test
    @DisplayName("Should handle statistics with zero values")
    void testZeroStatistics() {
        video.setTotalShots(0);
        video.setErrors(0);
        video.setWinners(0);
        video.setAces(0);
        video.setDoubleFaults(0);
        video.setRunningDistanceMeters(0.0);

        assertThat(video.getTotalShots()).isEqualTo(0);
        assertThat(video.getErrors()).isEqualTo(0);
        assertThat(video.getWinners()).isEqualTo(0);
        assertThat(video.getAces()).isEqualTo(0);
        assertThat(video.getDoubleFaults()).isEqualTo(0);
        assertThat(video.getRunningDistanceMeters()).isEqualTo(0.0);
    }
}
