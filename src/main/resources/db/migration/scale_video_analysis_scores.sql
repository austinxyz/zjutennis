-- Migration to scale video analysis scores from 0-10 to 0-5
-- Date: 2025-10-26

-- Update strength scores (divide by 2)
UPDATE video_analysis
SET
    strength_forehand_score = CASE WHEN strength_forehand_score IS NOT NULL THEN strength_forehand_score / 2.0 ELSE NULL END,
    strength_serve_score = CASE WHEN strength_serve_score IS NOT NULL THEN strength_serve_score / 2.0 ELSE NULL END,
    strength_volley_score = CASE WHEN strength_volley_score IS NOT NULL THEN strength_volley_score / 2.0 ELSE NULL END,
    strength_movement_score = CASE WHEN strength_movement_score IS NOT NULL THEN strength_movement_score / 2.0 ELSE NULL END
WHERE
    strength_forehand_score > 5 OR
    strength_serve_score > 5 OR
    strength_volley_score > 5 OR
    strength_movement_score > 5;

-- Update weakness scores (divide by 2)
UPDATE video_analysis
SET
    weakness_backhand_score = CASE WHEN weakness_backhand_score IS NOT NULL THEN weakness_backhand_score / 2.0 ELSE NULL END,
    weakness_consistency_score = CASE WHEN weakness_consistency_score IS NOT NULL THEN weakness_consistency_score / 2.0 ELSE NULL END,
    weakness_pressure_score = CASE WHEN weakness_pressure_score IS NOT NULL THEN weakness_pressure_score / 2.0 ELSE NULL END
WHERE
    weakness_backhand_score > 5 OR
    weakness_consistency_score > 5 OR
    weakness_pressure_score > 5;

SELECT 'Migration completed: Scaled video analysis scores from 0-10 to 0-5' AS status;
