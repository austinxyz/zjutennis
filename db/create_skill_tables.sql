-- Database migration script for player skill evaluation tables

USE zjualumni;

-- Create player_skills table
CREATE TABLE IF NOT EXISTS player_skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,

    -- Technical Skills (1-10 scale)
    forehand DOUBLE,
    backhand DOUBLE,
    baseline DOUBLE,
    volley DOUBLE,
    smash DOUBLE,
    serve DOUBLE,
    return_serve DOUBLE,

    -- Mental and Physical Skills (1-10 scale)
    mental DOUBLE,
    movement DOUBLE,
    fitness DOUBLE,

    -- Strategy and Tactics (1-10 scale)
    court_positioning DOUBLE,
    shot_selection DOUBLE,
    competitive_spirit DOUBLE,

    -- Notes and Comments
    strengths TEXT,
    weaknesses TEXT,
    notes TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    INDEX idx_player_skills_player_id (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create player_statistics table
CREATE TABLE IF NOT EXISTS player_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,

    -- Rating Systems
    utr_rating DOUBLE,
    ntrp_rating DOUBLE,
    self_rating DOUBLE,

    -- Match Statistics
    total_matches INT,
    wins INT,
    losses INT,
    win_rate DOUBLE,
    singles_win_rate DOUBLE,
    doubles_win_rate DOUBLE,

    -- Activity Level
    play_frequency VARCHAR(50),  -- daily, weekly, monthly, occasionally
    matches_per_month INT,
    practice_hours_per_week DOUBLE,

    -- Competitive Level
    competitive_level VARCHAR(50),  -- recreational, intermediate, advanced, professional
    tournament_participation BOOLEAN,
    league_participation BOOLEAN,

    -- Performance Metrics
    serve_percentage DOUBLE,
    first_serve_percentage DOUBLE,
    break_point_conversion DOUBLE,
    average_match_duration_minutes INT,

    -- Preferred Playing Style
    preferred_surface VARCHAR(50),  -- hard, clay, grass, carpet
    preferred_playing_style VARCHAR(50),  -- baseline, serve-volley, all-court
    dominant_hand VARCHAR(20),  -- right, left, ambidextrous

    -- Partner Preferences (for doubles)
    preferred_doubles_position VARCHAR(50),  -- ad-court, deuce-court, both

    -- Availability and Goals
    availability TEXT,
    goals TEXT,
    last_match_date TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    INDEX idx_player_statistics_player_id (player_id),
    INDEX idx_competitive_level (competitive_level),
    INDEX idx_play_frequency (play_frequency),
    INDEX idx_ntrp_rating (ntrp_rating),
    INDEX idx_utr_rating (utr_rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Verify tables creation
SHOW TABLES LIKE 'player_%';

-- Display table structures
DESCRIBE player_skills;
DESCRIBE player_statistics;
