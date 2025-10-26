-- Add Singles UTR columns to player_statistics table
-- Migration: Add Singles UTR rating fields separate from Doubles UTR

ALTER TABLE player_statistics
ADD COLUMN singles_utr_rating DOUBLE AFTER player_id,
ADD COLUMN singles_utr_status VARCHAR(20) AFTER singles_utr_rating,
ADD COLUMN singles_utr_url VARCHAR(500) AFTER singles_utr_status,
ADD COLUMN singles_utr_updated_date DATETIME AFTER singles_utr_url;

-- Add comments to describe the columns
ALTER TABLE player_statistics
MODIFY COLUMN singles_utr_rating DOUBLE COMMENT 'Singles UTR rating (0-16+ scale)',
MODIFY COLUMN singles_utr_status VARCHAR(20) COMMENT 'Singles UTR status: rated, projected, unrated',
MODIFY COLUMN singles_utr_url VARCHAR(500) COMMENT 'URL to singles UTR profile',
MODIFY COLUMN singles_utr_updated_date DATETIME COMMENT 'Last update date for singles UTR rating';
