<template>
  <div class="h-full flex flex-col">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Video Analysis</h1>
        <p class="text-sm text-muted-foreground mt-1">
          {{ playerName }} - {{ videos.length }} video{{ videos.length !== 1 ? 's' : '' }}
        </p>
        <p class="text-xs text-muted-foreground mt-1">
          Videos from matches you participated in
        </p>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="text-center py-8">
      <p class="text-muted-foreground">Loading videos...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="text-center py-8">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <!-- Video Grid -->
    <div v-else-if="videos.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <VideoCard
        v-for="video in videos"
        :key="video.id"
        :video="video"
        @select="selectVideo"
        @edit="editVideo"
        @delete="deleteVideo"
      />
    </div>

    <!-- Empty State -->
    <div v-else class="flex-1 flex items-center justify-center">
      <div class="text-center">
        <Video class="h-16 w-16 mx-auto mb-4 text-muted-foreground opacity-50" />
        <h3 class="text-lg font-semibold mb-2">No Videos Yet</h3>
        <p class="text-muted-foreground">
          Videos will appear here when they are added to matches you participated in
        </p>
      </div>
    </div>

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
import { useRoute } from 'vue-router';
import { Video } from 'lucide-vue-next';
import VideoCard from '../components/VideoCard.vue';
import VideoDetailModal from '../components/VideoDetailModal.vue';
import videoAnalysisService from '../services/videoAnalysisService';
import playerService from '../services/playerService';

const route = useRoute();
const playerId = computed(() => parseInt(route.params.playerId));

const videos = ref([]);
const selectedVideo = ref(null);
const loading = ref(true);
const error = ref(null);
const playerName = ref('');

const loadVideos = async () => {
  try {
    loading.value = true;
    error.value = null;
    // Get all videos accessible to this player (own videos + match videos)
    videos.value = await videoAnalysisService.getVideosAccessibleToPlayer(playerId.value);

    // Load player name
    const player = await playerService.getPlayerById(playerId.value);
    playerName.value = player.name;
  } catch (err) {
    error.value = 'Failed to load videos: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const selectVideo = (video) => {
  selectedVideo.value = video;
};

onMounted(() => {
  loadVideos();
});
</script>
