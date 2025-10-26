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

        <!-- Match Statistics -->
        <div v-if="hasMatchStats" class="mb-6">
          <h3 class="font-semibold text-lg mb-3">Match Statistics</h3>
          <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
            <StatCard v-if="video.totalShots" label="Total Shots" :value="video.totalShots" color="blue" />
            <StatCard v-if="video.winners" label="Winners" :value="video.winners" color="green" />
            <StatCard v-if="video.errors" label="Errors" :value="video.errors" color="red" />
            <StatCard v-if="video.aces" label="Aces" :value="video.aces" color="purple" />
            <StatCard v-if="video.doubleFaults" label="Double Faults" :value="video.doubleFaults" color="orange" />
            <StatCard v-if="video.runningDistanceMeters" label="Distance" :value="`${video.runningDistanceMeters}m`" color="indigo" />
          </div>
        </div>

        <!-- AI Analysis Results -->
        <div v-if="video.aiAnalyzed" class="space-y-6">
          <!-- Strengths -->
          <div v-if="hasStrengths">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <TrendingUp class="w-5 h-5 mr-2 text-green-600" />
              Strengths
            </h3>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4 mb-3">
              <ScoreBar v-if="video.strengthForehandScore" label="Forehand" :score="video.strengthForehandScore" max="10" color="green" />
              <ScoreBar v-if="video.strengthServeScore" label="Serve" :score="video.strengthServeScore" max="10" color="green" />
              <ScoreBar v-if="video.strengthVolleyScore" label="Volley" :score="video.strengthVolleyScore" max="10" color="green" />
              <ScoreBar v-if="video.strengthMovementScore" label="Movement" :score="video.strengthMovementScore" max="10" color="green" />
            </div>
            <Card v-if="video.strengthSummary" class="p-4 bg-green-50">
              <p class="text-sm">{{ video.strengthSummary }}</p>
            </Card>
          </div>

          <!-- Weaknesses -->
          <div v-if="hasWeaknesses">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <TrendingDown class="w-5 h-5 mr-2 text-red-600" />
              Areas for Improvement
            </h3>
            <div class="grid grid-cols-2 md:grid-cols-3 gap-4 mb-3">
              <ScoreBar v-if="video.weaknessBackhandScore" label="Backhand Errors" :score="video.weaknessBackhandScore" max="10" color="red" />
              <ScoreBar v-if="video.weaknessConsistencyScore" label="Consistency" :score="video.weaknessConsistencyScore" max="10" color="red" />
              <ScoreBar v-if="video.weaknessPressureScore" label="Under Pressure" :score="video.weaknessPressureScore" max="10" color="red" />
            </div>
            <Card v-if="video.weaknessSummary" class="p-4 bg-red-50">
              <p class="text-sm">{{ video.weaknessSummary }}</p>
            </Card>
          </div>

          <!-- Tactical Analysis -->
          <div v-if="hasTacticalAnalysis">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <Target class="w-5 h-5 mr-2 text-blue-600" />
              Tactical Analysis
            </h3>
            <div class="grid grid-cols-2 md:grid-cols-3 gap-4 mb-3">
              <div v-if="video.tacticalStyle" class="text-center p-3 bg-blue-50 rounded">
                <div class="text-sm text-muted-foreground">Style</div>
                <div class="font-semibold text-blue-600 capitalize">{{ video.tacticalStyle }}</div>
              </div>
              <div v-if="video.aggressionIndex !== null" class="text-center p-3 bg-blue-50 rounded">
                <div class="text-sm text-muted-foreground">Aggression</div>
                <div class="font-semibold text-blue-600">{{ video.aggressionIndex.toFixed(0) }}%</div>
              </div>
              <div v-if="video.netApproachFrequency !== null" class="text-center p-3 bg-blue-50 rounded">
                <div class="text-sm text-muted-foreground">Net Approach</div>
                <div class="font-semibold text-blue-600">{{ video.netApproachFrequency.toFixed(0) }}%</div>
              </div>
            </div>
            <Card v-if="video.tacticalSummary" class="p-4 bg-blue-50">
              <p class="text-sm">{{ video.tacticalSummary }}</p>
            </Card>
          </div>

          <!-- AI Recommendations -->
          <div v-if="video.aiRecommendations || video.trainingFocusAreas">
            <h3 class="font-semibold text-lg mb-3 flex items-center">
              <Lightbulb class="w-5 h-5 mr-2 text-yellow-600" />
              AI Recommendations
            </h3>
            <Card class="p-4 bg-yellow-50">
              <div v-if="video.trainingFocusAreas" class="mb-3">
                <h4 class="font-medium text-sm mb-2">Training Focus Areas:</h4>
                <p class="text-sm">{{ video.trainingFocusAreas }}</p>
              </div>
              <div v-if="video.aiRecommendations">
                <h4 class="font-medium text-sm mb-2">Recommendations:</h4>
                <p class="text-sm whitespace-pre-wrap">{{ video.aiRecommendations }}</p>
              </div>
            </Card>
          </div>
        </div>

        <!-- No AI Analysis Yet -->
        <div v-else class="text-center py-8">
          <div class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gray-100 mb-4">
            <Sparkles class="w-8 h-8 text-gray-400" />
          </div>
          <h3 class="font-semibold mb-2">AI Analysis Not Available</h3>
          <p class="text-sm text-muted-foreground mb-4">
            This video hasn't been analyzed yet
          </p>
          <Badge :variant="getStatusVariant(video.status)">
            {{ video.status }}
          </Badge>
        </div>
      </div>
    </Card>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { X, ExternalLink, TrendingUp, TrendingDown, Target, Lightbulb, Sparkles } from 'lucide-vue-next';
import Card from './ui/Card.vue';
import Button from './ui/Button.vue';
import Badge from './ui/Badge.vue';

const props = defineProps({
  video: {
    type: Object,
    required: true
  }
});

defineEmits(['close', 'updated']);

const hasMatchStats = computed(() => {
  return props.video.totalShots || props.video.winners || props.video.errors ||
         props.video.aces || props.video.doubleFaults || props.video.runningDistanceMeters;
});

const hasStrengths = computed(() => {
  return props.video.strengthForehandScore || props.video.strengthServeScore ||
         props.video.strengthVolleyScore || props.video.strengthMovementScore ||
         props.video.strengthSummary;
});

const hasWeaknesses = computed(() => {
  return props.video.weaknessBackhandScore || props.video.weaknessConsistencyScore ||
         props.video.weaknessPressureScore || props.video.weaknessSummary;
});

const hasTacticalAnalysis = computed(() => {
  return props.video.tacticalStyle || props.video.aggressionIndex !== null ||
         props.video.netApproachFrequency !== null || props.video.tacticalSummary;
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
