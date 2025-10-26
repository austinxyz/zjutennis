<template>
  <div class="h-full flex flex-col">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Video Analysis</h1>
        <p class="text-sm text-muted-foreground mt-1">
          {{ playerName }} - {{ videos.length }} video{{ videos.length !== 1 ? 's' : '' }}
        </p>
      </div>
      <div class="flex gap-3">
        <Button @click="showUploadForm = true">
          <Plus class="mr-2 h-4 w-4" />
          Add Video
        </Button>
      </div>
    </div>

    <!-- Upload/Edit Form -->
    <VideoUploadForm
      v-if="showUploadForm"
      :video="editingVideo"
      :is-edit="!!editingVideo"
      @submit="handleSubmitVideo"
      @cancel="cancelForm"
      class="mb-6"
    />

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
        <p class="text-muted-foreground mb-4">
          Start by adding a match video or training session
        </p>
        <Button @click="showUploadForm = true">
          <Plus class="mr-2 h-4 w-4" />
          Add First Video
        </Button>
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
import { Plus, Video } from 'lucide-vue-next';
import Button from '../components/ui/Button.vue';
import VideoCard from '../components/VideoCard.vue';
import VideoUploadForm from '../components/VideoUploadForm.vue';
import VideoDetailModal from '../components/VideoDetailModal.vue';
import videoAnalysisService from '../services/videoAnalysisService';
import playerService from '../services/playerService';

const route = useRoute();
const playerId = computed(() => parseInt(route.params.playerId));

const videos = ref([]);
const selectedVideo = ref(null);
const loading = ref(true);
const error = ref(null);
const showUploadForm = ref(false);
const editingVideo = ref(null);
const playerName = ref('');

const loadVideos = async () => {
  try {
    loading.value = true;
    error.value = null;
    videos.value = await videoAnalysisService.getPlayerVideos(playerId.value);

    // Load player name
    const player = await playerService.getPlayerById(playerId.value);
    playerName.value = player.name;
  } catch (err) {
    error.value = 'Failed to load videos: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const handleSubmitVideo = async (videoData) => {
  try {
    if (editingVideo.value) {
      // Update existing video
      await videoAnalysisService.updateVideo(editingVideo.value.id, videoData);
    } else {
      // Create new video
      await videoAnalysisService.createVideo(playerId.value, videoData);
    }

    showUploadForm.value = false;
    editingVideo.value = null;
    await loadVideos();
  } catch (err) {
    alert('Failed to save video: ' + err.message);
  }
};

const cancelForm = () => {
  showUploadForm.value = false;
  editingVideo.value = null;
};

const selectVideo = (video) => {
  selectedVideo.value = video;
};

const editVideo = (video) => {
  editingVideo.value = video;
  showUploadForm.value = true;
};

const deleteVideo = async (video) => {
  if (!confirm(`Are you sure you want to delete "${video.title}"?`)) {
    return;
  }

  try {
    await videoAnalysisService.deleteVideo(video.id);
    await loadVideos();
  } catch (err) {
    alert('Failed to delete video: ' + err.message);
  }
};

onMounted(() => {
  loadVideos();
});
</script>
