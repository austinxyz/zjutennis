-- Add URL fields to player_statistics table
ALTER TABLE player_statistics
ADD COLUMN utr_url VARCHAR(500),
ADD COLUMN ntrp_url VARCHAR(500),
ADD COLUMN dynamic_rating DOUBLE,
ADD COLUMN dynamic_rating_url VARCHAR(500);
