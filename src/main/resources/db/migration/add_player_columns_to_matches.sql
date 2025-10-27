-- Migration: Add player columns to matches table and migrate data from match_players
-- Date: 2025-01-26

-- Step 1: Add new player columns to matches table
ALTER TABLE matches
ADD COLUMN player1_id BIGINT NULL,
ADD COLUMN player1_name VARCHAR(100) NULL,
ADD COLUMN player2_id BIGINT NULL,
ADD COLUMN player2_name VARCHAR(100) NULL,
ADD COLUMN opponent_player1_id BIGINT NULL,
ADD COLUMN opponent_player1_name VARCHAR(100) NULL,
ADD COLUMN opponent_player2_id BIGINT NULL,
ADD COLUMN opponent_player2_name VARCHAR(100) NULL;

-- Step 2: Add foreign key constraints
ALTER TABLE matches
ADD CONSTRAINT fk_match_player1 FOREIGN KEY (player1_id) REFERENCES players(id) ON DELETE SET NULL,
ADD CONSTRAINT fk_match_player2 FOREIGN KEY (player2_id) REFERENCES players(id) ON DELETE SET NULL,
ADD CONSTRAINT fk_match_opponent_player1 FOREIGN KEY (opponent_player1_id) REFERENCES players(id) ON DELETE SET NULL,
ADD CONSTRAINT fk_match_opponent_player2 FOREIGN KEY (opponent_player2_id) REFERENCES players(id) ON DELETE SET NULL;

-- Step 3: Migrate data from match_players to matches
-- Team 1, Position 1 or first team1 player
UPDATE matches m
LEFT JOIN (
    SELECT match_id, player_id, player_name
    FROM match_players
    WHERE team = 'team1' AND (position = 1 OR position IS NULL)
    ORDER BY id
    LIMIT 1
) mp1 ON m.id = mp1.match_id
SET m.player1_id = mp1.player_id,
    m.player1_name = mp1.player_name
WHERE mp1.match_id IS NOT NULL;

-- Team 1, Position 2 (for doubles)
UPDATE matches m
LEFT JOIN (
    SELECT match_id, player_id, player_name
    FROM match_players
    WHERE team = 'team1' AND position = 2
) mp2 ON m.id = mp2.match_id
SET m.player2_id = mp2.player_id,
    m.player2_name = mp2.player_name
WHERE mp2.match_id IS NOT NULL;

-- Team 2 (Opponent), Position 1 or first team2 player
UPDATE matches m
LEFT JOIN (
    SELECT match_id, player_id, player_name
    FROM match_players
    WHERE team = 'team2' AND (position = 1 OR position IS NULL)
    ORDER BY id
    LIMIT 1
) opp1 ON m.id = opp1.match_id
SET m.opponent_player1_id = opp1.player_id,
    m.opponent_player1_name = opp1.player_name
WHERE opp1.match_id IS NOT NULL;

-- Team 2 (Opponent), Position 2 (for doubles)
UPDATE matches m
LEFT JOIN (
    SELECT match_id, player_id, player_name
    FROM match_players
    WHERE team = 'team2' AND position = 2
) opp2 ON m.id = opp2.match_id
SET m.opponent_player2_id = opp2.player_id,
    m.opponent_player2_name = opp2.player_name
WHERE opp2.match_id IS NOT NULL;

-- Step 4: Drop the match_players table (after verifying data migration)
-- IMPORTANT: Comment this out until you've verified the data migration worked correctly!
-- DROP TABLE match_players;
