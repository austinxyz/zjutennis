<template>
  <div class="space-y-6">
    <!-- Header -->
    <div class="flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-foreground">Video Analysis</h1>
        <p class="text-sm text-muted-foreground mt-1">
          Browse videos and player performance analyses
        </p>
      </div>
    </div>

    <!-- Loading State -->
    <div v-if="loading" class="text-center py-12">
      <p class="text-muted-foreground">Loading videos...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="text-center py-12">
      <p class="text-destructive">{{ error }}</p>
    </div>

    <!-- Content -->
    <template v-else>
      <!-- Empty State -->
      <Card v-if="videos.length === 0">
        <CardContent class="pt-6">
          <div class="text-center py-12">
            <Video class="w-16 h-16 mx-auto text-muted-foreground mb-4 opacity-50" />
            <h3 class="text-lg font-semibold mb-2">No Videos Found</h3>
            <p class="text-muted-foreground mb-6">
              Videos are added through match records. Go to a match and add a video to enable analysis.
            </p>
            <Button @click="$router.push('/matches')">
              <ArrowLeft class="w-4 h-4 mr-2" />
              View Matches
            </Button>
          </div>
        </CardContent>
      </Card>

      <!-- Master-Detail Layout -->
      <div v-else class="grid grid-cols-12 gap-6">
        <!-- Left Side: Video List -->
        <div class="col-span-12 lg:col-span-4">
          <Card class="sticky top-6">
            <CardContent class="pt-6">
              <h4 class="font-semibold mb-4 flex items-center gap-2">
                <Video class="w-5 h-5" />
                All Videos ({{ videos.length }})
              </h4>

              <div class="space-y-2 max-h-[calc(100vh-200px)] overflow-y-auto pr-2">
                <div
                  v-for="video in videos"
                  :key="video.id"
                  @click="selectVideo(video)"
                  :class="[
                    'p-3 rounded-lg border cursor-pointer transition-all',
                    selectedVideo?.id === video.id
                      ? 'border-primary bg-primary/5 shadow-sm'
                      : 'border-border hover:border-primary/50 hover:bg-muted/50'
                  ]"
                >
                  <div class="flex justify-between items-start mb-1">
                    <div class="flex-1 min-w-0">
                      <p class="font-medium text-sm truncate">
                        {{ video.description || 'Match Video' }}
                      </p>
                      <p class="text-xs text-muted-foreground">
                        {{ video.tournamentName || 'N/A' }}
                      </p>
                    </div>
                    <Badge variant="secondary" class="ml-2 shrink-0">
                      {{ video.analysisCount || 0 }}
                    </Badge>
                  </div>
                  <p class="text-xs text-muted-foreground">
                    {{ formatDate(video.matchTime) }}
                  </p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        <!-- Right Side: Video Details & Player Analyses -->
        <div class="col-span-12 lg:col-span-8">
          <Card v-if="!selectedVideo" class="h-full">
            <CardContent class="pt-6">
              <div class="text-center py-24">
                <Video class="w-16 h-16 mx-auto text-muted-foreground mb-4 opacity-50" />
                <h3 class="text-lg font-semibold mb-2">Select a Video</h3>
                <p class="text-muted-foreground">
                  Choose a video from the list to view details and analyses
                </p>
              </div>
            </CardContent>
          </Card>

          <!-- Selected Video Details -->
          <Card v-else class="h-[calc(100vh-200px)]">
            <CardContent class="pt-6 h-full flex flex-col">
              <!-- Video Basic Information Section (Top) -->
              <div class="mb-4 pb-4 border-b border-border flex-shrink-0">
                <div class="flex justify-between items-center mb-4">
                  <h4 class="font-semibold text-lg">Video Information</h4>
                  <div v-if="!editingVideoInfo" class="flex gap-2">
                    <Button
                      v-if="selectedVideo.videoUrl"
                      size="sm"
                      variant="default"
                      @click="showVideoPlayer = true"
                      title="Watch Video"
                    >
                      <Video class="w-4 h-4 mr-2" />
                      Watch Video
                    </Button>
                    <Button
                      size="sm"
                      variant="outline"
                      @click="startEditVideoInfo"
                      title="Edit Video Information"
                    >
                      <Edit2 class="w-4 h-4" />
                    </Button>
                  </div>
                </div>

                <!-- View Mode -->
                <div v-if="!editingVideoInfo">
                  <div class="space-y-2">
                    <div class="flex items-center gap-2">
                      <Badge variant="outline">{{ selectedVideo.tournamentName || 'Match Video' }}</Badge>
                      <Badge variant="secondary">{{ formatDate(selectedVideo.matchTime) }}</Badge>
                      <Badge v-if="selectedVideo.durationSeconds" variant="outline">
                        {{ formatDuration(selectedVideo.durationSeconds) }}
                      </Badge>
                    </div>
                    <div v-if="selectedVideo.description">
                      <p class="text-sm">{{ selectedVideo.description }}</p>
                    </div>

                    <!-- Video Statistics -->
                    <div v-if="hasStatistics(selectedVideo)" class="grid grid-cols-6 gap-4 p-4 bg-muted rounded-md mt-4">
                      <div v-if="selectedVideo.totalShots" class="text-center">
                        <div class="text-2xl font-bold text-blue-600">{{ selectedVideo.totalShots }}</div>
                        <div class="text-xs text-muted-foreground">Total Shots</div>
                      </div>
                      <div v-if="selectedVideo.winners" class="text-center">
                        <div class="text-2xl font-bold text-green-600">{{ selectedVideo.winners }}</div>
                        <div class="text-xs text-muted-foreground">Winners</div>
                      </div>
                      <div v-if="selectedVideo.errors" class="text-center">
                        <div class="text-2xl font-bold text-red-600">{{ selectedVideo.errors }}</div>
                        <div class="text-xs text-muted-foreground">Errors</div>
                      </div>
                      <div v-if="selectedVideo.aces" class="text-center">
                        <div class="text-2xl font-bold text-purple-600">{{ selectedVideo.aces }}</div>
                        <div class="text-xs text-muted-foreground">Aces</div>
                      </div>
                      <div v-if="selectedVideo.doubleFaults" class="text-center">
                        <div class="text-2xl font-bold text-orange-600">{{ selectedVideo.doubleFaults }}</div>
                        <div class="text-xs text-muted-foreground">Double Faults</div>
                      </div>
                      <div v-if="selectedVideo.runningDistanceMeters" class="text-center">
                        <div class="text-2xl font-bold text-teal-600">{{ Math.round(selectedVideo.runningDistanceMeters) }}m</div>
                        <div class="text-xs text-muted-foreground">Distance</div>
                      </div>
                    </div>
                  </div>
                </div>

                <!-- Edit Mode -->
                <div v-else class="space-y-4">
                  <div>
                    <Label for="description">Description</Label>
                    <Input id="description" v-model="videoForm.description" />
                  </div>
                  <div>
                    <Label for="videoUrl">Video URL</Label>
                    <Input id="videoUrl" v-model="videoForm.videoUrl" />
                  </div>
                  <div>
                    <Label for="thumbnailUrl">Thumbnail URL</Label>
                    <Input id="thumbnailUrl" v-model="videoForm.thumbnailUrl" />
                  </div>
                  <div class="grid grid-cols-2 gap-4">
                    <div>
                      <Label for="durationSeconds">Duration (seconds)</Label>
                      <Input id="durationSeconds" type="number" v-model.number="videoForm.durationSeconds" />
                    </div>
                    <div>
                      <Label for="totalShots">Total Shots</Label>
                      <Input id="totalShots" type="number" v-model.number="videoForm.totalShots" />
                    </div>
                  </div>
                  <div class="grid grid-cols-2 gap-4">
                    <div>
                      <Label for="winners">Winners</Label>
                      <Input id="winners" type="number" v-model.number="videoForm.winners" />
                    </div>
                    <div>
                      <Label for="errors">Errors</Label>
                      <Input id="errors" type="number" v-model.number="videoForm.errors" />
                    </div>
                  </div>
                  <div class="grid grid-cols-3 gap-4">
                    <div>
                      <Label for="aces">Aces</Label>
                      <Input id="aces" type="number" v-model.number="videoForm.aces" />
                    </div>
                    <div>
                      <Label for="doubleFaults">Double Faults</Label>
                      <Input id="doubleFaults" type="number" v-model.number="videoForm.doubleFaults" />
                    </div>
                    <div>
                      <Label for="runningDistance">Running Distance (m)</Label>
                      <Input id="runningDistance" type="number" v-model.number="videoForm.runningDistanceMeters" />
                    </div>
                  </div>
                  <div class="flex gap-2 justify-end">
                    <Button variant="outline" size="sm" @click="cancelEditVideoInfo">Cancel</Button>
                    <Button size="sm" @click="saveVideoInfo">Save Changes</Button>
                  </div>
                </div>
              </div>

              <!-- Player Analyses Section -->
              <div class="flex-1 min-h-0 flex flex-col">
                <!-- Tabs for existing analyses -->
                <div v-if="selectedVideoAnalyses.length > 0 && !addingNewAnalysis" class="flex flex-col flex-1 min-h-0">
                  <!-- Tab Headers with Add Button -->
                  <div class="flex justify-between items-center mb-4 border-b border-border flex-shrink-0">
                    <div class="flex gap-2 overflow-x-auto">
                      <button
                        v-for="(analysis, index) in selectedVideoAnalyses"
                        :key="analysis.id"
                        @click="selectAnalysisTab(index)"
                        :class="[
                          'px-4 py-2 font-medium text-sm whitespace-nowrap transition-colors border-b-2',
                          selectedAnalysisIndex === index
                            ? 'border-primary text-primary'
                            : 'border-transparent text-muted-foreground hover:text-foreground hover:border-border'
                        ]"
                      >
                        {{ analysis.playerName || `Player ${index + 1}` }}
                      </button>
                    </div>
                    <Button
                      v-if="!editingAnalysis"
                      size="sm"
                      variant="ghost"
                      @click="startAddNewAnalysis"
                      title="Add Player Analysis"
                      class="shrink-0"
                    >
                      <Plus class="w-4 h-4" />
                    </Button>
                  </div>

                  <!-- Tab Content - View or Edit Mode -->
                  <div v-if="selectedAnalysis" class="flex-1 min-h-0 overflow-y-auto">
                    <!-- View Mode -->
                    <div v-if="editingAnalysis?.id !== selectedAnalysis.id">
                      <AnalysisCard
                        :analysis="selectedAnalysis"
                        @edit="startEditAnalysis"
                        @delete="deleteAnalysis"
                      />
                    </div>

                    <!-- Edit Mode -->
                    <div v-else>
                      <AnalysisEditForm
                        :analysis="analysisForm"
                        :available-players="availablePlayers"
                        @save="saveAnalysis"
                        @cancel="cancelEditAnalysis"
                      />
                    </div>
                  </div>
                </div>

                <!-- Add New Analysis Form -->
                <div v-else-if="addingNewAnalysis" class="flex-1 min-h-0 overflow-y-auto">
                  <AnalysisEditForm
                    :analysis="analysisForm"
                    :is-new="true"
                    :available-players="availablePlayers"
                    @save="saveNewAnalysis"
                    @cancel="cancelAddNewAnalysis"
                  />
                </div>

                <!-- No Analyses -->
                <div v-else class="text-center py-12 border-2 border-dashed border-border rounded-lg">
                  <Users class="w-12 h-12 mx-auto text-muted-foreground mb-3 opacity-50" />
                  <p class="text-sm text-muted-foreground mb-4">
                    No player analyses yet for this video
                  </p>
                  <Button size="sm" @click="startAddNewAnalysis">
                    <Plus class="w-4 h-4 mr-2" />
                    Add First Analysis
                  </Button>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </template>

    <!-- Floating Video Player Modal -->
    <div
      v-if="showVideoPlayer && selectedVideo?.videoUrl"
      ref="videoPlayerRef"
      class="fixed bg-background rounded-lg shadow-2xl border-2 border-primary/20 z-50 resize-container"
      :style="{
        left: videoPlayerPosition.x + 'px',
        top: videoPlayerPosition.y + 'px',
        width: videoPlayerSize.width + 'px',
        height: videoPlayerSize.height + 'px'
      }"
    >
      <!-- Modal Header -->
      <div
        class="flex justify-between items-center p-3 border-b bg-muted/50 rounded-t-lg cursor-move"
        @mousedown="startDrag"
      >
        <h3 class="font-semibold text-sm">{{ selectedVideo.description || 'Video Player' }}</h3>
        <div class="flex gap-2">
          <Button
            size="sm"
            variant="ghost"
            @click="resetSize"
            @mousedown.stop
            title="Reset size"
          >
            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 8V4m0 0h4M4 4l5 5m11-1V4m0 0h-4m4 0l-5 5M4 16v4m0 0h4m-4 0l5-5m11 5l-5-5m5 5v-4m0 4h-4" />
            </svg>
          </Button>
          <Button
            size="sm"
            variant="ghost"
            @click="showVideoPlayer = false"
            @mousedown.stop
            title="Close video player"
          >
            <X class="w-4 h-4" />
          </Button>
        </div>
      </div>
      <!-- Video Player -->
      <div class="bg-black rounded-b-lg overflow-hidden" style="height: calc(100% - 53px);">
        <iframe
          :src="getEmbedUrl(selectedVideo.videoUrl)"
          class="w-full h-full"
          frameborder="0"
          allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowfullscreen
        ></iframe>
      </div>
      <!-- Resize Handles -->
      <div
        class="absolute bottom-0 right-0 w-4 h-4 cursor-nwse-resize"
        @mousedown.stop="startResize"
        style="background: linear-gradient(135deg, transparent 50%, rgba(59, 130, 246, 0.5) 50%);"
      ></div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import Card from '../../components/ui/Card.vue';
import CardContent from '../../components/ui/CardContent.vue';
import Button from '../../components/ui/Button.vue';
import Badge from '../../components/ui/Badge.vue';
import Input from '../../components/ui/Input.vue';
import Label from '../../components/ui/Label.vue';
import AnalysisCard from '../../components/AnalysisCard.vue';
import AnalysisEditForm from '../../components/AnalysisEditForm.vue';
import { Video, Clock, Users, ExternalLink, ArrowLeft, Plus, Edit2, X } from 'lucide-vue-next';
import videoService from '../../services/videoService';
import videoAnalysisService from '../../services/videoAnalysisService';
import playerService from '../../services/playerService';

const router = useRouter();

const videos = ref([]);
const videoAnalyses = ref({}); // Map of videoId -> analyses[]
const availablePlayers = ref([]);
const selectedVideo = ref(null);
const selectedAnalysisIndex = ref(0);
const editingVideoInfo = ref(false);
const editingAnalysis = ref(null);
const addingNewAnalysis = ref(false);
const loading = ref(true);
const error = ref(null);
const showVideoPlayer = ref(false);
const videoPlayerRef = ref(null);
const videoPlayerPosition = ref({ x: 100, y: 100 });
const videoPlayerSize = ref({ width: 600, height: 390 }); // 16:9 ratio + header
const isDragging = ref(false);
const isResizing = ref(false);
const dragOffset = ref({ x: 0, y: 0 });
const resizeStart = ref({ x: 0, y: 0, width: 0, height: 0 });

const videoForm = ref({
  description: '',
  videoUrl: '',
  thumbnailUrl: '',
  durationSeconds: null,
  totalShots: null,
  winners: null,
  errors: null,
  aces: null,
  doubleFaults: null,
  runningDistanceMeters: null
});

const analysisForm = ref({
  playerId: '',
  strengthForehandScore: null,
  strengthServeScore: null,
  strengthVolleyScore: null,
  strengthMovementScore: null,
  strengthSummary: '',
  weaknessBackhandScore: null,
  weaknessConsistencyScore: null,
  weaknessPressureScore: null,
  weaknessSummary: '',
  tacticalStyle: '',
  aggressionIndex: null,
  netApproachFrequency: null,
  baselineRallyPreference: null,
  tacticalSummary: '',
  aiRecommendations: '',
  trainingFocusAreas: ''
});

const selectedVideoAnalyses = computed(() => {
  if (!selectedVideo.value) return [];
  return videoAnalyses.value[selectedVideo.value.id] || [];
});

const selectedAnalysis = computed(() => {
  if (selectedVideoAnalyses.value.length === 0) return null;
  return selectedVideoAnalyses.value[selectedAnalysisIndex.value] || selectedVideoAnalyses.value[0];
});

// Reset when video changes
watch(selectedVideo, () => {
  selectedAnalysisIndex.value = 0;
  editingVideoInfo.value = false;
  editingAnalysis.value = null;
  addingNewAnalysis.value = false;
});

const loadData = async () => {
  try {
    loading.value = true;
    error.value = null;

    // Load all videos and players in parallel
    const [videosData, playersData] = await Promise.all([
      videoService.getAllVideos(),
      playerService.searchPlayers({})
    ]);

    videos.value = videosData;
    availablePlayers.value = playersData.players || [];

    // Load analyses for each video
    const analysesPromises = videos.value.map(video =>
      videoAnalysisService.getAnalysesByVideo(video.id)
        .then(analyses => {
          videoAnalyses.value[video.id] = analyses;
        })
        .catch(err => {
          console.error(`Error loading analyses for video ${video.id}:`, err);
          videoAnalyses.value[video.id] = [];
        })
    );

    await Promise.all(analysesPromises);

    // Auto-select first video if available
    if (videos.value.length > 0) {
      selectedVideo.value = videos.value[0];
    }
  } catch (err) {
    console.error('Error loading videos:', err);
    error.value = 'Failed to load videos: ' + err.message;
  } finally {
    loading.value = false;
  }
};

const selectVideo = (video) => {
  selectedVideo.value = video;
};

const selectAnalysisTab = (index) => {
  selectedAnalysisIndex.value = index;
  editingAnalysis.value = null;
};

const hasStatistics = (video) => {
  return video.totalShots || video.winners || video.errors ||
    video.aces || video.doubleFaults || video.runningDistanceMeters;
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  });
};

const formatDuration = (seconds) => {
  const mins = Math.floor(seconds / 60);
  const secs = seconds % 60;
  return `${mins}:${secs.toString().padStart(2, '0')}`;
};

const watchVideo = () => {
  if (selectedVideo.value?.videoUrl) {
    window.open(selectedVideo.value.videoUrl, '_blank');
  }
};

const getEmbedUrl = (url) => {
  if (!url) return '';

  // YouTube URL conversion
  if (url.includes('youtube.com/watch')) {
    const videoId = url.split('v=')[1]?.split('&')[0];
    return `https://www.youtube.com/embed/${videoId}`;
  } else if (url.includes('youtu.be/')) {
    const videoId = url.split('youtu.be/')[1]?.split('?')[0];
    return `https://www.youtube.com/embed/${videoId}`;
  }

  // Vimeo URL conversion
  if (url.includes('vimeo.com/')) {
    const videoId = url.split('vimeo.com/')[1]?.split('?')[0];
    return `https://player.vimeo.com/video/${videoId}`;
  }

  // If already an embed URL or unknown format, return as is
  return url;
};

// Video Info Edit Functions
const startEditVideoInfo = () => {
  videoForm.value = {
    description: selectedVideo.value.description || '',
    videoUrl: selectedVideo.value.videoUrl || '',
    thumbnailUrl: selectedVideo.value.thumbnailUrl || '',
    durationSeconds: selectedVideo.value.durationSeconds,
    totalShots: selectedVideo.value.totalShots,
    winners: selectedVideo.value.winners,
    errors: selectedVideo.value.errors,
    aces: selectedVideo.value.aces,
    doubleFaults: selectedVideo.value.doubleFaults,
    runningDistanceMeters: selectedVideo.value.runningDistanceMeters
  };
  editingVideoInfo.value = true;
};

const cancelEditVideoInfo = () => {
  editingVideoInfo.value = false;
};

const saveVideoInfo = async () => {
  try {
    await videoService.updateVideo(selectedVideo.value.id, videoForm.value);
    editingVideoInfo.value = false;
    await loadData();
    // Reselect the video
    selectedVideo.value = videos.value.find(v => v.id === selectedVideo.value.id);
  } catch (err) {
    console.error('Error saving video info:', err);
    alert('Failed to save video information: ' + err.message);
  }
};

// Analysis Edit Functions
const startEditAnalysis = (analysis) => {
  analysisForm.value = {
    playerId: analysis.playerId,
    strengthForehandScore: analysis.strengthForehandScore,
    strengthServeScore: analysis.strengthServeScore,
    strengthVolleyScore: analysis.strengthVolleyScore,
    strengthMovementScore: analysis.strengthMovementScore,
    strengthSummary: analysis.strengthSummary || '',
    weaknessBackhandScore: analysis.weaknessBackhandScore,
    weaknessConsistencyScore: analysis.weaknessConsistencyScore,
    weaknessPressureScore: analysis.weaknessPressureScore,
    weaknessSummary: analysis.weaknessSummary || '',
    tacticalStyle: analysis.tacticalStyle || '',
    aggressionIndex: analysis.aggressionIndex,
    netApproachFrequency: analysis.netApproachFrequency,
    baselineRallyPreference: analysis.baselineRallyPreference,
    tacticalSummary: analysis.tacticalSummary || '',
    aiRecommendations: analysis.aiRecommendations || '',
    trainingFocusAreas: analysis.trainingFocusAreas || ''
  };
  editingAnalysis.value = analysis;
};

const cancelEditAnalysis = () => {
  editingAnalysis.value = null;
};

const saveAnalysis = async (formData) => {
  try {
    await videoAnalysisService.updateAnalysis(editingAnalysis.value.id, formData);
    editingAnalysis.value = null;
    await loadData();
  } catch (err) {
    console.error('Error saving analysis:', err);
    alert('Failed to save analysis: ' + err.message);
  }
};

// Add New Analysis Functions
const startAddNewAnalysis = () => {
  analysisForm.value = {
    playerId: '',
    strengthForehandScore: null,
    strengthServeScore: null,
    strengthVolleyScore: null,
    strengthMovementScore: null,
    strengthSummary: '',
    weaknessBackhandScore: null,
    weaknessConsistencyScore: null,
    weaknessPressureScore: null,
    weaknessSummary: '',
    tacticalStyle: '',
    aggressionIndex: null,
    netApproachFrequency: null,
    baselineRallyPreference: null,
    tacticalSummary: '',
    aiRecommendations: '',
    trainingFocusAreas: ''
  };
  addingNewAnalysis.value = true;
};

const cancelAddNewAnalysis = () => {
  addingNewAnalysis.value = false;
};

const saveNewAnalysis = async (formData) => {
  try {
    const analysisData = {
      ...formData,
      videoId: selectedVideo.value.id
    };
    await videoAnalysisService.createAnalysis(analysisData);
    addingNewAnalysis.value = false;
    await loadData();
  } catch (err) {
    console.error('Error creating analysis:', err);
    alert('Failed to create analysis: ' + err.message);
  }
};

const deleteAnalysis = async (analysisId) => {
  if (!confirm('Are you sure you want to delete this analysis?')) {
    return;
  }

  try {
    await videoAnalysisService.deleteAnalysis(analysisId);
    await loadData();
  } catch (err) {
    console.error('Error deleting analysis:', err);
    alert('Failed to delete analysis: ' + err.message);
  }
};

// Drag functionality for video player
const startDrag = (e) => {
  // Only drag from header, not the video itself
  if (e.target.tagName === 'IFRAME') return;

  isDragging.value = true;
  dragOffset.value = {
    x: e.clientX - videoPlayerPosition.value.x,
    y: e.clientY - videoPlayerPosition.value.y
  };

  document.addEventListener('mousemove', onDrag);
  document.addEventListener('mouseup', stopDrag);
};

const onDrag = (e) => {
  if (!isDragging.value) return;

  videoPlayerPosition.value = {
    x: e.clientX - dragOffset.value.x,
    y: e.clientY - dragOffset.value.y
  };
};

const stopDrag = () => {
  isDragging.value = false;
  document.removeEventListener('mousemove', onDrag);
  document.removeEventListener('mouseup', stopDrag);
};

// Resize functionality for video player
const startResize = (e) => {
  isResizing.value = true;
  resizeStart.value = {
    x: e.clientX,
    y: e.clientY,
    width: videoPlayerSize.value.width,
    height: videoPlayerSize.value.height
  };

  document.addEventListener('mousemove', onResize);
  document.addEventListener('mouseup', stopResize);
};

const onResize = (e) => {
  if (!isResizing.value) return;

  const deltaX = e.clientX - resizeStart.value.x;
  const deltaY = e.clientY - resizeStart.value.y;

  // Set minimum size
  const minWidth = 400;
  const minHeight = 300;

  videoPlayerSize.value = {
    width: Math.max(minWidth, resizeStart.value.width + deltaX),
    height: Math.max(minHeight, resizeStart.value.height + deltaY)
  };
};

const stopResize = () => {
  isResizing.value = false;
  document.removeEventListener('mousemove', onResize);
  document.removeEventListener('mouseup', stopResize);
};

const resetSize = () => {
  videoPlayerSize.value = { width: 600, height: 390 };
};

onMounted(() => {
  loadData();
});
</script>
