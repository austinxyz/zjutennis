-- Remove duplicate UTR rating and status fields from players table
-- These fields are now only maintained in player_statistics table

ALTER TABLE players
DROP COLUMN utr_rating;

ALTER TABLE players
DROP COLUMN status;
