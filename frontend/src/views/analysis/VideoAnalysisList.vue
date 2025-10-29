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
          <Card v-else>
            <CardContent class="pt-6">
              <!-- Video Basic Information Section -->
              <div class="mb-6 pb-6 border-b border-border">
                <div class="flex justify-between items-center mb-4">
                  <h4 class="font-semibold text-lg">Video Information</h4>
                  <div v-if="!editingVideoInfo" class="flex gap-2">
                    <Button
                      v-if="selectedVideo.videoUrl"
                      size="sm"
                      variant="default"
                      @click="watchVideo"
                      title="Watch Video"
                    >
                      <Video class="w-4 h-4" />
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
              <div>
                <!-- Tabs for existing analyses -->
                <div v-if="selectedVideoAnalyses.length > 0 && !addingNewAnalysis">
                  <!-- Tab Headers with Add Button -->
                  <div class="flex justify-between items-center mb-4 border-b border-border">
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
                  <div v-if="selectedAnalysis">
                    <!-- View Mode -->
                    <div v-if="editingAnalysis?.id !== selectedAnalysis.id">
                      <AnalysisCard
                        :analysis="selectedAnalysis"
                        @edit="startEditAnalysis"
                        @delete="deleteAnalysis"
                      />
                    </div>

                    <!-- Edit Mode -->
                    <div v-else class="space-y-4">
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
                <div v-else-if="addingNewAnalysis">
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
import { Video, Clock, Users, ExternalLink, ArrowLeft, Plus, Edit2 } from 'lucide-vue-next';
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

onMounted(() => {
  loadData();
});
</script>
