<template>
  <Card class="p-6">
    <h3 class="text-lg font-semibold mb-4">{{ isEdit ? 'Edit' : 'Add' }} Video Analysis</h3>

    <form @submit.prevent="handleSubmit">
      <!-- Basic Information -->
      <div class="space-y-4">
        <div v-if="!matchId">
          <Label for="title">Video Title *</Label>
          <Input
            id="title"
            v-model="formData.title"
            placeholder="e.g., Singles Match vs John Doe - Oct 2025"
            required
          />
        </div>

        <div>
          <Label for="description">Description</Label>
          <Textarea
            id="description"
            v-model="formData.description"
            :rows="3"
            placeholder="Brief description of the match or training session..."
          />
        </div>

        <div>
          <Label for="videoUrl">Video URL (YouTube, Vimeo, etc.) *</Label>
          <Input
            id="videoUrl"
            v-model="formData.videoUrl"
            type="url"
            placeholder="https://youtube.com/watch?v=..."
            required
          />
        </div>

        <div>
          <Label for="thumbnailUrl">Thumbnail URL (optional)</Label>
          <Input
            id="thumbnailUrl"
            v-model="formData.thumbnailUrl"
            type="url"
            placeholder="https://..."
          />
        </div>

        <div v-if="!matchId">
          <Label for="matchId">Link to Match (optional)</Label>
          <select
            id="matchId"
            v-model="formData.matchId"
            class="w-full px-3 py-2 border border-input rounded-md bg-background"
          >
            <option value="">No match selected</option>
            <option v-for="match in availableMatches" :key="match.id" :value="match.id">
              {{ formatMatchOption(match) }}
            </option>
          </select>
        </div>

        <div v-if="!matchId" class="grid grid-cols-2 gap-4">
          <div>
            <Label for="matchDate">Match Date</Label>
            <Input
              id="matchDate"
              v-model="formData.matchDate"
              type="date"
            />
          </div>
          <div>
            <Label for="durationSeconds">Duration (seconds)</Label>
            <Input
              id="durationSeconds"
              v-model.number="formData.durationSeconds"
              type="number"
              placeholder="e.g., 3600"
            />
          </div>
        </div>

        <div v-if="matchId">
          <Label for="durationSeconds">Duration (seconds)</Label>
          <Input
            id="durationSeconds"
            v-model.number="formData.durationSeconds"
            type="number"
            placeholder="e.g., 3600"
          />
        </div>
      </div>

      <!-- Player Selection (only when adding video to a match) -->
      <div v-if="matchId && !isEdit && availablePlayers.length > 0" class="mt-6">
        <h4 class="font-semibold mb-3">Select Players to Analyze</h4>
        <p class="text-sm text-muted-foreground mb-3">
          Select one or more players to create individual analysis for each player.
        </p>
        <div class="space-y-2">
          <div v-for="player in availablePlayers" :key="player.id" class="flex items-center gap-3">
            <input
              type="checkbox"
              :id="`player-${player.id}`"
              :value="player.id"
              v-model="selectedPlayerIds"
              class="w-4 h-4 rounded border-input"
            />
            <Label :for="`player-${player.id}`" class="cursor-pointer flex-1">
              {{ player.name }} ({{ player.team === 'team1' ? 'Our Team' : 'Opponents' }})
            </Label>
          </div>
        </div>
      </div>

      <!-- Match Statistics -->
      <div class="mt-6">
        <h4 class="font-semibold mb-3">Match Statistics (optional)</h4>
        <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
          <div>
            <Label for="totalShots">Total Shots</Label>
            <Input
              id="totalShots"
              v-model.number="formData.totalShots"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="winners">Winners</Label>
            <Input
              id="winners"
              v-model.number="formData.winners"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="errors">Errors</Label>
            <Input
              id="errors"
              v-model.number="formData.errors"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="aces">Aces</Label>
            <Input
              id="aces"
              v-model.number="formData.aces"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="doubleFaults">Double Faults</Label>
            <Input
              id="doubleFaults"
              v-model.number="formData.doubleFaults"
              type="number"
              placeholder="0"
            />
          </div>
          <div>
            <Label for="runningDistance">Running Distance (m)</Label>
            <Input
              id="runningDistance"
              v-model.number="formData.runningDistanceMeters"
              type="number"
              step="0.1"
              placeholder="0.0"
            />
          </div>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="flex gap-3 mt-6">
        <Button type="submit" :disabled="saving">
          {{ saving ? 'Saving...' : (isEdit ? 'Update' : 'Create') }}
        </Button>
        <Button type="button" variant="outline" @click="$emit('cancel')">
          Cancel
        </Button>
      </div>
    </form>
  </Card>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import Card from './ui/Card.vue';
import Label from './ui/Label.vue';
import Input from './ui/Input.vue';
import Textarea from './ui/Textarea.vue';
import Button from './ui/Button.vue';
import matchService from '../services/matchService';

const props = defineProps({
  video: {
    type: Object,
    default: null
  },
  isEdit: {
    type: Boolean,
    default: false
  },
  matchId: {
    type: Number,
    default: null
  },
  playerId: {
    type: Number,
    default: null
  },
  availablePlayers: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['submit', 'cancel']);

const saving = ref(false);
const availableMatches = ref([]);
const selectedPlayerIds = ref([]);

const formData = ref({
  title: '',
  description: '',
  videoUrl: '',
  thumbnailUrl: '',
  matchId: '',
  matchDate: '',
  durationSeconds: null,
  totalShots: null,
  winners: null,
  errors: null,
  aces: null,
  doubleFaults: null,
  runningDistanceMeters: null
});

const loadMatches = async () => {
  try {
    availableMatches.value = await matchService.getAllMatches();
  } catch (error) {
    console.error('Error loading matches:', error);
  }
};

const formatMatchOption = (match) => {
  const date = new Date(match.matchTime).toLocaleDateString();
  const tournament = match.tournamentName || 'Match';
  return `${date} - ${tournament} (${match.matchType})`;
};

// Load video data if editing
watch(() => props.video, (newVideo) => {
  if (newVideo) {
    // Format date for input[type="date"]
    let matchDate = '';
    if (newVideo.matchDate) {
      const date = new Date(newVideo.matchDate);
      // Format as YYYY-MM-DD
      matchDate = date.toISOString().split('T')[0];
    }

    formData.value = {
      title: newVideo.title || '',
      description: newVideo.description || '',
      videoUrl: newVideo.videoUrl || '',
      thumbnailUrl: newVideo.thumbnailUrl || '',
      matchId: newVideo.match?.id || props.matchId || '',
      matchDate: matchDate,
      durationSeconds: newVideo.durationSeconds || null,
      totalShots: newVideo.totalShots || null,
      winners: newVideo.winners || null,
      errors: newVideo.errors || null,
      aces: newVideo.aces || null,
      doubleFaults: newVideo.doubleFaults || null,
      runningDistanceMeters: newVideo.runningDistanceMeters || null
    };
  } else if (props.matchId) {
    // Initialize with matchId prop if no video is being edited
    formData.value.matchId = props.matchId;
  }
}, { immediate: true });

onMounted(() => {
  loadMatches();
});

const handleSubmit = async () => {
  saving.value = true;
  try {
    // Include selected player IDs when submitting
    const submitData = {
      ...formData.value,
      playerIds: selectedPlayerIds.value.length > 0 ? selectedPlayerIds.value : (props.playerId ? [props.playerId] : [])
    };

    await emit('submit', submitData);
    if (!props.isEdit) {
      // Reset form after creating
      formData.value = {
        title: '',
        description: '',
        videoUrl: '',
        thumbnailUrl: '',
        matchId: '',
        matchDate: '',
        durationSeconds: null,
        totalShots: null,
        winners: null,
        errors: null,
        aces: null,
        doubleFaults: null,
        runningDistanceMeters: null
      };
      selectedPlayerIds.value = [];
    }
  } finally {
    saving.value = false;
  }
};
</script>
