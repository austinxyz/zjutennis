-- Add status fields for UTR and NTRP ratings
ALTER TABLE player_statistics
ADD COLUMN utr_status VARCHAR(20),
ADD COLUMN ntrp_status VARCHAR(20);
