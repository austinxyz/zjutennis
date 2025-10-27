-- Create matches table
CREATE TABLE IF NOT EXISTS matches (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Match basic information
    match_type VARCHAR(20) NOT NULL, -- 'singles' or 'doubles'
    match_time DATETIME NOT NULL,
    location VARCHAR(200),
    tournament_name VARCHAR(200),
    round VARCHAR(50),

    -- Match score (e.g., "6-4, 6-3" or "6-4, 3-6, 10-8")
    score VARCHAR(100),

    -- Match result
    result VARCHAR(20) NOT NULL DEFAULT 'complete', -- 'complete', 'retired', 'default', 'double_default'
    winner_side VARCHAR(10), -- 'team1' or 'team2' (null if not complete)

    -- Additional metadata
    duration_minutes INT,
    surface VARCHAR(50), -- 'hard', 'clay', 'grass', 'carpet'
    indoor BOOLEAN DEFAULT FALSE,
    notes TEXT,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    INDEX idx_match_time (match_time),
    INDEX idx_match_type (match_type),
    INDEX idx_result (result)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create match_players junction table to link players to matches
-- Supports both singles (2 players) and doubles (4 players)
CREATE TABLE IF NOT EXISTS match_players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    match_id BIGINT NOT NULL,

    -- Player reference (nullable to support non-registered players)
    player_id BIGINT,

    -- Player name (for non-registered players or override)
    player_name VARCHAR(100) NOT NULL,

    -- Team assignment
    team VARCHAR(10) NOT NULL, -- 'team1' or 'team2'

    -- Position in doubles (1 or 2, null for singles)
    position INT,

    -- Whether this is our team member (tracked player) vs opponent
    is_our_team BOOLEAN DEFAULT FALSE,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_match_players_match FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE CASCADE,
    CONSTRAINT fk_match_players_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE SET NULL,

    INDEX idx_match_id (match_id),
    INDEX idx_player_id (player_id),
    INDEX idx_team (team),
    INDEX idx_is_our_team (is_our_team),

    -- Ensure unique player per match
    UNIQUE KEY unique_match_player (match_id, player_id, team, position)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Add match_id to video_analysis table (only if doesn't exist)
SET @dbname = 'zjualumni';
SET @tablename = 'video_analysis';
SET @columnname = 'match_id';
SET @preparedStatement = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (column_name = @columnname)
  ) > 0,
  'SELECT 1',
  'ALTER TABLE video_analysis ADD COLUMN match_id BIGINT AFTER player_id, ADD INDEX idx_match_id (match_id)'
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Add foreign key constraint if doesn't exist
SET @fkname = 'fk_video_analysis_match';
SET @preparedStatement2 = (SELECT IF(
  (
    SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
    WHERE
      (table_name = @tablename)
      AND (table_schema = @dbname)
      AND (constraint_name = @fkname)
  ) > 0,
  'SELECT 1',
  'ALTER TABLE video_analysis ADD CONSTRAINT fk_video_analysis_match FOREIGN KEY (match_id) REFERENCES matches(id) ON DELETE SET NULL'
));
PREPARE alterIfNotExists2 FROM @preparedStatement2;
EXECUTE alterIfNotExists2;
DEALLOCATE PREPARE alterIfNotExists2;
