# Video and VideoAnalysis Entities - Implementation Complete

**Date:** 2025-10-28
**Status:** ✅ Successfully Created

---

## Summary

Created Video and VideoAnalysis entities with proper cascade delete relationships as requested:
- **Match → Video** (OneToOne, cascade delete)
- **Video → VideoAnalysis** (OneToMany, cascade delete)
- **Player → VideoAnalysis** (OneToMany, cascade delete)

---

## Entity Structure

### 1. Match Entity (Updated)

**File:** `src/main/java/com/zjutennis/model/Match.java`

**Relationship:**
- OneToOne with Video
- When match is deleted → video is deleted
- Cascade: `ALL`, orphanRemoval: `true`

**Fields Added:**
```java
@OneToOne(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference("match-video")
private Video video;
```

**Helper Methods:**
```java
public void setVideo(Video video)  // Sets bidirectional relationship
public void removeVideo()          // Removes video from match
```

---

### 2. Video Entity (New)

**File:** `src/main/java/com/zjutennis/model/Video.java`

**Table:** `videos`

**Relationships:**
- OneToOne with Match (required, owner side)
- OneToMany with VideoAnalysis
- When video is deleted → all analyses are deleted
- Cascade: `ALL`, orphanRemoval: `true`

**Fields (14 total):**
```java
// Primary Key
private Long id;

// Video Metadata
private String description;
private String videoUrl;
private String thumbnailUrl;
private Integer durationSeconds;

// Relationship (required)
@OneToOne
@JoinColumn(name = "match_id", nullable = false)
private Match match;

// Match Statistics
private Integer totalShots;
private Integer errors;
private Integer winners;
private Integer aces;
private Integer doubleFaults;
private Double runningDistanceMeters;

// Relationships
@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
private List<VideoAnalysis> analyses;

// Timestamps
private LocalDateTime createdAt;
private LocalDateTime updatedAt;
```

**Helper Methods:**
```java
public void addAnalysis(VideoAnalysis analysis)     // Add analysis
public void removeAnalysis(VideoAnalysis analysis)  // Remove analysis
```

---

### 3. VideoAnalysis Entity (New)

**File:** `src/main/java/com/zjutennis/model/VideoAnalysis.java`

**Table:** `video_analysis`

**Relationships:**
- ManyToOne with Video (required)
- ManyToOne with Player (required)
- When video is deleted → this analysis is deleted
- When player is deleted → this analysis is deleted

**Fields (26 total):**
```java
// Primary Key
private Long id;

// Relationships (both required)
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "video_id", nullable = false)
private Video video;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "player_id", nullable = false)
private Player player;

// AI Analysis Status
private Boolean aiAnalyzed = false;
private LocalDateTime aiAnalysisDate;

// Strengths (0-5 scale)
private Double strengthForehandScore;
private Double strengthServeScore;
private Double strengthVolleyScore;
private Double strengthMovementScore;
private String strengthSummary;

// Weaknesses (0-5 scale)
private Double weaknessBackhandScore;
private Double weaknessConsistencyScore;
private Double weaknessPressureScore;
private String weaknessSummary;

// Tactical Analysis
private String tacticalStyle;
private Double aggressionIndex;           // 0-100
private Double netApproachFrequency;      // Percentage
private Double baselineRallyPreference;   // Percentage
private String tacticalSummary;

// AI Recommendations
private String aiRecommendations;  // JSON format
private String trainingFocusAreas;
private String keyframesJson;      // JSON array

// Status
private String status = "pending"; // pending/processing/completed/failed
private String processingNotes;

// Timestamps
private LocalDateTime createdAt;
private LocalDateTime updatedAt;
```

---

### 4. Player Entity (Updated)

**File:** `src/main/java/com/zjutennis/model/Player.java`

**Relationship Added:**
- OneToMany with VideoAnalysis
- When player is deleted → all their analyses are deleted
- Cascade: `ALL`, orphanRemoval: `true`

**Fields Added:**
```java
@OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference("player-analyses")
private List<VideoAnalysis> videoAnalyses;
```

---

## Repository Interfaces

### VideoRepository

**File:** `src/main/java/com/zjutennis/repository/VideoRepository.java`

**Methods:**
```java
Optional<Video> findByMatchId(Long matchId)
boolean existsByMatchId(Long matchId)
void deleteByMatchId(Long matchId)
```

---

### VideoAnalysisRepository

**File:** `src/main/java/com/zjutennis/repository/VideoAnalysisRepository.java`

**Methods:**
```java
// Find operations
List<VideoAnalysis> findByVideoId(Long videoId)
List<VideoAnalysis> findByPlayerId(Long playerId)
Optional<VideoAnalysis> findByVideoIdAndPlayerId(Long videoId, Long playerId)

// Existence checks
boolean existsByVideoIdAndPlayerId(Long videoId, Long playerId)

// Delete operations
void deleteByVideoId(Long videoId)
void deleteByPlayerId(Long playerId)

// Count operations
long countByVideoId(Long videoId)
long countByPlayerId(Long playerId)

// Filtered find
List<VideoAnalysis> findByPlayerIdAndAiAnalyzed(Long playerId, Boolean aiAnalyzed)
```

---

## Cascade Delete Behavior

### Scenario 1: Delete Match
```
User deletes Match
  ↓
Match.video is deleted (CASCADE ALL)
  ↓
Video.analyses are all deleted (CASCADE ALL)
  ↓
All VideoAnalysis records for that video are deleted
```

**Result:** Match deletion removes video and all analyses

---

### Scenario 2: Delete Video
```
User deletes Video
  ↓
Video.analyses are all deleted (CASCADE ALL)
  ↓
All VideoAnalysis records for that video are deleted
```

**Result:** Video deletion removes all its analyses (match remains)

---

### Scenario 3: Delete Player
```
User deletes Player
  ↓
Player.videoAnalyses are all deleted (CASCADE ALL)
  ↓
All VideoAnalysis records for that player are deleted
```

**Result:** Player deletion removes all their analyses (videos remain)

---

## Entity Relationship Diagram

```
┌─────────────┐
│   Match     │
│ (1 match)   │
└──────┬──────┘
       │
       │ OneToOne (owner: Video)
       │ ON DELETE: CASCADE ALL
       │
┌──────▼──────┐
│    Video    │
│  (1 video)  │
└──────┬──────┘
       │
       │ OneToMany
       │ ON DELETE: CASCADE ALL
       │
┌──────▼──────────────┐
│  VideoAnalysis      │
│  (N analyses)       │
└─────────────────────┘
       │
       │ ManyToOne
       │ ON DELETE: CASCADE ALL
       │
┌──────▼──────┐
│   Player    │
│ (1 player)  │
└─────────────┘
```

**Cardinality:**
- Match (1) ←→ (1) Video
- Video (1) ←→ (N) VideoAnalysis
- Player (1) ←→ (N) VideoAnalysis

---

## Database Schema Alignment

### videos Table ✅ Matches Entity
```
✓ id
✓ description
✓ video_url
✓ thumbnail_url
✓ duration_seconds
✓ match_id (FK, NOT NULL)
✓ total_shots
✓ errors
✓ winners
✓ aces
✓ double_faults
✓ running_distance_meters
✓ created_at
✓ updated_at
```

### video_analysis Table ✅ Matches Entity
```
✓ id
✓ video_id (FK, NOT NULL)
✓ player_id (FK, NOT NULL)
✓ ai_analyzed
✓ ai_analysis_date
✓ strength_* (4 fields)
✓ strength_summary
✓ weakness_* (3 fields)
✓ weakness_summary
✓ tactical_* (4 fields)
✓ tactical_summary
✓ ai_recommendations
✓ training_focus_areas
✓ keyframes_json
✓ status
✓ processing_notes
✓ created_at
✓ updated_at
```

All entity fields correctly map to database columns!

---

## JSON Serialization

**Annotations used:**
- `@JsonManagedReference` - Parent side (owns the relationship)
- `@JsonBackReference` - Child side (prevents circular reference)

**References:**
- `"match-video"` - Match ↔ Video
- `"video-analyses"` - Video ↔ VideoAnalysis
- `"player-analyses"` - Player ↔ VideoAnalysis

**Result:** No circular JSON serialization issues

---

## Usage Examples

### Create Video for Match
```java
Match match = matchRepository.findById(matchId).orElseThrow();
Video video = new Video();
video.setDescription("Final match video");
video.setVideoUrl("https://youtube.com/...");
video.setMatch(match);
videoRepository.save(video);
```

### Add Analysis to Video
```java
Video video = videoRepository.findById(videoId).orElseThrow();
Player player = playerRepository.findById(playerId).orElseThrow();

VideoAnalysis analysis = new VideoAnalysis();
analysis.setVideo(video);
analysis.setPlayer(player);
analysis.setStatus("pending");
videoAnalysisRepository.save(analysis);

// Or use helper method
video.addAnalysis(analysis);
videoRepository.save(video);
```

### Delete Video (cascades to analyses)
```java
Video video = videoRepository.findById(videoId).orElseThrow();
videoRepository.delete(video);
// All analyses for this video are automatically deleted
```

### Delete Match (cascades to video and analyses)
```java
Match match = matchRepository.findById(matchId).orElseThrow();
matchRepository.delete(match);
// Video is deleted
// All analyses are deleted
```

---

## Files Created/Modified

### Created (4 files)
1. ✅ `src/main/java/com/zjutennis/model/Video.java`
2. ✅ `src/main/java/com/zjutennis/model/VideoAnalysis.java`
3. ✅ `src/main/java/com/zjutennis/repository/VideoRepository.java`
4. ✅ `src/main/java/com/zjutennis/repository/VideoAnalysisRepository.java`

### Modified (2 files)
1. ✅ `src/main/java/com/zjutennis/model/Match.java` - Added video relationship
2. ✅ `src/main/java/com/zjutennis/model/Player.java` - Added videoAnalyses relationship

---

## Next Steps

To complete the implementation:

1. **Create Services**
   - `VideoService` - CRUD for videos
   - `VideoAnalysisService` - CRUD for analyses

2. **Create Controllers**
   - `VideoController` - REST API for videos
   - `VideoAnalysisController` - REST API for analyses

3. **Create DTOs**
   - `VideoRequest/Response`
   - `VideoAnalysisRequest/Response`

4. **Update Match Service/Controller**
   - Add methods to attach/detach video

5. **Test Cascade Deletes**
   - Verify match deletion removes video
   - Verify video deletion removes analyses
   - Verify player deletion removes analyses

---

## Conclusion

✅ Video and VideoAnalysis entities successfully created with:
- Proper bidirectional relationships
- Cascade delete behavior as requested
- Clean entity mapping to database schema
- Repository interfaces with useful query methods
- Helper methods for relationship management

The backend is ready for service and controller implementation!
