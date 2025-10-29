-- Migration: Simplify Video Schema
-- Date: 2025-10-28
-- Purpose: Clean up redundant fields and reorganize video/analysis structure

-- ============================================================================
-- PART 1: Simplify videos table
-- ============================================================================

-- Remove title from videos (title can be derived from match or use description)
ALTER TABLE videos
DROP COLUMN title;

-- Remove match_date from videos (can get from matches.match_time)
ALTER TABLE videos
DROP COLUMN match_date;

-- Note: match_id stays optional (nullable) because FK constraint has ON DELETE SET NULL
-- This is acceptable - videos can exist temporarily without a match

-- Add match statistics fields to videos table (moved from video_analysis)
ALTER TABLE videos
ADD COLUMN total_shots INT COMMENT 'Total number of shots',
ADD COLUMN errors INT COMMENT 'Total errors/unforced errors',
ADD COLUMN winners INT COMMENT 'Total winners',
ADD COLUMN aces INT COMMENT 'Total aces',
ADD COLUMN double_faults INT COMMENT 'Total double faults',
ADD COLUMN running_distance_meters DOUBLE COMMENT 'Estimated running distance in meters';


-- ============================================================================
-- PART 2: Simplify video_analysis table (remove redundant fields)
-- ============================================================================

-- Drop match_id (can get from video -> match)
ALTER TABLE video_analysis
DROP FOREIGN KEY fk_video_analysis_match,
DROP INDEX idx_match_id,
DROP COLUMN match_id;

-- Drop duplicate video fields (exist in videos table)
ALTER TABLE video_analysis
DROP COLUMN title,
DROP COLUMN description,
DROP COLUMN video_url,
DROP COLUMN thumbnail_url,
DROP COLUMN duration_seconds,
DROP COLUMN video_file_path,
DROP COLUMN upload_date,
DROP COLUMN match_date;

-- Drop statistics fields (moved to videos table)
ALTER TABLE video_analysis
DROP COLUMN total_shots,
DROP COLUMN errors,
DROP COLUMN winners,
DROP COLUMN aces,
DROP COLUMN double_faults,
DROP COLUMN running_distance_meters;


-- ============================================================================
-- SUMMARY OF CHANGES
-- ============================================================================

-- videos table:
--   REMOVED: title, match_date (2 columns)
--   ADDED: total_shots, errors, winners, aces, double_faults, running_distance_meters (6 columns)
--   RESULT: 10 -> 14 columns (net +4)

-- video_analysis table:
--   REMOVED: match_id, title, description, video_url, thumbnail_url, duration_seconds,
--            video_file_path, upload_date, match_date, total_shots, errors, winners,
--            aces, double_faults, running_distance_meters (15 columns)
--   RESULT: 41 -> 26 columns (net -15)

-- Total columns before: 74 (videos 10 + video_analysis 41 + matches 23)
-- Total columns after: 63 (videos 14 + video_analysis 26 + matches 23)
-- Reduction: 11 columns removed

SELECT 'Migration completed: Video schema simplified' AS status;
