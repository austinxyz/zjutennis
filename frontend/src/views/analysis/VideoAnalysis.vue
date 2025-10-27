<template>
  <div class="h-full flex flex-col">
    <!-- Header -->
    <div class="flex justify-between items-center mb-6">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Video Analysis</h1>
        <p class="text-sm text-muted-foreground mt-1">
          AI-powered match video analysis across all players
        </p>
      </div>
      <div class="flex gap-3 items-center">
        <!-- Filter by Player -->
        <select
          v-model="selectedPlayerId"
          class="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
        >
          <option value="">All Players</option>
          <option v-for="player in uniquePlayers" :key="player.id" :value="player.id">
            {{ player.name }}
          </option>
        </select>

        <!-- Filter by Status -->
        <select
          v-model="selectedStatus"
          class="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
        >
          <option value="">All Statuses</option>
          <option value="pending">Pending</option>
          <option value="processing">Processing</option>
          <option value="completed">Completed</option>
          <option value="failed">Failed</option>
        </select>

        <!-- Filter by AI Analyzed -->
        <select
          v-model="aiAnalyzedFilter"
          class="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm"
        >
          <option value="">All Videos</option>
          <option value="analyzed">AI Analyzed Only</option>
          <option value="not-analyzed">Not Analyzed</option>
        </select>
      </div>
    </div>

    <!-- Statistics Summary -->
    <div class="grid grid-cols-4 gap-4 mb-6">
      <Card class="p-4">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-muted-foreground">Total Videos</p>
            <p class="text-2xl font-bold">{{ filteredVideos.length }}</p>
          </div>
          <Video class="h-8 w-8 text-blue-500" />
        </div>
      </Card>
      <Card class="p-4">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-muted-foreground">AI Analyzed</p>
            <p class="text-2xl font-bold text-green-600">{{ analyzedCount }}</p>
          </div>
          <Sparkles class="h-8 w-8 text-green-500" />
        </div>
      </Card>
      <Card class="p-4">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-muted-foreground">Pending Analysis</p>
            <p class="text-2xl font-bold text-orange-600">{{ pendingCount }}</p>
          </div>
          <Clock class="h-8 w-8 text-orange-500" />
        </div>
      </Card>
      <Card class="p-4">
        <div class="flex items-center justify-between">
          <div>
            <p class="text-sm text-muted-foreground">Players</p>
            <p class="text-2xl font-bold">{{ uniquePlayers.length }}</p>
          </div>
          <Users class="h-8 w-8 text-purple-500" />
        </div>
      </Card>
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
    <div v-else-if="filteredVideos.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <VideoCard
        v-for="video in filteredVideos"
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
        <h3 class="text-lg font-semibold mb-2">No Videos Found</h3>
        <p class="text-muted-foreground mb-4">
          {{ selectedPlayerId ? 'This player has no videos yet' : 'No videos have been uploaded yet' }}
        </p>
        <Button v-if="selectedPlayerId" @click="goToPlayerVideos(selectedPlayerId)">
          <Plus class="mr-2 h-4 w-4" />
          Add Video for Player
        </Button>
      </div>
    </div>

    <!-- Video Detail Modal -->
    <VideoDetailModal
      v-if="selectedVideo"
      :video="selectedVideo"
      @close="selectedVideo = null"
      @updated="loadVideos"
    />

    <!-- Video Edit Modal -->
    <div v-if="editingVideo" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4" @click.self="closeEditModal">
      <Card class="w-full max-w-4xl max-h-[90vh] overflow-y-auto">
        <div class="p-6 border-b flex justify-between items-center sticky top-0 bg-white z-10">
          <h2 class="text-2xl font-bold">Edit Video</h2>
          <Button variant="ghost" size="sm" @click="closeEditModal">
            <X class="h-5 w-5" />
          </Button>
        </div>
        <VideoUploadForm
          :video="editingVideo"
          :is-edit="true"
          @submit="handleVideoUpdate"
          @cancel="closeEditModal"
        />
      </Card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { Video, Plus, Sparkles, Clock, Users, X } from 'lucide-vue-next';
import Card from '../../components/ui/Card.vue';
import Button from '../../components/ui/Button.vue';
import VideoCard from '../../components/VideoCard.vue';
import VideoDetailModal from '../../components/VideoDetailModal.vue';
import VideoUploadForm from '../../components/VideoUploadForm.vue';
import videoAnalysisService from '../../services/videoAnalysisService';

const router = useRouter();
const videos = ref([]);
const selectedVideo = ref(null);
const editingVideo = ref(null);
const loading = ref(true);
const error = ref(null);
const selectedPlayerId = ref('');
const selectedStatus = ref('');
const aiAnalyzedFilter = ref('');

const loadVideos = async () => {
  try {
    loading.value = true;
    error.value = null;
    videos.value = await videoAnalysisService.getAllVideos();
  } catch (err) {
    error.value = 'Failed to load videos: ' + err.message;
  } finally {
    loading.value = false;
  }
};

// Computed properties
const uniquePlayers = computed(() => {
  const playersMap = new Map();
  videos.value.forEach(video => {
    if (video.player && !playersMap.has(video.player.id)) {
      playersMap.set(video.player.id, {
        id: video.player.id,
        name: video.player.name
      });
    }
  });
  return Array.from(playersMap.values()).sort((a, b) => a.name.localeCompare(b.name));
});

const filteredVideos = computed(() => {
  let filtered = videos.value;

  // Filter by player
  if (selectedPlayerId.value) {
    filtered = filtered.filter(v => v.player?.id === parseInt(selectedPlayerId.value));
  }

  // Filter by status
  if (selectedStatus.value) {
    filtered = filtered.filter(v => v.status === selectedStatus.value);
  }

  // Filter by AI analyzed
  if (aiAnalyzedFilter.value === 'analyzed') {
    filtered = filtered.filter(v => v.aiAnalyzed === true);
  } else if (aiAnalyzedFilter.value === 'not-analyzed') {
    filtered = filtered.filter(v => v.aiAnalyzed === false);
  }

  return filtered;
});

const analyzedCount = computed(() => {
  return filteredVideos.value.filter(v => v.aiAnalyzed === true).length;
});

const pendingCount = computed(() => {
  return filteredVideos.value.filter(v => v.status === 'pending' || v.status === 'processing').length;
});

// Event handlers
const selectVideo = (video) => {
  selectedVideo.value = video;
};

const editVideo = (video) => {
  editingVideo.value = video;
};

const closeEditModal = () => {
  editingVideo.value = null;
};

const handleVideoUpdate = async (videoData) => {
  try {
    await videoAnalysisService.updateVideo(editingVideo.value.id, videoData);
    closeEditModal();
    await loadVideos();
  } catch (err) {
    alert('Failed to update video: ' + err.message);
  }
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

const goToPlayerVideos = (playerId) => {
  router.push(`/players/${playerId}/videos`);
};

onMounted(() => {
  loadVideos();
});
</script>
