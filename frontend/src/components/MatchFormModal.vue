<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
    <div class="bg-card rounded-lg shadow-xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
      <!-- Header -->
      <div class="sticky top-0 bg-card border-b border-border px-6 py-4 flex justify-between items-center">
        <h2 class="text-xl font-bold">{{ isEdit ? 'Edit Match' : 'Create Match' }}</h2>
        <button @click="$emit('close')" class="text-muted-foreground hover:text-foreground">
          <X class="w-6 h-6" />
        </button>
      </div>

      <!-- Form -->
      <form @submit.prevent="handleSubmit" class="p-6 space-y-6">
        <!-- Basic Information -->
        <div class="space-y-4">
          <h3 class="text-lg font-semibold">Basic Information</h3>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <Label for="matchType">Match Type</Label>
              <select
                id="matchType"
                v-model="formData.matchType"
                class="w-full px-3 py-2 border border-input rounded-md bg-background"
                required
              >
                <option value="">Select type</option>
                <option value="singles">Singles</option>
                <option value="doubles">Doubles</option>
              </select>
            </div>

            <div>
              <Label for="matchTime">Match Date & Time</Label>
              <Input
                id="matchTime"
                type="datetime-local"
                v-model="formData.matchTime"
                required
              />
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <Label for="location">Location</Label>
              <Input
                id="location"
                v-model="formData.location"
                placeholder="Court location"
              />
            </div>

            <div>
              <Label for="surface">Surface</Label>
              <select
                id="surface"
                v-model="formData.surface"
                class="w-full px-3 py-2 border border-input rounded-md bg-background"
              >
                <option value="">Select surface</option>
                <option value="hard">Hard</option>
                <option value="clay">Clay</option>
                <option value="grass">Grass</option>
                <option value="carpet">Carpet</option>
              </select>
            </div>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div>
              <Label for="tournamentName">Tournament Name</Label>
              <Input
                id="tournamentName"
                v-model="formData.tournamentName"
                placeholder="Optional"
              />
            </div>

            <div>
              <Label for="round">Round</Label>
              <Input
                id="round"
                v-model="formData.round"
                placeholder="e.g., Final, Semi-final"
              />
            </div>
          </div>

          <div class="flex items-center gap-4">
            <input
              type="checkbox"
              id="indoor"
              v-model="formData.indoor"
              class="w-4 h-4"
            />
            <Label for="indoor" class="mb-0">Indoor Match</Label>
          </div>
        </div>

        <!-- Match Result -->
        <div class="space-y-4">
          <h3 class="text-lg font-semibold">Match Result</h3>

          <div class="grid grid-cols-3 gap-4">
            <div>
              <Label for="score">Score</Label>
              <Input
                id="score"
                v-model="formData.score"
                placeholder="e.g., 6-4, 6-3"
              />
            </div>

            <div>
              <Label for="result">Result Status</Label>
              <select
                id="result"
                v-model="formData.result"
                class="w-full px-3 py-2 border border-input rounded-md bg-background"
                required
              >
                <option value="complete">Complete</option>
                <option value="retired">Retired</option>
                <option value="default">Default</option>
                <option value="double_default">Double Default</option>
              </select>
            </div>

            <div>
              <Label for="winnerSide">Winner</Label>
              <select
                id="winnerSide"
                v-model="formData.winnerSide"
                class="w-full px-3 py-2 border border-input rounded-md bg-background"
              >
                <option value="">Not decided</option>
                <option value="team1">Team 1 (Our Team)</option>
                <option value="team2">Team 2 (Opponent)</option>
              </select>
            </div>
          </div>

          <div>
            <Label for="durationMinutes">Duration (minutes)</Label>
            <Input
              id="durationMinutes"
              type="number"
              v-model.number="formData.durationMinutes"
              placeholder="Match duration"
            />
          </div>
        </div>

        <!-- Players -->
        <div class="space-y-4">
          <h3 class="text-lg font-semibold">Players</h3>

          <!-- Team 1 (Our Team) -->
          <div class="border border-border rounded-lg p-4">
            <h4 class="font-medium mb-3">Team 1 (Our Team)</h4>
            <div class="space-y-3">
              <!-- Player 1 -->
              <div class="flex gap-2">
                <select
                  v-model="formData.player1Id"
                  @change="onPlayerSelect('player1', $event)"
                  class="flex-1 px-3 py-2 border border-input rounded-md bg-background"
                >
                  <option value="">Select player</option>
                  <option v-for="p in availablePlayers" :key="p.id" :value="p.id">
                    {{ p.name }}
                  </option>
                </select>
                <Input
                  v-model="formData.player1Name"
                  placeholder="Or enter name"
                  class="flex-1"
                  :disabled="!!formData.player1Id"
                />
              </div>

              <!-- Player 2 (only for doubles) -->
              <div v-if="formData.matchType === 'doubles'" class="flex gap-2">
                <select
                  v-model="formData.player2Id"
                  @change="onPlayerSelect('player2', $event)"
                  class="flex-1 px-3 py-2 border border-input rounded-md bg-background"
                >
                  <option value="">Select player</option>
                  <option v-for="p in availablePlayers" :key="p.id" :value="p.id">
                    {{ p.name }}
                  </option>
                </select>
                <Input
                  v-model="formData.player2Name"
                  placeholder="Or enter player 2 name"
                  class="flex-1"
                  :disabled="!!formData.player2Id"
                />
              </div>
            </div>
          </div>

          <!-- Team 2 (Opponent) -->
          <div class="border border-border rounded-lg p-4">
            <h4 class="font-medium mb-3">Team 2 (Opponent)</h4>
            <div class="space-y-3">
              <!-- Opponent Player 1 -->
              <div class="flex gap-2">
                <Input
                  v-model="formData.opponentPlayer1Name"
                  placeholder="Enter opponent name"
                  class="flex-1"
                  required
                />
              </div>

              <!-- Opponent Player 2 (only for doubles) -->
              <div v-if="formData.matchType === 'doubles'" class="flex gap-2">
                <Input
                  v-model="formData.opponentPlayer2Name"
                  placeholder="Enter opponent 2 name"
                  class="flex-1"
                  required
                />
              </div>
            </div>
          </div>
        </div>

        <!-- Notes -->
        <div>
          <Label for="notes">Notes</Label>
          <Textarea
            id="notes"
            v-model="formData.notes"
            placeholder="Additional notes about the match"
            rows="3"
          />
        </div>

        <!-- Video Section (only show when editing existing match) -->
        <div v-if="isEdit && props.match?.id" class="space-y-4 pt-4 border-t border-border">
          <div class="flex justify-between items-center">
            <h3 class="text-lg font-semibold">Match Video</h3>
            <Button
              v-if="!showVideoForm && !matchVideo"
              type="button"
              size="sm"
              variant="outline"
              @click="showVideoForm = true"
            >
              <Plus class="w-4 h-4 mr-2" />
              Add Video
            </Button>
          </div>

          <!-- Existing Video Display -->
          <div v-if="matchVideo && !showVideoForm" class="border border-border rounded-lg p-4">
            <div class="flex justify-between items-start">
              <div class="flex-1">
                <h4 class="font-medium">Match Video</h4>
                <p class="text-sm text-muted-foreground mt-1" v-if="matchVideo.description">
                  {{ matchVideo.description }}
                </p>
                <p class="text-sm mt-2" v-if="matchVideo.videoUrl">
                  <a :href="matchVideo.videoUrl" target="_blank" class="text-primary hover:underline flex items-center gap-1">
                    <Video class="w-4 h-4" />
                    {{ matchVideo.videoUrl }}
                  </a>
                </p>
                <p class="text-sm text-muted-foreground mt-1" v-if="matchVideo.analyses && matchVideo.analyses.length > 0">
                  {{ matchVideo.analyses.length }} analysis available
                </p>
              </div>
              <div class="flex gap-2">
                <Button type="button" size="sm" variant="outline" @click="editMatchVideo">
                  <Edit2 class="w-4 h-4" />
                </Button>
                <Button type="button" size="sm" variant="destructive" @click="deleteMatchVideo">
                  <Trash2 class="w-4 h-4" />
                </Button>
              </div>
            </div>
          </div>

          <!-- Video Upload Form -->
          <VideoUploadForm
            v-if="showVideoForm"
            :video="editingVideo"
            :is-edit="!!editingVideo"
            :match-id="props.match?.id"
            :player-id="defaultPlayerId"
            @submit="handleVideoSubmit"
            @cancel="cancelVideoForm"
          />
        </div>

        <!-- Actions -->
        <div class="flex justify-end gap-3 pt-4 border-t border-border">
          <Button type="button" variant="outline" @click.prevent="handleCancel">
            Cancel
          </Button>
          <Button type="submit" :disabled="saving">
            {{ saving ? 'Saving...' : (isEdit ? 'Update Match' : 'Create Match') }}
          </Button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import Button from './ui/Button.vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';
import Textarea from './ui/Textarea.vue';
import { X, Plus, Trash2, Edit2, Video } from 'lucide-vue-next';
import matchService from '../services/matchService';
import playerService from '../services/playerService';
import videoService from '../services/videoService';
import VideoUploadForm from './VideoUploadForm.vue';

const props = defineProps({
  match: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['close', 'save']);

const isEdit = computed(() => !!props.match);
const saving = ref(false);
const availablePlayers = ref([]);

const formData = ref({
  matchType: '',
  matchTime: '',
  location: '',
  tournamentName: '',
  round: '',
  score: '',
  result: 'complete',
  winnerSide: '',
  durationMinutes: null,
  surface: '',
  indoor: false,
  notes: '',
  player1Id: '',
  player1Name: '',
  player2Id: '',
  player2Name: '',
  opponentPlayer1Name: '',
  opponentPlayer2Name: ''
});

// Video management
const matchVideo = ref(null);
const showVideoForm = ref(false);
const editingVideo = ref(null);

// Get first team1 player ID as default for video upload
const defaultPlayerId = computed(() => {
  return formData.value.player1Id || null;
});

const loadPlayers = async () => {
  try {
    const result = await playerService.searchPlayers({});
    availablePlayers.value = result.players || [];
  } catch (error) {
    console.error('Error loading players:', error);
  }
};

const onPlayerSelect = (playerField, event) => {
  const playerId = event.target.value;
  if (playerId) {
    const selectedPlayer = availablePlayers.value.find(p => p.id == playerId);
    if (selectedPlayer) {
      if (playerField === 'player1') {
        formData.value.player1Name = selectedPlayer.name;
      } else if (playerField === 'player2') {
        formData.value.player2Name = selectedPlayer.name;
      }
    }
  }
};

const handleSubmit = async () => {
  saving.value = true;
  try {
    // Prepare match data
    const matchData = {
      matchType: formData.value.matchType,
      matchTime: formData.value.matchTime ? new Date(formData.value.matchTime).toISOString() : null,
      location: formData.value.location,
      tournamentName: formData.value.tournamentName,
      round: formData.value.round,
      score: formData.value.score,
      result: formData.value.result,
      winnerSide: formData.value.winnerSide,
      durationMinutes: formData.value.durationMinutes,
      surface: formData.value.surface,
      indoor: formData.value.indoor,
      notes: formData.value.notes
    };

    // Add player fields - always include names, include IDs only if selected
    matchData.player1Name = formData.value.player1Name;
    if (formData.value.player1Id) {
      matchData.player1 = { id: formData.value.player1Id };
    }

    // Player 2 only for doubles
    if (formData.value.matchType === 'doubles') {
      matchData.player2Name = formData.value.player2Name;
      if (formData.value.player2Id) {
        matchData.player2 = { id: formData.value.player2Id };
      }
    } else {
      // Clear player 2 fields for singles
      matchData.player2Name = null;
      matchData.player2 = null;
    }

    // Add opponent fields
    matchData.opponentPlayer1Name = formData.value.opponentPlayer1Name;
    if (formData.value.matchType === 'doubles') {
      matchData.opponentPlayer2Name = formData.value.opponentPlayer2Name;
    } else {
      // Clear opponent player 2 for singles
      matchData.opponentPlayer2Name = null;
    }

    if (isEdit.value) {
      await matchService.updateMatch(props.match.id, matchData);
    } else {
      await matchService.createMatch(matchData);
    }

    emit('save');
  } catch (error) {
    console.error('Error saving match:', error);
    alert('Failed to save match: ' + (error.response?.data?.message || error.message));
  } finally {
    saving.value = false;
  }
};

const loadMatchVideo = async () => {
  if (!props.match?.id) return;

  try {
    console.log('Loading video for match:', props.match.id);
    console.log('Match object:', props.match);

    // First check if video is already in match object (from list view)
    if (props.match.video) {
      console.log('Found video in match object:', props.match.video);
      matchVideo.value = props.match.video;
    } else {
      console.log('Fetching video from API...');
      // Otherwise fetch from API
      const video = await videoService.getVideoByMatch(props.match.id);
      console.log('Fetched video:', video);
      matchVideo.value = video;
    }

    console.log('matchVideo.value set to:', matchVideo.value);
  } catch (error) {
    console.error('Error loading match video:', error);
    matchVideo.value = null;
  }
};

const handleVideoSubmit = async (videoData) => {
  try {
    // Filter to only include fields that Video entity accepts
    const videoPayload = {
      title: videoData.title,
      description: videoData.description,
      videoUrl: videoData.videoUrl,
      thumbnailUrl: videoData.thumbnailUrl,
      durationSeconds: videoData.durationSeconds,
      matchDate: videoData.matchDate,
      matchId: props.match?.id
    };

    if (editingVideo.value) {
      await videoService.updateVideo(editingVideo.value.id, videoPayload);
    } else {
      await videoService.createVideo(videoPayload);
    }

    await loadMatchVideo();
    showVideoForm.value = false;
    editingVideo.value = null;
  } catch (error) {
    console.error('Error saving video:', error);
    alert('Failed to save video: ' + error.message);
  }
};

const editMatchVideo = () => {
  editingVideo.value = matchVideo.value;
  showVideoForm.value = true;
};

const deleteMatchVideo = async () => {
  if (!confirm('Are you sure you want to delete this video?')) {
    return;
  }

  try {
    await videoService.deleteVideo(matchVideo.value.id);
    matchVideo.value = null;
  } catch (error) {
    console.error('Error deleting video:', error);
    alert('Failed to delete video: ' + error.message);
  }
};

const cancelVideoForm = () => {
  showVideoForm.value = false;
  editingVideo.value = null;
};

const handleCancel = () => {
  emit('close');
};

onMounted(async () => {
  await loadPlayers();

  if (props.match) {
    // Populate form with existing match data
    formData.value = {
      matchType: props.match.matchType,
      matchTime: props.match.matchTime ? new Date(props.match.matchTime).toISOString().slice(0, 16) : '',
      location: props.match.location || '',
      tournamentName: props.match.tournamentName || '',
      round: props.match.round || '',
      score: props.match.score || '',
      result: props.match.result || 'complete',
      winnerSide: props.match.winnerSide || '',
      durationMinutes: props.match.durationMinutes || null,
      surface: props.match.surface || '',
      indoor: props.match.indoor || false,
      notes: props.match.notes || '',
      player1Id: props.match.player1?.id || '',
      player1Name: props.match.player1Name || '',
      player2Id: props.match.player2?.id || '',
      player2Name: props.match.player2Name || '',
      opponentPlayer1Name: props.match.opponentPlayer1Name || '',
      opponentPlayer2Name: props.match.opponentPlayer2Name || ''
    };

    // Load match video if editing
    await loadMatchVideo();
  }
});
</script>
