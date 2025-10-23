-- Database Migration Script
-- Purpose: Add player_alumni table and migrate graduation_year from players table
-- Date: 2025-10-23
-- Description: Creates alumni information table with player's and couple's graduation universities

-- Step 1: Create the player_alumni table
CREATE TABLE IF NOT EXISTS player_alumni (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL UNIQUE,

    -- Player's Graduation Universities
    graduation_university1 VARCHAR(200),
    graduation_year1 INT,
    graduation_university2 VARCHAR(200),
    graduation_year2 INT,
    graduation_university3 VARCHAR(200),
    graduation_year3 INT,

    -- Couple's Graduation Universities
    couple_graduation_university1 VARCHAR(200),
    couple_graduation_year1 INT,
    couple_graduation_university2 VARCHAR(200),
    couple_graduation_year2 INT,
    couple_graduation_university3 VARCHAR(200),
    couple_graduation_year3 INT,

    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
);

-- Step 2: Migrate existing graduation_year data from players table to player_alumni table
-- Only for players that have a graduation_year set
INSERT INTO player_alumni (player_id, graduation_year1, created_at)
SELECT id, graduation_year, NOW()
FROM players
WHERE graduation_year IS NOT NULL;

-- Step 3: Drop the graduation_year and major columns from players table
-- (Optional - you may want to keep them for a while as backup)
ALTER TABLE players
DROP COLUMN graduation_year,
DROP COLUMN major;

-- Verify the changes
DESCRIBE player_alumni;
DESCRIBE players;

-- Show sample data
SELECT * FROM player_alumni LIMIT 5;
