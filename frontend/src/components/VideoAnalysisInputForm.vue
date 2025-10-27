<template>
  <div class="p-6 space-y-6">
    <div class="flex justify-between items-start">
      <div>
        <h3 class="text-lg font-semibold mb-2">Manual Analysis Input</h3>
        <p class="text-sm text-muted-foreground">
          Enter analysis data manually or import from SwingVision CSV
        </p>
      </div>
      <CSVImportButton :video-id="video.id" @imported="handleCSVImport" />
    </div>

    <form @submit.prevent="handleSubmit" class="space-y-6">
      <!-- Player Selection -->
      <div v-if="availablePlayers.length > 0">
        <Label for="playerId">Analyzing Player *</Label>
        <select
          id="playerId"
          v-model.number="formData.playerId"
          class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
          required
        >
          <option value="">Select player...</option>
          <option v-for="player in availablePlayers" :key="player.id" :value="player.id">
            {{ player.name }}
          </option>
        </select>
        <p class="text-xs text-muted-foreground mt-1">
          Select which player this analysis is for
        </p>
      </div>
      <!-- Strengths Section -->
      <div>
        <h4 class="font-semibold text-md mb-3 flex items-center">
          <TrendingUp class="w-5 h-5 mr-2 text-green-600" />
          Strengths (0-5 scale)
        </h4>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div>
            <Label for="strengthForehand">Forehand</Label>
            <Input
              id="strengthForehand"
              v-model.number="formData.strengthForehandScore"
              type="number"
              step="0.1"
              min="0"
              max="5"
              placeholder="0.0"
            />
          </div>
          <div>
            <Label for="strengthServe">Serve</Label>
            <Input
              id="strengthServe"
              v-model.number="formData.strengthServeScore"
              type="number"
              step="0.1"
              min="0"
              max="5"
              placeholder="0.0"
            />
          </div>
          <div>
            <Label for="strengthVolley">Volley</Label>
            <Input
              id="strengthVolley"
              v-model.number="formData.strengthVolleyScore"
              type="number"
              step="0.1"
              min="0"
              max="5"
              placeholder="0.0"
            />
          </div>
          <div>
            <Label for="strengthMovement">Movement</Label>
            <Input
              id="strengthMovement"
              v-model.number="formData.strengthMovementScore"
              type="number"
              step="0.1"
              min="0"
              max="5"
              placeholder="0.0"
            />
          </div>
        </div>
        <div class="mt-3">
          <Label for="strengthSummary">Strength Summary</Label>
          <textarea
            id="strengthSummary"
            v-model="formData.strengthSummary"
            rows="3"
            class="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            placeholder="Summary of key strengths..."
          ></textarea>
        </div>
      </div>

      <!-- Weaknesses Section -->
      <div>
        <h4 class="font-semibold text-md mb-3 flex items-center">
          <TrendingDown class="w-5 h-5 mr-2 text-red-600" />
          Weaknesses (0-5 scale)
        </h4>
        <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
          <div>
            <Label for="weaknessBackhand">Backhand Errors</Label>
            <Input
              id="weaknessBackhand"
              v-model.number="formData.weaknessBackhandScore"
              type="number"
              step="0.1"
              min="0"
              max="5"
              placeholder="0.0"
            />
          </div>
          <div>
            <Label for="weaknessConsistency">Consistency Issues</Label>
            <Input
              id="weaknessConsistency"
              v-model.number="formData.weaknessConsistencyScore"
              type="number"
              step="0.1"
              min="0"
              max="5"
              placeholder="0.0"
            />
          </div>
          <div>
            <Label for="weaknessPressure">Under Pressure</Label>
            <Input
              id="weaknessPressure"
              v-model.number="formData.weaknessPressureScore"
              type="number"
              step="0.1"
              min="0"
              max="5"
              placeholder="0.0"
            />
          </div>
        </div>
        <div class="mt-3">
          <Label for="weaknessSummary">Weakness Summary</Label>
          <textarea
            id="weaknessSummary"
            v-model="formData.weaknessSummary"
            rows="3"
            class="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            placeholder="Summary of areas for improvement..."
          ></textarea>
        </div>
      </div>

      <!-- Tactical Analysis Section -->
      <div>
        <h4 class="font-semibold text-md mb-3 flex items-center">
          <Target class="w-5 h-5 mr-2 text-blue-600" />
          Tactical Analysis
        </h4>
        <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
          <div>
            <Label for="tacticalStyle">Playing Style</Label>
            <select
              id="tacticalStyle"
              v-model="formData.tacticalStyle"
              class="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            >
              <option value="">Select style...</option>
              <option value="aggressive baseline">Aggressive Baseline</option>
              <option value="defensive baseline">Defensive Baseline</option>
              <option value="serve-and-volley">Serve-and-Volley</option>
              <option value="all-court">All-Court</option>
              <option value="counter-puncher">Counter-Puncher</option>
            </select>
          </div>
          <div>
            <Label for="aggressionIndex">Aggression Index (%)</Label>
            <Input
              id="aggressionIndex"
              v-model.number="formData.aggressionIndex"
              type="number"
              step="0.1"
              min="0"
              max="100"
              placeholder="0.0"
            />
          </div>
          <div>
            <Label for="netApproach">Net Approach Frequency (%)</Label>
            <Input
              id="netApproach"
              v-model.number="formData.netApproachFrequency"
              type="number"
              step="0.1"
              min="0"
              max="100"
              placeholder="0.0"
            />
          </div>
        </div>
        <div class="mt-3">
          <Label for="tacticalSummary">Tactical Summary</Label>
          <textarea
            id="tacticalSummary"
            v-model="formData.tacticalSummary"
            rows="3"
            class="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
            placeholder="Tactical patterns and tendencies..."
          ></textarea>
        </div>
      </div>

      <!-- Recommendations Section -->
      <div>
        <h4 class="font-semibold text-md mb-3 flex items-center">
          <Lightbulb class="w-5 h-5 mr-2 text-yellow-600" />
          Recommendations
        </h4>
        <div class="space-y-3">
          <div>
            <Label for="trainingFocusAreas">Training Focus Areas</Label>
            <textarea
              id="trainingFocusAreas"
              v-model="formData.trainingFocusAreas"
              rows="2"
              class="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
              placeholder="Key areas to focus on in training..."
            ></textarea>
          </div>
          <div>
            <Label for="aiRecommendations">Detailed Recommendations</Label>
            <textarea
              id="aiRecommendations"
              v-model="formData.aiRecommendations"
              rows="4"
              class="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm"
              placeholder="Detailed recommendations for improvement..."
            ></textarea>
          </div>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="flex gap-3 justify-end pt-4 border-t">
        <Button type="button" variant="outline" @click="$emit('cancel')">
          Cancel
        </Button>
        <Button type="submit" :disabled="submitting">
          {{ submitting ? 'Saving...' : 'Save Analysis' }}
        </Button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { TrendingUp, TrendingDown, Target, Lightbulb } from 'lucide-vue-next';
import Button from './ui/Button.vue';
import Input from './ui/Input.vue';
import Label from './ui/Label.vue';
import CSVImportButton from './CSVImportButton.vue';

const props = defineProps({
  video: {
    type: Object,
    required: true
  },
  initialData: {
    type: Object,
    default: null
  },
  availablePlayers: {
    type: Array,
    default: () => []
  }
});

const emit = defineEmits(['submit', 'cancel']);

const submitting = ref(false);

const formData = ref({
  playerId: null,
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
  tacticalSummary: '',
  aiRecommendations: '',
  trainingFocusAreas: ''
});

// Load initial data if provided
watch(() => props.initialData, (newData) => {
  if (newData) {
    // Set player ID from the video's player
    if (newData.player && newData.player.id) {
      formData.value.playerId = newData.player.id;
    }

    // Load other analysis data
    Object.keys(formData.value).forEach(key => {
      if (key !== 'playerId' && newData[key] !== undefined && newData[key] !== null) {
        formData.value[key] = newData[key];
      }
    });
  }
}, { immediate: true });

const handleSubmit = async () => {
  try {
    submitting.value = true;
    emit('submit', formData.value);
  } catch (err) {
    console.error('Error submitting analysis:', err);
  } finally {
    submitting.value = false;
  }
};

// Handle CSV import
const handleCSVImport = (csvData) => {
  Object.keys(csvData).forEach(key => {
    if (formData.value.hasOwnProperty(key) && csvData[key] !== null && csvData[key] !== '') {
      formData.value[key] = csvData[key];
    }
  });
};

// Expose method to populate form from CSV data
const populateFromCSV = (csvData) => {
  handleCSVImport(csvData);
};

defineExpose({
  populateFromCSV
});
</script>
