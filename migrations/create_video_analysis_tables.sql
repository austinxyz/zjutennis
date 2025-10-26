-- Create video_analysis table for player match video analysis
-- Migration: Add video analysis feature (PRD 2.1)

CREATE TABLE IF NOT EXISTS video_analysis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,

    -- Video Information
    title VARCHAR(200) NOT NULL COMMENT 'Video title or match name',
    description TEXT COMMENT 'Video description',
    video_url VARCHAR(500) COMMENT 'External video URL (YouTube, etc.)',
    video_file_path VARCHAR(500) COMMENT 'Local uploaded video file path',
    thumbnail_url VARCHAR(500) COMMENT 'Video thumbnail image URL',
    duration_seconds INT COMMENT 'Video duration in seconds',
    upload_date DATETIME COMMENT 'Video upload date',
    match_date DATE COMMENT 'Actual match date',

    -- Match Summary Statistics
    total_shots INT COMMENT 'Total number of shots',
    errors INT COMMENT 'Total errors/unforced errors',
    winners INT COMMENT 'Total winners',
    aces INT COMMENT 'Total aces',
    double_faults INT COMMENT 'Total double faults',
    running_distance_meters DOUBLE COMMENT 'Estimated running distance in meters',

    -- AI Analysis Results
    ai_analyzed BOOLEAN DEFAULT FALSE COMMENT 'Whether AI analysis has been performed',
    ai_analysis_date DATETIME COMMENT 'When AI analysis was performed',

    -- Strengths Analysis
    strength_forehand_score DOUBLE COMMENT 'Forehand attack effectiveness (0-10)',
    strength_serve_score DOUBLE COMMENT 'Serve effectiveness (0-10)',
    strength_volley_score DOUBLE COMMENT 'Volley effectiveness (0-10)',
    strength_movement_score DOUBLE COMMENT 'Movement and court coverage (0-10)',
    strength_summary TEXT COMMENT 'AI-generated strengths summary',

    -- Weaknesses Analysis
    weakness_backhand_score DOUBLE COMMENT 'Backhand passive error rate (0-10, higher = more errors)',
    weakness_consistency_score DOUBLE COMMENT 'Consistency issues (0-10, higher = more issues)',
    weakness_pressure_score DOUBLE COMMENT 'Performance under pressure (0-10, higher = more issues)',
    weakness_summary TEXT COMMENT 'AI-generated weaknesses summary',

    -- Tactical Analysis
    tactical_style VARCHAR(50) COMMENT 'Playing style: aggressive, defensive, balanced, all-court',
    aggression_index DOUBLE COMMENT 'Aggression level (0-100)',
    net_approach_frequency DOUBLE COMMENT 'Net approach frequency (%)',
    baseline_rally_preference DOUBLE COMMENT 'Baseline rally preference (%)',
    tactical_summary TEXT COMMENT 'AI-generated tactical analysis',

    -- AI Recommendations
    ai_recommendations TEXT COMMENT 'AI-generated improvement recommendations (JSON format)',
    training_focus_areas TEXT COMMENT 'Suggested training focus areas',

    -- Video Keyframes/Highlights
    keyframes_json TEXT COMMENT 'JSON array of keyframe timestamps and descriptions',

    -- Status and Metadata
    status VARCHAR(20) DEFAULT 'pending' COMMENT 'Status: pending, processing, completed, failed',
    processing_notes TEXT COMMENT 'Notes about processing status or errors',

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign Keys
    CONSTRAINT fk_video_analysis_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,

    -- Indexes
    INDEX idx_player_id (player_id),
    INDEX idx_match_date (match_date),
    INDEX idx_upload_date (upload_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Player match video analysis and AI insights';
