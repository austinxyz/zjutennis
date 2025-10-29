package com.zjutennis.repository;

import com.zjutennis.model.Match;
import com.zjutennis.model.Video;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("VideoRepository Integration Tests")
class VideoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MatchRepository matchRepository;

    private Match match1;
    private Match match2;
    private Video video1;
    private Video video2;

    @BeforeEach
    void setUp() {
        // Create and persist matches
        match1 = new Match();
        match1.setMatchType("doubles");
        match1.setMatchTime(LocalDateTime.now());
        match1.setTournamentName("Tournament 1");
        match1.setResult("complete");
        match1 = entityManager.persistAndFlush(match1);

        match2 = new Match();
        match2.setMatchType("singles");
        match2.setMatchTime(LocalDateTime.now().plusDays(1));
        match2.setTournamentName("Tournament 2");
        match2.setResult("complete");
        match2 = entityManager.persistAndFlush(match2);

        // Create and persist videos
        video1 = new Video();
        video1.setDescription("Video 1 description");
        video1.setVideoUrl("https://youtube.com/video1");
        video1.setThumbnailUrl("https://youtube.com/thumb1");
        video1.setDurationSeconds(3600);
        video1.setMatch(match1);
        video1.setTotalShots(150);
        video1.setErrors(20);
        video1.setWinners(30);
        video1 = entityManager.persistAndFlush(video1);

        video2 = new Video();
        video2.setDescription("Video 2 description");
        video2.setVideoUrl("https://youtube.com/video2");
        video2.setMatch(match2);
        video2.setTotalShots(200);
        video2 = entityManager.persistAndFlush(video2);

        entityManager.clear();
    }

    @Test
    @DisplayName("Should find all videos")
    void testFindAll() {
        var videos = videoRepository.findAll();

        assertThat(videos).hasSize(2);
        assertThat(videos).extracting(Video::getDescription)
                .containsExactlyInAnyOrder("Video 1 description", "Video 2 description");
    }

    @Test
    @DisplayName("Should find video by ID")
    void testFindById() {
        Optional<Video> found = videoRepository.findById(video1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(video1.getId());
        assertThat(found.get().getDescription()).isEqualTo("Video 1 description");
        assertThat(found.get().getVideoUrl()).isEqualTo("https://youtube.com/video1");
    }

    @Test
    @DisplayName("Should return empty when video ID not found")
    void testFindByIdNotFound() {
        Optional<Video> found = videoRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should find video by match ID")
    void testFindByMatchId() {
        Optional<Video> found = videoRepository.findByMatchId(match1.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getMatch().getId()).isEqualTo(match1.getId());
        assertThat(found.get().getDescription()).isEqualTo("Video 1 description");
    }

    @Test
    @DisplayName("Should return empty when no video for match ID")
    void testFindByMatchIdNotFound() {
        Match matchWithoutVideo = new Match();
        matchWithoutVideo.setMatchType("doubles");
        matchWithoutVideo.setMatchTime(LocalDateTime.now());
        matchWithoutVideo.setResult("complete");
        matchWithoutVideo = entityManager.persistAndFlush(matchWithoutVideo);

        Optional<Video> found = videoRepository.findByMatchId(matchWithoutVideo.getId());

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Should check if video exists by match ID")
    void testExistsByMatchId() {
        boolean exists = videoRepository.existsByMatchId(match1.getId());
        assertThat(exists).isTrue();

        boolean notExists = videoRepository.existsByMatchId(999L);
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should save new video")
    void testSaveVideo() {
        Match newMatch = new Match();
        newMatch.setMatchType("doubles");
        newMatch.setMatchTime(LocalDateTime.now());
        newMatch.setResult("complete");
        newMatch = entityManager.persistAndFlush(newMatch);

        Video newVideo = new Video();
        newVideo.setDescription("New video");
        newVideo.setVideoUrl("https://youtube.com/new");
        newVideo.setMatch(newMatch);
        newVideo.setTotalShots(100);
        newVideo.setWinners(15);

        Video saved = videoRepository.save(newVideo);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getDescription()).isEqualTo("New video");
        assertThat(saved.getTotalShots()).isEqualTo(100);
        assertThat(saved.getWinners()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should update existing video")
    void testUpdateVideo() {
        Video toUpdate = videoRepository.findById(video1.getId()).orElseThrow();

        toUpdate.setDescription("Updated description");
        toUpdate.setTotalShots(175);
        toUpdate.setWinners(35);

        Video updated = videoRepository.save(toUpdate);

        assertThat(updated.getId()).isEqualTo(video1.getId());
        assertThat(updated.getDescription()).isEqualTo("Updated description");
        assertThat(updated.getTotalShots()).isEqualTo(175);
        assertThat(updated.getWinners()).isEqualTo(35);
    }

    @Test
    @DisplayName("Should delete video by ID")
    void testDeleteById() {
        videoRepository.deleteById(video1.getId());
        entityManager.flush();

        Optional<Video> found = videoRepository.findById(video1.getId());
        assertThat(found).isEmpty();

        // Verify other video still exists
        assertThat(videoRepository.findById(video2.getId())).isPresent();
    }

    @Test
    @DisplayName("Should delete video by match ID")
    void testDeleteByMatchId() {
        videoRepository.deleteByMatchId(match1.getId());
        entityManager.flush();

        Optional<Video> found = videoRepository.findByMatchId(match1.getId());
        assertThat(found).isEmpty();

        // Verify other video still exists
        assertThat(videoRepository.findByMatchId(match2.getId())).isPresent();
    }

    @Test
    @DisplayName("Should persist video with all statistics fields")
    void testSaveWithAllStatistics() {
        Match newMatch = new Match();
        newMatch.setMatchType("doubles");
        newMatch.setMatchTime(LocalDateTime.now());
        newMatch.setResult("complete");
        newMatch = entityManager.persistAndFlush(newMatch);

        Video video = new Video();
        video.setDescription("Complete stats video");
        video.setMatch(newMatch);
        video.setTotalShots(250);
        video.setErrors(30);
        video.setWinners(40);
        video.setAces(8);
        video.setDoubleFaults(5);
        video.setRunningDistanceMeters(3500.75);

        Video saved = videoRepository.save(video);
        entityManager.flush();
        entityManager.clear();

        Video retrieved = videoRepository.findById(saved.getId()).orElseThrow();

        assertThat(retrieved.getTotalShots()).isEqualTo(250);
        assertThat(retrieved.getErrors()).isEqualTo(30);
        assertThat(retrieved.getWinners()).isEqualTo(40);
        assertThat(retrieved.getAces()).isEqualTo(8);
        assertThat(retrieved.getDoubleFaults()).isEqualTo(5);
        assertThat(retrieved.getRunningDistanceMeters()).isEqualTo(3500.75);
    }

    @Test
    @DisplayName("Should handle null optional fields")
    void testSaveWithNullOptionalFields() {
        Match newMatch = new Match();
        newMatch.setMatchType("doubles");
        newMatch.setMatchTime(LocalDateTime.now());
        newMatch.setResult("complete");
        newMatch = entityManager.persistAndFlush(newMatch);

        Video video = new Video();
        video.setMatch(newMatch);
        // Only required field is match, all others are optional

        Video saved = videoRepository.save(video);
        entityManager.flush();
        entityManager.clear();

        Video retrieved = videoRepository.findById(saved.getId()).orElseThrow();

        assertThat(retrieved.getDescription()).isNull();
        assertThat(retrieved.getVideoUrl()).isNull();
        assertThat(retrieved.getThumbnailUrl()).isNull();
        assertThat(retrieved.getDurationSeconds()).isNull();
        assertThat(retrieved.getTotalShots()).isNull();
        assertThat(retrieved.getErrors()).isNull();
    }

    @Test
    @DisplayName("Should maintain relationship with match")
    void testMatchRelationship() {
        Video video = videoRepository.findById(video1.getId()).orElseThrow();

        assertThat(video.getMatch()).isNotNull();
        assertThat(video.getMatch().getId()).isEqualTo(match1.getId());
        assertThat(video.getMatch().getTournamentName()).isEqualTo("Tournament 1");
    }

    @Test
    @DisplayName("Should count videos")
    void testCount() {
        long count = videoRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("Should check if video exists by ID")
    void testExistsById() {
        boolean exists = videoRepository.existsById(video1.getId());
        assertThat(exists).isTrue();

        boolean notExists = videoRepository.existsById(999L);
        assertThat(notExists).isFalse();
    }
}
