<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4" @click.self="$emit('close')">
    <Card class="w-full max-w-4xl max-h-[90vh] overflow-y-auto">
      <!-- Header -->
      <div class="p-6 border-b flex justify-between items-start sticky top-0 bg-white z-10">
        <div>
          <h2 class="text-2xl font-bold">{{ video.title }}</h2>
          <p class="text-sm text-muted-foreground mt-1">
            {{ formatDate(video.matchDate) }}
          </p>
        </div>
        <Button variant="ghost" size="sm" @click="$emit('close')">
          <X class="h-5 w-5" />
        </Button>
      </div>

      <div class="p-6">
        <!-- Player Selector (if multiple analyses exist) -->
        <div v-if="allMatchAnalyses.length > 1" class="mb-6">
          <h3 class="text-sm font-semibold mb-3">Player Analyses</h3>
          <div class="flex flex-wrap gap-2">
            <Button
              v-for="analysis in allMatchAnalyses"
              :key="analysis.id"
              size="sm"
              :variant="selectedPlayerAnalysis?.id === analysis.id ? 'default' : 'outline'"
              @click="selectedPlayerAnalysis = analysis"
            >
              {{ analysis.player?.name || 'Unknown Player' }}
              <Badge v-if="analysis.aiAnalyzed" variant="success" class="ml-2 text-xs">Analyzed</Badge>
            </Button>
          </div>
        </div>

        <!-- Video Embed (if available) -->
        <div v-if="video.videoUrl" class="mb-6">
          <div class="aspect-video bg-gray-200 rounded-lg overflow-hidden">
            <iframe
              v-if="getEmbedUrl(video.videoUrl)"
              :src="getEmbedUrl(video.videoUrl)"
              class="w-full h-full"
              frameborder="0"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowfullscreen
            ></iframe>
            <div v-else class="w-full h-full flex items-center justify-center">
              <Button @click="openVideo">
                <ExternalLink class="mr-2 h-4 w-4" />
                Open Video
              </Button>
            </div>
          </div>
        </div>

        <!-- Currently Selected Player -->
        <div v-if="activeVideo.player" class="mb-4">
          <Badge variant="outline" class="text-sm">
            Viewing analysis for: {{ activeVideo.player.name || 'Unknown Player' }}
          </Badge>
        </div>

        <!-- Match Statistics -->
        <div v-if="hasMatchStats" class="mb-6">
          <h3 class="font-semibold text-lg mb-3">Match Statistics</h3>
          <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
            <StatCard v-if="activeVideo.totalShots" label="Total Shots" :value="activeVideo.totalShots" color="blue" />
            <StatCard v-if="activeVideo.winners" label="Winners" :value="activeVideo.winners" color="green" />
            <StatCard v-if="activeVideo.errors" label="Errors" :value="activeVideo.errors" color="red" />
            <StatCard v-if="activeVideo.aces" label="Aces" :value="activeVideo.aces" color="purple" />
            <StatCard v-if="activeVideo.doubleFaults" label="Double Faults" :value="activeVideo.doubleFaults" color="orange" />
            <StatCard v-if="activeVideo.runningDistanceMeters" label="Distance" :value="`${activeVideo.runningDistanceMeters}m`" color="indigo" />
          </div>
        </div>

        <!-- AI Analysis Results -->
        <div v-if="activeVideo.aiAnalyzed" class="space-y-6">
          <!-- Strengths -->
          <div v-if="hasStrengths">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <TrendingUp class="w-5 h-5 mr-2 text-green-600" />
              Strengths
            </h3>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-3">
              <ScoreBar v-if="activeVideo.strengthForehandScore" label="Forehand" :score="activeVideo.strengthForehandScore" max="5" color="green" />
              <ScoreBar v-if="activeVideo.strengthServeScore" label="Serve" :score="activeVideo.strengthServeScore" max="5" color="green" />
              <ScoreBar v-if="activeVideo.strengthVolleyScore" label="Volley" :score="activeVideo.strengthVolleyScore" max="5" color="green" />
              <ScoreBar v-if="activeVideo.strengthMovementScore" label="Movement" :score="activeVideo.strengthMovementScore" max="5" color="green" />
            </div>
            <Card v-if="activeVideo.strengthSummary" class="p-4 bg-green-50">
              <p class="text-sm">{{ activeVideo.strengthSummary }}</p>
            </Card>
          </div>

          <!-- Weaknesses -->
          <div v-if="hasWeaknesses">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <TrendingDown class="w-5 h-5 mr-2 text-red-600" />
              Areas for Improvement
            </h3>
            <div class="grid grid-cols-2 md:grid-cols-3 gap-4 mb-3">
              <ScoreBar v-if="activeVideo.weaknessBackhandScore" label="Backhand Errors" :score="activeVideo.weaknessBackhandScore" max="5" color="red" />
              <ScoreBar v-if="activeVideo.weaknessConsistencyScore" label="Consistency" :score="activeVideo.weaknessConsistencyScore" max="5" color="red" />
              <ScoreBar v-if="activeVideo.weaknessPressureScore" label="Under Pressure" :score="activeVideo.weaknessPressureScore" max="5" color="red" />
            </div>
            <Card v-if="activeVideo.weaknessSummary" class="p-4 bg-red-50">
              <p class="text-sm">{{ activeVideo.weaknessSummary }}</p>
            </Card>
          </div>

          <!-- Tactical Analysis -->
          <div v-if="hasTacticalAnalysis">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <Target class="w-5 h-5 mr-2 text-blue-600" />
              Tactical Analysis
            </h3>
            <div class="grid grid-cols-2 md:grid-cols-3 gap-4 mb-3">
              <div v-if="activeVideo.tacticalStyle" class="text-center p-3 bg-blue-50 rounded">
                <div class="text-sm text-muted-foreground">Style</div>
                <div class="font-semibold text-blue-600 capitalize">{{ activeVideo.tacticalStyle }}</div>
              </div>
              <div v-if="activeVideo.aggressionIndex !== null" class="text-center p-3 bg-blue-50 rounded">
                <div class="text-sm text-muted-foreground">Aggression</div>
                <div class="font-semibold text-blue-600">{{ activeVideo.aggressionIndex.toFixed(0) }}%</div>
              </div>
              <div v-if="activeVideo.netApproachFrequency !== null" class="text-center p-3 bg-blue-50 rounded">
                <div class="text-sm text-muted-foreground">Net Approach</div>
                <div class="font-semibold text-blue-600">{{ activeVideo.netApproachFrequency.toFixed(0) }}%</div>
              </div>
            </div>
            <Card v-if="activeVideo.tacticalSummary" class="p-4 bg-blue-50">
              <p class="text-sm">{{ activeVideo.tacticalSummary }}</p>
            </Card>
          </div>

          <!-- AI Recommendations -->
          <div v-if="activeVideo.aiRecommendations || activeVideo.trainingFocusAreas">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <Lightbulb class="w-5 h-5 mr-2 text-yellow-600" />
              AI Recommendations
            </h3>
            <Card class="p-4 bg-yellow-50">
              <div v-if="activeVideo.trainingFocusAreas" class="mb-3">
                <h4 class="font-medium text-sm mb-2">Training Focus Areas:</h4>
                <p class="text-sm">{{ activeVideo.trainingFocusAreas }}</p>
              </div>
              <div v-if="activeVideo.aiRecommendations">
                <h4 class="font-medium text-sm mb-2">Recommendations:</h4>
                <p class="text-sm whitespace-pre-wrap">{{ activeVideo.aiRecommendations }}</p>
              </div>
            </Card>
          </div>
        </div>

        <!-- No AI Analysis Yet -->
        <div v-if="!showManualInput" class="text-center py-8">
          <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gray-100 mb-4">
            <Sparkles class="w-8 h-8 text-gray-400" />
          </div>
          <h3 class="font-semibold mb-2">AI Analysis Not Available</h3>
          <p class="text-sm text-muted-foreground mb-4">
            This video hasn't been analyzed yet
          </p>
          <div class="flex gap-3 justify-center items-center">
            <Badge :variant="getStatusVariant(video.status)">
              {{ video.status }}
            </Badge>
            <Button
              v-if="video.status !== 'processing'"
              @click="triggerAIAnalysis"
              :disabled="analyzing"
              size="sm"
            >
              <Sparkles class="w-4 h-4 mr-2" />
              {{ analyzing ? 'Analyzing...' : 'Auto-Generate Analysis' }}
            </Button>
            <Button
              v-if="video.status !== 'processing'"
              @click="showManualInput = true"
              size="sm"
              variant="outline"
            >
              <Edit class="w-4 h-4 mr-2" />
              Manual Input
            </Button>
          </div>
        </div>

        <!-- Manual Analysis Input Form -->
        <div v-else>
          <VideoAnalysisInputForm
            :video="video"
            :initial-data="video"
            :available-players="availablePlayers"
            @submit="handleManualAnalysis"
            @cancel="showManualInput = false"
          />
        </div>
      </div>
    </Card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { X, ExternalLink, TrendingUp, TrendingDown, Target, Lightbulb, Sparkles, Edit } from 'lucide-vue-next';
import Card from './ui/Card.vue';
import Button from './ui/Button.vue';
import Badge from './ui/Badge.vue';
import VideoAnalysisInputForm from './VideoAnalysisInputForm.vue';
import videoAnalysisService from '../services/videoAnalysisService';
import playerService from '../services/playerService';

const props = defineProps({
  video: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['close', 'updated']);

const analyzing = ref(false);
const showManualInput = ref(false);
const availablePlayers = ref([]);
const allMatchAnalyses = ref([]);
const selectedPlayerAnalysis = ref(null);

// Use selectedPlayerAnalysis as the active video for display
const activeVideo = computed(() => selectedPlayerAnalysis.value || props.video);

// Load all players for selection
const loadPlayers = async () => {
  try {
    const result = await playerService.searchPlayers({});
    availablePlayers.value = result.players || [];
  } catch (error) {
    console.error('Error loading players:', error);
  }
};

// Load all analyses for this match
const loadMatchAnalyses = async () => {
  if (props.video.match?.id) {
    try {
      allMatchAnalyses.value = await videoAnalysisService.getVideosByMatch(props.video.match.id);
      // Set the current video as the selected analysis
      selectedPlayerAnalysis.value = props.video;
    } catch (error) {
      console.error('Error loading match analyses:', error);
    }
  } else {
    // If no match, just show this video's analysis
    allMatchAnalyses.value = [props.video];
    selectedPlayerAnalysis.value = props.video;
  }
};

onMounted(() => {
  loadPlayers();
  loadMatchAnalyses();
});

const triggerAIAnalysis = async () => {
  try {
    analyzing.value = true;
    await videoAnalysisService.analyzeVideo(props.video.id);
    emit('updated'); // Refresh the video data
  } catch (err) {
    alert('Failed to analyze video: ' + err.message);
  } finally {
    analyzing.value = false;
  }
};

const handleManualAnalysis = async (analysisData) => {
  try {
    await videoAnalysisService.updateAIAnalysis(props.video.id, analysisData);
    showManualInput.value = false;
    emit('updated'); // Refresh the video data
  } catch (err) {
    alert('Failed to save analysis: ' + err.message);
  }
};

const hasMatchStats = computed(() => {
  const v = activeVideo.value;
  return v.totalShots || v.winners || v.errors ||
         v.aces || v.doubleFaults || v.runningDistanceMeters;
});

const hasStrengths = computed(() => {
  const v = activeVideo.value;
  return v.strengthForehandScore || v.strengthServeScore ||
         v.strengthVolleyScore || v.strengthMovementScore ||
         v.strengthSummary;
});

const hasWeaknesses = computed(() => {
  const v = activeVideo.value;
  return v.weaknessBackhandScore || v.weaknessConsistencyScore ||
         v.weaknessPressureScore || v.weaknessSummary;
});

const hasTacticalAnalysis = computed(() => {
  const v = activeVideo.value;
  return v.tacticalStyle || v.aggressionIndex !== null ||
         v.netApproachFrequency !== null || v.tacticalSummary;
});

const formatDate = (dateString) => {
  if (!dateString) return 'No date';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const getEmbedUrl = (url) => {
  if (!url) return null;

  // YouTube
  const youtubeMatch = url.match(/(?:youtube\.com\/watch\?v=|youtu\.be\/)([^&\s]+)/);
  if (youtubeMatch) {
    return `https://www.youtube.com/embed/${youtubeMatch[1]}`;
  }

  // Vimeo
  const vimeoMatch = url.match(/vimeo\.com\/(\d+)/);
  if (vimeoMatch) {
    return `https://player.vimeo.com/video/${vimeoMatch[1]}`;
  }

  return null;
};

const openVideo = () => {
  if (props.video.videoUrl) {
    window.open(props.video.videoUrl, '_blank');
  }
};

const getStatusVariant = (status) => {
  const variants = {
    'pending': 'secondary',
    'processing': 'warning',
    'completed': 'success',
    'failed': 'destructive'
  };
  return variants[status] || 'default';
};

// Simple stat card component
const StatCard = {
  props: ['label', 'value', 'color'],
  template: `
    <div class="text-center p-3 rounded" :class="'bg-' + color + '-50'">
      <div class="text-sm text-muted-foreground">{{ label }}</div>
      <div class="text-2xl font-bold" :class="'text-' + color + '-600'">{{ value }}</div>
    </div>
  `
};

// Simple score bar component
const ScoreBar = {
  props: ['label', 'score', 'max', 'color'],
  template: `
    <div>
      <div class="flex justify-between text-sm mb-1">
        <span class="text-muted-foreground">{{ label }}</span>
        <span class="font-medium">{{ score.toFixed(1) }}/{{ max }}</span>
      </div>
      <div class="h-2 bg-gray-200 rounded-full overflow-hidden">
        <div
          class="h-full transition-all"
          :class="'bg-' + color + '-500'"
          :style="{ width: (score / max * 100) + '%' }"
        ></div>
      </div>
    </div>
  `
};
</script>
