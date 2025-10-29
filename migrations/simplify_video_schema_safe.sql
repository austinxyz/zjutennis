-- Migration: Simplify Video Schema (Safe version with existence checks)
-- Date: 2025-10-28
-- Purpose: Clean up redundant fields and reorganize video/analysis structure

-- ============================================================================
-- PART 1: Add statistics fields to videos table (moved from video_analysis)
-- ============================================================================

-- Check and add total_shots
SET @dbname = DATABASE();
SET @tablename = 'videos';
SET @columnname = 'total_shots';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'SELECT ''Column total_shots already exists'' AS Info',
  'ALTER TABLE videos ADD COLUMN total_shots INT COMMENT ''Total number of shots'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Check and add errors
SET @columnname = 'errors';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'SELECT ''Column errors already exists'' AS Info',
  'ALTER TABLE videos ADD COLUMN errors INT COMMENT ''Total errors/unforced errors'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Check and add winners
SET @columnname = 'winners';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'SELECT ''Column winners already exists'' AS Info',
  'ALTER TABLE videos ADD COLUMN winners INT COMMENT ''Total winners'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Check and add aces
SET @columnname = 'aces';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'SELECT ''Column aces already exists'' AS Info',
  'ALTER TABLE videos ADD COLUMN aces INT COMMENT ''Total aces'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Check and add double_faults
SET @columnname = 'double_faults';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'SELECT ''Column double_faults already exists'' AS Info',
  'ALTER TABLE videos ADD COLUMN double_faults INT COMMENT ''Total double faults'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;

-- Check and add running_distance_meters
SET @columnname = 'running_distance_meters';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'SELECT ''Column running_distance_meters already exists'' AS Info',
  'ALTER TABLE videos ADD COLUMN running_distance_meters DOUBLE COMMENT ''Estimated running distance in meters'''
));
PREPARE alterIfNotExists FROM @preparedStatement;
EXECUTE alterIfNotExists;
DEALLOCATE PREPARE alterIfNotExists;


-- ============================================================================
-- PART 2: Simplify video_analysis table (remove redundant fields)
-- ============================================================================

-- Drop match_id foreign key constraint if exists
SET @tablename = 'video_analysis';
SET @fkname = 'fk_video_analysis_match';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
   WHERE table_schema = @dbname AND table_name = @tablename AND constraint_name = @fkname) > 0,
  'ALTER TABLE video_analysis DROP FOREIGN KEY fk_video_analysis_match',
  'SELECT ''FK fk_video_analysis_match does not exist'' AS Info'
));
PREPARE dropFKIfExists FROM @preparedStatement;
EXECUTE dropFKIfExists;
DEALLOCATE PREPARE dropFKIfExists;

-- Drop match_id index if exists
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
   WHERE table_schema = @dbname AND table_name = @tablename AND index_name = 'idx_match_id') > 0,
  'ALTER TABLE video_analysis DROP INDEX idx_match_id',
  'SELECT ''Index idx_match_id does not exist'' AS Info'
));
PREPARE dropIdxIfExists FROM @preparedStatement;
EXECUTE dropIdxIfExists;
DEALLOCATE PREPARE dropIdxIfExists;

-- Drop match_id column if exists
SET @columnname = 'match_id';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN match_id',
  'SELECT ''Column match_id already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop duplicate video fields if they exist
SET @columns_to_drop = 'title,description,video_url,thumbnail_url,duration_seconds,video_file_path,upload_date,match_date';

-- Drop title
SET @columnname = 'title';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN title',
  'SELECT ''Column title already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop description
SET @columnname = 'description';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN description',
  'SELECT ''Column description already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop video_url
SET @columnname = 'video_url';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN video_url',
  'SELECT ''Column video_url already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop thumbnail_url
SET @columnname = 'thumbnail_url';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN thumbnail_url',
  'SELECT ''Column thumbnail_url already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop duration_seconds
SET @columnname = 'duration_seconds';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN duration_seconds',
  'SELECT ''Column duration_seconds already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop video_file_path
SET @columnname = 'video_file_path';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN video_file_path',
  'SELECT ''Column video_file_path already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop upload_date and its index
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
   WHERE table_schema = @dbname AND table_name = @tablename AND index_name = 'idx_upload_date') > 0,
  'ALTER TABLE video_analysis DROP INDEX idx_upload_date',
  'SELECT ''Index idx_upload_date does not exist'' AS Info'
));
PREPARE dropIdxIfExists FROM @preparedStatement;
EXECUTE dropIdxIfExists;
DEALLOCATE PREPARE dropIdxIfExists;

SET @columnname = 'upload_date';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN upload_date',
  'SELECT ''Column upload_date already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop match_date and its index
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
   WHERE table_schema = @dbname AND table_name = @tablename AND index_name = 'idx_match_date') > 0,
  'ALTER TABLE video_analysis DROP INDEX idx_match_date',
  'SELECT ''Index idx_match_date does not exist'' AS Info'
));
PREPARE dropIdxIfExists FROM @preparedStatement;
EXECUTE dropIdxIfExists;
DEALLOCATE PREPARE dropIdxIfExists;

SET @columnname = 'match_date';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN match_date',
  'SELECT ''Column match_date already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

-- Drop statistics fields (moved to videos table)
SET @columnname = 'total_shots';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN total_shots',
  'SELECT ''Column total_shots already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

SET @columnname = 'errors';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN errors',
  'SELECT ''Column errors already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

SET @columnname = 'winners';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN winners',
  'SELECT ''Column winners already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

SET @columnname = 'aces';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN aces',
  'SELECT ''Column aces already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

SET @columnname = 'double_faults';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN double_faults',
  'SELECT ''Column double_faults already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

SET @columnname = 'running_distance_meters';
SET @preparedStatement = (SELECT IF(
  (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
   WHERE table_schema = @dbname AND table_name = @tablename AND column_name = @columnname) > 0,
  'ALTER TABLE video_analysis DROP COLUMN running_distance_meters',
  'SELECT ''Column running_distance_meters already dropped'' AS Info'
));
PREPARE dropIfExists FROM @preparedStatement;
EXECUTE dropIfExists;
DEALLOCATE PREPARE dropIfExists;

SELECT 'Migration completed: Video schema simplified' AS status;
