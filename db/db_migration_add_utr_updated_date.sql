-- Database Migration Script
-- Purpose: Add utr_updated_date column to player_statistics table
-- Date: 2025-10-23
-- Description: This column tracks when a player's UTR rating was last updated

-- Add the utr_updated_date column to the player_statistics table
ALTER TABLE player_statistics
ADD COLUMN utr_updated_date DATETIME NULL
AFTER utr_url;

-- Verify the column was added successfully
DESCRIBE player_statistics;

-- Optional: Set initial value for existing records (uncomment if needed)
-- UPDATE player_statistics
-- SET utr_updated_date = updated_at
-- WHERE utr_rating IS NOT NULL AND utr_updated_date IS NULL;
