<template>
  <div class="space-y-6">
    <!-- Header with Back Button -->
    <div class="flex items-center gap-4 mb-6">
      <Button variant="outline" size="sm" @click="goBack">
        <ArrowLeft class="w-4 h-4 mr-2" />
        Back to Matches
      </Button>
    </div>

    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Manage Match Videos</h1>
        <p class="text-sm text-muted-foreground mt-1" v-if="matchInfo">
          {{ formatMatchTitle(matchInfo) }}
        </p>
        <div class="flex items-center gap-3 mt-2">
          <p class="text-xs text-muted-foreground">
            {{ videos.length }} video{{ videos.length !== 1 ? 's' : '' }}
          </p>
          <div v-if="matchPlayers.length > 0" class="flex items-center gap-2">
            <Badge :variant="analysisCompletionStatus.completed ? 'success' : 'secondary'">
              Analysis: {{ analysisCompletionStatus.analyzed }}/{{ analysisCompletionStatus.total }} players ({{ analysisCompletionStatus.percentage }}%)
            </Badge>
          </div>
        </div>
      </div>
      <Button @click="showUploadForm = true" v-if="!showUploadForm">
        <Plus class="w-4 h-4 mr-2" />
        Add Video
      </Button>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading match and videos...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <template v-else>
      <!-- Video Upload Form -->
      <VideoUploadForm
        v-if="showUploadForm"
        :video="editingVideo"
        :is-edit="!!editingVideo"
        :match-id="matchId"
        :available-players="matchPlayers"
        @submit="handleSubmitVideo"
        @cancel="cancelForm"
      />

      <!-- Videos Grouped by Match Date -->
      <div v-if="videos.length > 0" class="space-y-8">
        <div v-for="group in videosByDate" :key="group.date" class="space-y-4">
          <div class="flex items-center gap-3">
            <h3 class="text-lg font-semibold">{{ group.dateLabel }}</h3>
            <Badge variant="secondary">{{ group.videos.length }} video{{ group.videos.length !== 1 ? 's' : '' }}</Badge>
          </div>
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            <VideoCard
              v-for="video in group.videos"
              :key="video.id"
              :video="video"
              @select="selectVideo"
              @edit="editVideo"
              @delete="deleteVideo"
            />
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <Card v-else-if="!showUploadForm">
        <CardContent class="pt-6">
          <div class="text-center py-12">
            <Video class="w-16 h-16 mx-auto text-muted-foreground mb-4 opacity-50" />
            <h3 class="text-lg font-semibold mb-2">No Videos Yet</h3>
            <p class="text-muted-foreground mb-6">
              Add videos for this match to help players analyze their performance
            </p>
            <Button @click="showUploadForm = true">
              <Plus class="w-4 h-4 mr-2" />
              Add First Video
            </Button>
          </div>
        </CardContent>
      </Card>
    </template>

    <!-- Video Detail Modal (if selected) -->
    <VideoDetailModal
      v-if="selectedVideo"
      :video="selectedVideo"
      @close="selectedVideo = null"
      @updated="loadVideos"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import Button from '../../components/ui/Button.vue';
import Badge from '../../components/ui/Badge.vue';
import { Video, Plus, ArrowLeft } from 'lucide-vue-next';
import VideoCard from '../../components/VideoCard.vue';
import VideoDetailModal from '../../components/VideoDetailModal.vue';
import VideoUploadForm from '../../components/VideoUploadForm.vue';
import videoAnalysisService from '../../services/videoAnalysisService';
import matchService from '../../services/matchService';

const route = useRoute();
const router = useRouter();
const matchId = computed(() => parseInt(route.params.matchId));

const videos = ref([]);
const matchInfo = ref(null);
const selectedVideo = ref(null);
const loading = ref(true);
const error = ref(null);
const showUploadForm = ref(false);
const editingVideo = ref(null);

// Get all players from the match
const matchPlayers = computed(() => {
  if (!matchInfo.value) return [];

  const players = [];

  // Add player1 if exists
  if (matchInfo.value.player1 && matchInfo.value.player1.id) {
    players.push({
      id: matchInfo.value.player1.id,
      name: matchInfo.value.player1Name || matchInfo.value.player1.name || 'Player 1',
      team: 'team1'
    });
  }

  // Add player2 if exists
  if (matchInfo.value.player2 && matchInfo.value.player2.id) {
    players.push({
      id: matchInfo.value.player2.id,
      name: matchInfo.value.player2Name || matchInfo.value.player2.name || 'Player 2',
      team: 'team1'
    });
  }

  // Add opponentPlayer1 if exists
  if (matchInfo.value.opponentPlayer1 && matchInfo.value.opponentPlayer1.id) {
    players.push({
      id: matchInfo.value.opponentPlayer1.id,
      name: matchInfo.value.opponentPlayer1Name || matchInfo.value.opponentPlayer1.name || 'Opponent 1',
      team: 'team2'
    });
  }

  // Add opponentPlayer2 if exists
  if (matchInfo.value.opponentPlayer2 && matchInfo.value.opponentPlayer2.id) {
    players.push({
      id: matchInfo.value.opponentPlayer2.id,
      name: matchInfo.value.opponentPlayer2Name || matchInfo.value.opponentPlayer2.name || 'Opponent 2',
      team: 'team2'
    });
  }

  return players;
});

// Compute completion status - completed when all players have been analyzed
const analysisCompletionStatus = computed(() => {
  if (!matchInfo.value || matchPlayers.value.length === 0) {
    return { completed: false, analyzed: 0, total: 0, percentage: 0 };
  }

  const totalPlayers = matchPlayers.value.length;
  const analyzedPlayerIds = new Set(
    videos.value
      .filter(v => v.aiAnalyzed || v.status === 'completed')
      .map(v => v.player?.id)
      .filter(id => id != null)
  );

  const analyzedCount = analyzedPlayerIds.size;
  const isCompleted = analyzedCount === totalPlayers && totalPlayers > 0;
  const percentage = totalPlayers > 0 ? Math.round((analyzedCount / totalPlayers) * 100) : 0;

  return {
    completed: isCompleted,
    analyzed: analyzedCount,
    total: totalPlayers,
    percentage: percentage
  };
});

// Group videos by match date
const videosByDate = computed(() => {
  const groups = {};

  videos.value.forEach(video => {
    const dateStr = video.matchDate || 'No Date';
    if (!groups[dateStr]) {
      groups[dateStr] = [];
    }
    groups[dateStr].push(video);
  });

  // Convert to array and sort by date (most recent first)
  return Object.entries(groups)
    .map(([date, videos]) => ({
      date,
      dateLabel: formatDateLabel(date),
      videos
    }))
    .sort((a, b) => {
      if (a.date === 'No Date') return 1;
      if (b.date === 'No Date') return -1;
      return new Date(b.date) - new Date(a.date);
    });
});

const formatDateLabel = (dateString) => {
  if (!dateString || dateString === 'No Date') return 'No Date';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const loadMatchInfo = async () => {
  try {
    matchInfo.value = await matchService.getMatchById(matchId.value);
  } catch (err) {
    console.error('Error loading match info:', err);
    error.value = 'Failed to load match information';
  }
};

const loadVideos = async () => {
  try {
    loading.value = true;
    error.value = null;

    // Load match info and videos in parallel
    await Promise.all([
      loadMatchInfo(),
      videoAnalysisService.getVideosByMatch(matchId.value).then(result => {
        videos.value = result;
      })
    ]);
  } catch (err) {
    console.error('Error loading videos:', err);
    error.value = 'Failed to load videos: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const formatMatchTitle = (match) => {
  if (!match) return '';
  const date = new Date(match.matchTime).toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
  const tournament = match.tournamentName || 'Match';
  const type = match.matchType === 'singles' ? 'Singles' : 'Doubles';
  return `${date} - ${tournament} (${type})`;
};

const selectVideo = (video) => {
  selectedVideo.value = video;
};

const editVideo = (video) => {
  editingVideo.value = video;
  showUploadForm.value = true;
};

const deleteVideo = async (videoId) => {
  if (!confirm('Are you sure you want to delete this video?')) {
    return;
  }

  try {
    await videoAnalysisService.deleteVideo(videoId);
    await loadVideos();
  } catch (err) {
    console.error('Error deleting video:', err);
    alert('Failed to delete video: ' + err.message);
  }
};

const handleSubmitVideo = async (videoData) => {
  try {
    if (editingVideo.value) {
      // When editing, update the existing video
      await videoAnalysisService.updateVideo(editingVideo.value.id, videoData);
    } else {
      // When creating new video(s), support multiple players
      const playerIds = videoData.playerIds || [];

      if (playerIds.length === 0) {
        alert('Please select at least one player for this video');
        return;
      }

      // Create a video analysis for each selected player
      for (const playerId of playerIds) {
        await videoAnalysisService.createVideo(playerId, videoData);
      }
    }

    showUploadForm.value = false;
    editingVideo.value = null;
    await loadVideos();
  } catch (err) {
    console.error('Error saving video:', err);
    alert('Failed to save video: ' + err.message);
  }
};

const cancelForm = () => {
  showUploadForm.value = false;
  editingVideo.value = null;
};

const goBack = () => {
  router.push('/matches');
};

onMounted(() => {
  loadVideos();
});
</script>
