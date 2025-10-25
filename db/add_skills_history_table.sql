-- Migration: Add player_skills_history table for tracking strengths/weaknesses changes over time
-- Date: 2025-10-24
-- Description: Creates a history table to track all changes to player strengths and weaknesses

-- Create player_skills_history table
CREATE TABLE IF NOT EXISTS player_skills_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    strengths TEXT,
    weaknesses TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(255),

    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    INDEX idx_player_created (player_id, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Optional: Migrate existing strengths/weaknesses to history table
-- This creates an initial history record for players who already have strengths/weaknesses
INSERT INTO player_skills_history (player_id, strengths, weaknesses, created_at)
SELECT
    p.id as player_id,
    ps.strengths,
    ps.weaknesses,
    ps.updated_at as created_at
FROM players p
INNER JOIN player_skills ps ON p.id = ps.player_id
WHERE (ps.strengths IS NOT NULL AND ps.strengths != '')
   OR (ps.weaknesses IS NOT NULL AND ps.weaknesses != '');

-- Add index for better query performance
CREATE INDEX idx_player_skills_history_player_id ON player_skills_history(player_id);
CREATE INDEX idx_player_skills_history_created_at ON player_skills_history(created_at DESC);
