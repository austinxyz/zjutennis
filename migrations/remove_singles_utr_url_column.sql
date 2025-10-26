-- Remove singles_utr_url column from player_statistics table
-- Since Singles and Doubles UTR URLs are the same, we only need one utr_url field

ALTER TABLE player_statistics
DROP COLUMN singles_utr_url;
