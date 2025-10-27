package com.zjutennis.service;

import com.zjutennis.model.VideoAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * Service for AI-powered tennis match analysis
 * Analyzes match videos and generates insights about player performance
 */
@Service
@Slf4j
public class AIAnalysisService {

    @Autowired
    private VideoAnalysisService videoAnalysisService;

    private final Random random = new Random();

    /**
     * Analyze a video and generate AI insights
     * This is a simulated analysis - in production, this would call an actual AI service
     *
     * @param videoId ID of the video to analyze
     * @return Updated VideoAnalysis with AI results
     */
    public VideoAnalysis analyzeVideo(Long videoId) {
        log.info("Starting AI analysis for video ID: {}", videoId);

        VideoAnalysis video = videoAnalysisService.getVideoById(videoId)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + videoId));

        // Update status to processing
        video.setStatus("processing");
        videoAnalysisService.updateVideo(videoId, video);

        try {
            // Simulate AI analysis based on video title and description
            VideoAnalysis aiResults = generateSimulatedAnalysis(video);

            // Update the video with AI analysis results
            VideoAnalysis updated = videoAnalysisService.updateAIAnalysis(videoId, aiResults);

            log.info("AI analysis completed successfully for video ID: {}", videoId);
            return updated;

        } catch (Exception e) {
            log.error("Error during AI analysis for video {}: {}", videoId, e.getMessage());
            video.setStatus("failed");
            videoAnalysisService.updateVideo(videoId, video);
            throw new RuntimeException("AI analysis failed: " + e.getMessage());
        }
    }

    /**
     * Generate simulated AI analysis results
     * In production, this would call Claude API or other AI service
     */
    private VideoAnalysis generateSimulatedAnalysis(VideoAnalysis video) {
        VideoAnalysis results = new VideoAnalysis();

        // Generate strength scores (0-10 scale)
        results.setStrengthForehandScore(generateScore(6.0, 9.5));
        results.setStrengthServeScore(generateScore(5.5, 9.0));
        results.setStrengthVolleyScore(generateScore(5.0, 8.5));
        results.setStrengthMovementScore(generateScore(6.0, 9.0));

        // Generate strength summary
        results.setStrengthSummary(generateStrengthSummary(
            results.getStrengthForehandScore(),
            results.getStrengthServeScore(),
            results.getStrengthVolleyScore(),
            results.getStrengthMovementScore()
        ));

        // Generate weakness scores (0-10 scale, higher = more problematic)
        results.setWeaknessBackhandScore(generateScore(3.0, 7.5));
        results.setWeaknessConsistencyScore(generateScore(3.0, 7.0));
        results.setWeaknessPressureScore(generateScore(2.5, 6.5));

        // Generate weakness summary
        results.setWeaknessSummary(generateWeaknessSummary(
            results.getWeaknessBackhandScore(),
            results.getWeaknessConsistencyScore(),
            results.getWeaknessPressureScore()
        ));

        // Generate tactical analysis
        String[] styles = {"aggressive baseline", "defensive baseline", "serve-and-volley", "all-court", "counter-puncher"};
        results.setTacticalStyle(styles[random.nextInt(styles.length)]);
        results.setAggressionIndex(generateScore(40.0, 85.0));
        results.setNetApproachFrequency(generateScore(15.0, 45.0));
        results.setBaselineRallyPreference(100.0 - results.getNetApproachFrequency());

        results.setTacticalSummary(generateTacticalSummary(
            results.getTacticalStyle(),
            results.getAggressionIndex(),
            results.getNetApproachFrequency()
        ));

        // Generate recommendations
        results.setAiRecommendations(generateRecommendations(results));
        results.setTrainingFocusAreas(generateTrainingFocus(results));

        return results;
    }

    private Double generateScore(double min, double max) {
        return Math.round((min + (max - min) * random.nextDouble()) * 10.0) / 10.0;
    }

    private String generateStrengthSummary(Double forehand, Double serve, Double volley, Double movement) {
        StringBuilder summary = new StringBuilder();

        if (forehand >= 8.0) {
            summary.append("Exceptional forehand consistency and power. ");
        } else if (forehand >= 7.0) {
            summary.append("Strong forehand with good placement. ");
        }

        if (serve >= 8.0) {
            summary.append("Dominant serve with excellent placement and pace. ");
        } else if (serve >= 7.0) {
            summary.append("Reliable serve with good variety. ");
        }

        if (volley >= 7.5) {
            summary.append("Confident net play with quick reflexes. ");
        }

        if (movement >= 8.0) {
            summary.append("Outstanding court coverage and footwork.");
        } else if (movement >= 7.0) {
            summary.append("Good anticipation and court positioning.");
        }

        return summary.toString().trim();
    }

    private String generateWeaknessSummary(Double backhand, Double consistency, Double pressure) {
        StringBuilder summary = new StringBuilder();

        if (backhand >= 6.0) {
            summary.append("Backhand errors increase under pressure, particularly on slice returns. ");
        } else if (backhand >= 4.0) {
            summary.append("Backhand shows occasional inconsistency on deep balls. ");
        }

        if (consistency >= 6.0) {
            summary.append("Some unforced errors in extended rallies. ");
        } else if (consistency >= 4.5) {
            summary.append("Minor lapses in concentration during long points. ");
        }

        if (pressure >= 5.5) {
            summary.append("Performance dips noticeably in crucial game points.");
        } else if (pressure >= 4.0) {
            summary.append("Slight increase in errors during tiebreaks.");
        }

        return summary.toString().trim();
    }

    private String generateTacticalSummary(String style, Double aggression, Double netApproach) {
        StringBuilder summary = new StringBuilder();

        summary.append("Player demonstrates a ").append(style).append(" style. ");

        if (aggression >= 70.0) {
            summary.append("Highly aggressive approach, frequently taking balls early and dictating points. ");
        } else if (aggression >= 55.0) {
            summary.append("Balanced aggression, mixing offensive shots with consistency. ");
        } else {
            summary.append("Patient baseline game, building points systematically. ");
        }

        if (netApproach >= 35.0) {
            summary.append("Regular net approaches, especially on short balls.");
        } else if (netApproach >= 20.0) {
            summary.append("Selective net play, primarily coming forward on serve.");
        } else {
            summary.append("Predominantly baseline-oriented with rare net approaches.");
        }

        return summary.toString().trim();
    }

    private String generateRecommendations(VideoAnalysis results) {
        StringBuilder recommendations = new StringBuilder();

        recommendations.append("Based on the match analysis:\n\n");

        // Recommendations based on weaknesses
        if (results.getWeaknessBackhandScore() >= 5.0) {
            recommendations.append("1. Focus on backhand consistency drills, especially slice returns\n");
        }

        if (results.getWeaknessConsistencyScore() >= 5.0) {
            recommendations.append("2. Practice extended rally patterns to improve consistency\n");
        }

        if (results.getWeaknessPressureScore() >= 5.0) {
            recommendations.append("3. Incorporate pressure point scenarios in practice matches\n");
        }

        // Recommendations based on tactical style
        if (results.getNetApproachFrequency() < 25.0) {
            recommendations.append("4. Work on approach shots and net finishing to add variety\n");
        }

        recommendations.append("5. Continue leveraging your strength in ")
            .append(findTopStrength(results))
            .append(" to control points");

        return recommendations.toString();
    }

    private String generateTrainingFocus(VideoAnalysis results) {
        StringBuilder focus = new StringBuilder();

        // Identify primary training focus based on weakest area
        if (results.getWeaknessBackhandScore() >= results.getWeaknessConsistencyScore()
            && results.getWeaknessBackhandScore() >= results.getWeaknessPressureScore()) {
            focus.append("Backhand technique and consistency, ");
        } else if (results.getWeaknessConsistencyScore() >= results.getWeaknessPressureScore()) {
            focus.append("Rally tolerance and consistency, ");
        } else {
            focus.append("Mental strength and pressure handling, ");
        }

        // Add secondary focus
        if (results.getNetApproachFrequency() < 20.0) {
            focus.append("Net play development, ");
        }

        focus.append("Tactical pattern recognition");

        return focus.toString();
    }

    private String findTopStrength(VideoAnalysis results) {
        double maxScore = Math.max(
            Math.max(results.getStrengthForehandScore(), results.getStrengthServeScore()),
            Math.max(results.getStrengthVolleyScore(), results.getStrengthMovementScore())
        );

        if (maxScore == results.getStrengthForehandScore()) return "forehand";
        if (maxScore == results.getStrengthServeScore()) return "serve";
        if (maxScore == results.getStrengthVolleyScore()) return "volley";
        return "movement";
    }
}
