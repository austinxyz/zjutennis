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
        <h1 class="text-2xl font-bold text-foreground">Match Video Management</h1>
        <p class="text-sm text-muted-foreground mt-1" v-if="matchInfo">
          {{ formatMatchTitle(matchInfo) }}
        </p>
        <div class="flex items-center gap-3 mt-2" v-if="video">
          <Badge variant="success">Video Added</Badge>
          <Badge :variant="analysisCompletionStatus.completed ? 'success' : 'secondary'">
            Analysis: {{ analysisCompletionStatus.analyzed }}/{{ analysisCompletionStatus.total }} players ({{ analysisCompletionStatus.percentage }}%)
          </Badge>
        </div>
      </div>
      <Button @click="showUploadForm = true" v-if="!video && !showUploadForm">
        <Plus class="w-4 h-4 mr-2" />
        Add Video
      </Button>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading match information...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <template v-else>
      <!-- Video Upload Form -->
      <VideoUploadForm
        v-if="showUploadForm"
        :video="video"
        :is-edit="!!video"
        :match-id="matchId"
        :available-players="matchPlayers"
        @submit="handleSubmitVideo"
        @cancel="cancelForm"
      />

      <!-- Video Display (when video exists) -->
      <div v-else-if="video" class="space-y-6">
        <VideoCard
          :video="video"
          @select="selectVideo"
          @edit="editVideo"
          @delete="confirmDeleteVideo"
        />

        <!-- Video Analyses Section -->
        <Card>
          <CardContent class="pt-6">
            <div class="flex justify-between items-center mb-4">
              <h3 class="text-lg font-semibold">Player Analyses ({{ analyses.length }})</h3>
              <Button @click="showAnalysisForm = true" size="sm" v-if="!showAnalysisForm && canAddMoreAnalyses">
                <Plus class="w-4 h-4 mr-2" />
                Add Analysis
              </Button>
            </div>

            <!-- Analysis Form -->
            <div v-if="showAnalysisForm" class="mb-6">
              <AnalysisForm
                :video-id="video.id"
                :available-players="unanalyzedPlayers"
                @submit="handleSubmitAnalysis"
                @cancel="showAnalysisForm = false"
              />
            </div>

            <!-- Analyses List -->
            <div v-if="analyses.length > 0" class="space-y-4">
              <AnalysisCard
                v-for="analysis in analyses"
                :key="analysis.id"
                :analysis="analysis"
                @edit="editAnalysis"
                @delete="deleteAnalysis"
              />
            </div>

            <!-- Empty Analyses State -->
            <div v-else class="text-center py-8">
              <p class="text-muted-foreground mb-4">No player analyses yet</p>
              <Button @click="showAnalysisForm = true" size="sm">
                <Plus class="w-4 h-4 mr-2" />
                Add First Analysis
              </Button>
            </div>
          </CardContent>
        </Card>
      </div>

      <!-- Empty State (no video) -->
      <Card v-else>
        <CardContent class="pt-6">
          <div class="text-center py-12">
            <Video class="w-16 h-16 mx-auto text-muted-foreground mb-4 opacity-50" />
            <h3 class="text-lg font-semibold mb-2">No Video Added</h3>
            <p class="text-muted-foreground mb-6">
              Add a video for this match to enable player performance analysis
            </p>
            <Button @click="showUploadForm = true">
              <Plus class="w-4 h-4 mr-2" />
              Add Video
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
      @updated="loadVideo"
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
import AnalysisForm from '../../components/AnalysisForm.vue';
import AnalysisCard from '../../components/AnalysisCard.vue';
import videoService from '../../services/videoService';
import matchService from '../../services/matchService';
import videoAnalysisService from '../../services/videoAnalysisService';

const route = useRoute();
const router = useRouter();
const matchId = computed(() => parseInt(route.params.matchId));

const video = ref(null);
const analyses = ref([]);
const matchInfo = ref(null);
const selectedVideo = ref(null);
const loading = ref(true);
const error = ref(null);
const showUploadForm = ref(false);
const showAnalysisForm = ref(false);

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

// Get players who don't have analysis yet
const unanalyzedPlayers = computed(() => {
  const analyzedPlayerIds = new Set(analyses.value.map(a => a.playerId));
  return matchPlayers.value.filter(p => !analyzedPlayerIds.has(p.id));
});

// Can add more analyses if there are unanalyzed players
const canAddMoreAnalyses = computed(() => {
  return unanalyzedPlayers.value.length > 0;
});

// Compute completion status - completed when all players have been analyzed
const analysisCompletionStatus = computed(() => {
  if (!matchInfo.value || matchPlayers.value.length === 0) {
    return { completed: false, analyzed: 0, total: 0, percentage: 0 };
  }

  const totalPlayers = matchPlayers.value.length;
  const analyzedCount = analyses.value.length;
  const isCompleted = analyzedCount === totalPlayers && totalPlayers > 0;
  const percentage = totalPlayers > 0 ? Math.round((analyzedCount / totalPlayers) * 100) : 0;

  return {
    completed: isCompleted,
    analyzed: analyzedCount,
    total: totalPlayers,
    percentage: percentage
  };
});

const loadMatchInfo = async () => {
  try {
    matchInfo.value = await matchService.getMatchById(matchId.value);
  } catch (err) {
    console.error('Error loading match info:', err);
    error.value = 'Failed to load match information';
  }
};

const loadVideo = async () => {
  try {
    video.value = await videoService.getVideoByMatch(matchId.value);

    // If video exists, load analyses
    if (video.value) {
      analyses.value = await videoAnalysisService.getAnalysesByVideo(video.value.id);
    } else {
      analyses.value = [];
    }
  } catch (err) {
    console.error('Error loading video:', err);
    // Don't set error here - no video is a valid state
    video.value = null;
    analyses.value = [];
  }
};

const loadData = async () => {
  try {
    loading.value = true;
    error.value = null;

    await loadMatchInfo();
    await loadVideo();
  } catch (err) {
    console.error('Error loading data:', err);
    error.value = 'Failed to load data: ' + err.message;
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

const selectVideo = (videoData) => {
  selectedVideo.value = videoData;
};

const editVideo = () => {
  showUploadForm.value = true;
};

const confirmDeleteVideo = async () => {
  if (!confirm('Are you sure you want to delete this video? All player analyses will also be deleted.')) {
    return;
  }

  try {
    await matchService.removeVideo(matchId.value);
    video.value = null;
    analyses.value = [];
  } catch (err) {
    console.error('Error deleting video:', err);
    alert('Failed to delete video: ' + err.message);
  }
};

const handleSubmitVideo = async (videoData) => {
  try {
    // Build video payload with match ID
    const videoPayload = {
      description: videoData.description,
      videoUrl: videoData.videoUrl,
      thumbnailUrl: videoData.thumbnailUrl,
      durationSeconds: videoData.durationSeconds,
      totalShots: videoData.totalShots,
      errors: videoData.errors,
      winners: videoData.winners,
      aces: videoData.aces,
      doubleFaults: videoData.doubleFaults,
      runningDistanceMeters: videoData.runningDistanceMeters,
      matchId: matchId.value
    };

    if (video.value) {
      // Update existing video
      await videoService.updateVideo(video.value.id, videoPayload);
    } else {
      // Create new video via match service
      await matchService.attachVideo(matchId.value, videoPayload);
    }

    showUploadForm.value = false;
    await loadVideo();
  } catch (err) {
    console.error('Error saving video:', err);
    alert('Failed to save video: ' + err.message);
  }
};

const handleSubmitAnalysis = async (analysisData) => {
  try {
    await videoAnalysisService.createAnalysis({
      videoId: video.value.id,
      playerId: analysisData.playerId,
      // Optional analysis fields
      strengthForehandScore: analysisData.strengthForehandScore,
      strengthServeScore: analysisData.strengthServeScore,
      strengthVolleyScore: analysisData.strengthVolleyScore,
      strengthMovementScore: analysisData.strengthMovementScore,
      strengthSummary: analysisData.strengthSummary,
      weaknessBackhandScore: analysisData.weaknessBackhandScore,
      weaknessConsistencyScore: analysisData.weaknessConsistencyScore,
      weaknessPressureScore: analysisData.weaknessPressureScore,
      weaknessSummary: analysisData.weaknessSummary,
      tacticalStyle: analysisData.tacticalStyle,
      aggressionIndex: analysisData.aggressionIndex,
      netApproachFrequency: analysisData.netApproachFrequency,
      baselineRallyPreference: analysisData.baselineRallyPreference,
      tacticalSummary: analysisData.tacticalSummary,
      aiRecommendations: analysisData.aiRecommendations,
      trainingFocusAreas: analysisData.trainingFocusAreas,
      status: analysisData.status || 'pending'
    });

    showAnalysisForm.value = false;
    await loadVideo(); // Reload to get updated analyses
  } catch (err) {
    console.error('Error creating analysis:', err);
    alert('Failed to create analysis: ' + err.message);
  }
};

const editAnalysis = (analysis) => {
  // TODO: Implement edit analysis
  console.log('Edit analysis:', analysis);
};

const deleteAnalysis = async (analysisId) => {
  if (!confirm('Are you sure you want to delete this analysis?')) {
    return;
  }

  try {
    await videoAnalysisService.deleteAnalysis(analysisId);
    await loadVideo(); // Reload to get updated analyses
  } catch (err) {
    console.error('Error deleting analysis:', err);
    alert('Failed to delete analysis: ' + err.message);
  }
};

const cancelForm = () => {
  showUploadForm.value = false;
};

const goBack = () => {
  router.push('/matches');
};

onMounted(() => {
  loadData();
});
</script>
