package com.zjutennis.service;

import com.zjutennis.dto.VideoRequest;
import com.zjutennis.dto.VideoResponse;
import com.zjutennis.model.Match;
import com.zjutennis.model.Video;
import com.zjutennis.repository.MatchRepository;
import com.zjutennis.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("VideoService Tests")
class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private MatchRepository matchRepository;

    @InjectMocks
    private VideoService videoService;

    private Video testVideo1;
    private Video testVideo2;
    private Match testMatch;
    private VideoRequest testRequest;

    @BeforeEach
    void setUp() {
        // Setup test match
        testMatch = new Match();
        testMatch.setId(1L);
        testMatch.setMatchDate(LocalDate.of(2024, 1, 15));
        testMatch.setLocation("Beijing Tennis Center");

        // Setup test video 1
        testVideo1 = new Video();
        testVideo1.setId(1L);
        testVideo1.setDescription("Singles match - Round 1");
        testVideo1.setVideoUrl("https://example.com/video1.mp4");
        testVideo1.setThumbnailUrl("https://example.com/thumb1.jpg");
        testVideo1.setDurationSeconds(3600);
        testVideo1.setMatch(testMatch);
        testVideo1.setTotalShots(150);
        testVideo1.setErrors(20);
        testVideo1.setWinners(35);
        testVideo1.setAces(8);
        testVideo1.setDoubleFaults(3);
        testVideo1.setRunningDistanceMeters(2500.0);

        // Setup test video 2
        testVideo2 = new Video();
        testVideo2.setId(2L);
        testVideo2.setDescription("Doubles match");
        testVideo2.setVideoUrl("https://example.com/video2.mp4");
        testVideo2.setThumbnailUrl("https://example.com/thumb2.jpg");
        testVideo2.setDurationSeconds(2400);

        // Setup test request
        testRequest = new VideoRequest();
        testRequest.setMatchId(1L);
        testRequest.setDescription("Test video");
        testRequest.setVideoUrl("https://example.com/new-video.mp4");
        testRequest.setThumbnailUrl("https://example.com/new-thumb.jpg");
        testRequest.setDurationSeconds(1800);
        testRequest.setTotalShots(100);
        testRequest.setErrors(15);
        testRequest.setWinners(25);
        testRequest.setAces(5);
        testRequest.setDoubleFaults(2);
        testRequest.setRunningDistanceMeters(2000.0);
    }

    @Test
    @DisplayName("Should return all videos")
    void testGetAllVideos() {
        // Arrange
        when(videoRepository.findAll()).thenReturn(Arrays.asList(testVideo1, testVideo2));

        // Act
        List<VideoResponse> results = videoService.getAllVideos();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getDescription()).isEqualTo("Singles match - Round 1");
        assertThat(results.get(1).getDescription()).isEqualTo("Doubles match");
        verify(videoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no videos exist")
    void testGetAllVideosWhenEmpty() {
        // Arrange
        when(videoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<VideoResponse> results = videoService.getAllVideos();

        // Assert
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("Should get video by ID")
    void testGetVideoById() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo1));

        // Act
        VideoResponse result = videoService.getVideoById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Singles match - Round 1");
        assertThat(result.getTotalShots()).isEqualTo(150);
    }

    @Test
    @DisplayName("Should throw exception when video not found by ID")
    void testGetVideoByIdNotFound() {
        // Arrange
        when(videoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoService.getVideoById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video not found with id: 999");
    }

    @Test
    @DisplayName("Should get video by match ID")
    void testGetVideoByMatchId() {
        // Arrange
        when(videoRepository.findByMatchId(1L)).thenReturn(Optional.of(testVideo1));

        // Act
        VideoResponse result = videoService.getVideoByMatchId(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Singles match - Round 1");
    }

    @Test
    @DisplayName("Should throw exception when video not found by match ID")
    void testGetVideoByMatchIdNotFound() {
        // Arrange
        when(videoRepository.findByMatchId(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoService.getVideoByMatchId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video not found for match: 999");
    }

    @Test
    @DisplayName("Should check if video exists for match")
    void testVideoExistsForMatch() {
        // Arrange
        when(videoRepository.existsByMatchId(1L)).thenReturn(true);
        when(videoRepository.existsByMatchId(2L)).thenReturn(false);

        // Act & Assert
        assertThat(videoService.videoExistsForMatch(1L)).isTrue();
        assertThat(videoService.videoExistsForMatch(2L)).isFalse();
    }

    @Test
    @DisplayName("Should create video successfully")
    void testCreateVideo() {
        // Arrange
        when(matchRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(videoRepository.existsByMatchId(1L)).thenReturn(false);
        when(videoRepository.save(any(Video.class))).thenReturn(testVideo1);

        // Act
        VideoResponse result = videoService.createVideo(testRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(matchRepository, times(1)).findById(1L);
        verify(videoRepository, times(1)).existsByMatchId(1L);
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    @DisplayName("Should throw exception when creating video without match ID")
    void testCreateVideoWithoutMatchId() {
        // Arrange
        VideoRequest invalidRequest = new VideoRequest();
        invalidRequest.setMatchId(null);

        // Act & Assert
        assertThatThrownBy(() -> videoService.createVideo(invalidRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Match ID is required");

        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when match not found during video creation")
    void testCreateVideoMatchNotFound() {
        // Arrange
        when(matchRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoService.createVideo(testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Match not found with id: 1");

        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when video already exists for match")
    void testCreateVideoDuplicate() {
        // Arrange
        when(matchRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(videoRepository.existsByMatchId(1L)).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> videoService.createVideo(testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video already exists for match: 1");

        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update video successfully")
    void testUpdateVideo() {
        // Arrange
        VideoRequest updateRequest = new VideoRequest();
        updateRequest.setDescription("Updated description");
        updateRequest.setVideoUrl("https://example.com/updated.mp4");
        updateRequest.setThumbnailUrl("https://example.com/updated-thumb.jpg");
        updateRequest.setDurationSeconds(2000);
        updateRequest.setTotalShots(200);
        updateRequest.setErrors(25);
        updateRequest.setWinners(40);
        updateRequest.setAces(10);
        updateRequest.setDoubleFaults(5);
        updateRequest.setRunningDistanceMeters(3000.0);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo1));
        when(videoRepository.save(any(Video.class))).thenReturn(testVideo1);

        // Act
        VideoResponse result = videoService.updateVideo(1L, updateRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(videoRepository, times(1)).findById(1L);
        verify(videoRepository, times(1)).save(testVideo1);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent video")
    void testUpdateVideoNotFound() {
        // Arrange
        when(videoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoService.updateVideo(999L, testRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video not found with id: 999");

        verify(videoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete video by ID")
    void testDeleteVideo() {
        // Arrange
        when(videoRepository.existsById(1L)).thenReturn(true);

        // Act
        videoService.deleteVideo(1L);

        // Assert
        verify(videoRepository, times(1)).existsById(1L);
        verify(videoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent video")
    void testDeleteVideoNotFound() {
        // Arrange
        when(videoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> videoService.deleteVideo(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video not found with id: 999");

        verify(videoRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should delete video by match ID")
    void testDeleteVideoByMatchId() {
        // Arrange
        when(videoRepository.existsByMatchId(1L)).thenReturn(true);

        // Act
        videoService.deleteVideoByMatchId(1L);

        // Assert
        verify(videoRepository, times(1)).existsByMatchId(1L);
        verify(videoRepository, times(1)).deleteByMatchId(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting video by non-existent match ID")
    void testDeleteVideoByMatchIdNotFound() {
        // Arrange
        when(videoRepository.existsByMatchId(999L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> videoService.deleteVideoByMatchId(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video not found for match: 999");

        verify(videoRepository, never()).deleteByMatchId(any());
    }

    @Test
    @DisplayName("Should get video entity")
    void testGetVideoEntity() {
        // Arrange
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo1));

        // Act
        Video result = videoService.getVideoEntity(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Singles match - Round 1");
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent video entity")
    void testGetVideoEntityNotFound() {
        // Arrange
        when(videoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> videoService.getVideoEntity(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Video not found with id: 999");
    }

    @Test
    @DisplayName("Should preserve all video statistics when creating")
    void testCreateVideoPreservesStatistics() {
        // Arrange
        when(matchRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(videoRepository.existsByMatchId(1L)).thenReturn(false);
        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> {
            Video savedVideo = invocation.getArgument(0);
            assertThat(savedVideo.getTotalShots()).isEqualTo(100);
            assertThat(savedVideo.getErrors()).isEqualTo(15);
            assertThat(savedVideo.getWinners()).isEqualTo(25);
            assertThat(savedVideo.getAces()).isEqualTo(5);
            assertThat(savedVideo.getDoubleFaults()).isEqualTo(2);
            assertThat(savedVideo.getRunningDistanceMeters()).isEqualTo(2000.0);
            return testVideo1;
        });

        // Act
        videoService.createVideo(testRequest);

        // Assert
        verify(videoRepository, times(1)).save(any(Video.class));
    }

    @Test
    @DisplayName("Should handle null statistics gracefully")
    void testCreateVideoWithNullStatistics() {
        // Arrange
        VideoRequest requestWithNulls = new VideoRequest();
        requestWithNulls.setMatchId(1L);
        requestWithNulls.setDescription("Video with null stats");
        requestWithNulls.setVideoUrl("https://example.com/video.mp4");
        requestWithNulls.setTotalShots(null);
        requestWithNulls.setErrors(null);
        requestWithNulls.setWinners(null);

        when(matchRepository.findById(1L)).thenReturn(Optional.of(testMatch));
        when(videoRepository.existsByMatchId(1L)).thenReturn(false);
        when(videoRepository.save(any(Video.class))).thenReturn(testVideo1);

        // Act
        VideoResponse result = videoService.createVideo(requestWithNulls);

        // Assert
        assertThat(result).isNotNull();
        verify(videoRepository, times(1)).save(any(Video.class));
    }
}
